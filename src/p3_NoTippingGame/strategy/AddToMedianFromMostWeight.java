package p3_NoTippingGame.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import p3_NoTippingGame.Board;
import p3_NoTippingGame.Movement;
import p3_NoTippingGame.Strategy;

public class AddToMedianFromMostWeight implements Strategy {
	public String name = "AddToMedianFromMostWeight";

	Movement bestMove = null;
	int depth = 2;
	List<Movement> addHistory = new ArrayList<Movement>();
	int leftCount = 0;
	int rightCount = 0;
	
	@Override
	public Movement addABlock(Board board, List<Integer> blocks, List<Integer> blocksAdver, int playerIdx) {
		if(playerIdx == 1){
			return addAsPlayer1(board, blocks, blocksAdver, playerIdx);
		} else {
			Movement m = addAsPlayer2(board, blocks, blocksAdver, playerIdx, leftCount < rightCount);
			if(m.getPosi() < -2){ leftCount++; }else{ rightCount++; }
			return m;
		} 
	}
	
	private Movement addAsPlayer1(Board board, List<Integer> blocks, List<Integer> blocksAdver, int playerIdx){
		for(int i=blocks.size()-1; i>=0; i--){
			int blockWeight = blocks.get(i);
			
			int medianPosi = board.getMostMedianPosi(playerIdx, blockWeight);
			if(medianPosi != Integer.MAX_VALUE){
				blocks.remove(i);
				Movement m = Movement.getAddMove(playerIdx, medianPosi, blockWeight);
				addHistory.add(m);
				return m;		
			}		
		}
		return null;
	}
	
	private Movement addAsPlayer2(Board board, List<Integer> blocks, List<Integer> blocksAdver, int playerIdx, boolean toLeft){
		for(int i=blocks.size()-1; i>=0; i--){
			int blockWeight = blocks.get(i);
			int medianPosi = board.getMostMedianBalancePosi(playerIdx, blockWeight, toLeft);
			
			if(medianPosi != 26){
				blocks.remove(i);
				Movement m = Movement.getAddMove(playerIdx, medianPosi, blockWeight);
				//System.out.println("movement=" + m.toString());
				addHistory.add(m);
				return m;
			}
		}	
		return null;
	}
	

	@Override
	public Movement removeABlock(Board board, int playerIdx) {
		
		if(playerIdx == 1){
			return removeAsPlayer1(board, playerIdx);
		}else{
			int thisScore = tryMinMaxRemove(new ArrayList<Movement>(), depth, true, board, playerIdx);
			if(thisScore == 1){
				System.out.println("found best score!!");
				System.out.println(bestMove.toString());
				return bestMove;
			}
			return removeAsPlayer2(board, playerIdx);
		}
	}
	
	
	private Movement removeAsPlayer1(Board board, int playerIdx){

		for(int i=0; i<addHistory.size(); i++){
			int posi = addHistory.get(i).getPosi();
			if(board.testRemoveWithoutTipping(playerIdx, posi)){
				Movement m = Movement.getRemoveMove(playerIdx, posi, board.getWeightAt(posi));
				addHistory.remove(i);
				return m;
			}
		}
		
		List<Integer> list = board.getAllPossibleRemove(playerIdx);
		if(list.size() > 0){
			int posi = list.get(list.size()/2);
			return  Movement.getRemoveMove(playerIdx, posi, board.getWeightAt(posi));			
		}else{
			return getRandomRemove(board, playerIdx);
		}

	}
	
	private Movement removeAsPlayer2(Board board, int playerIdx){
		// start removing from my own blocks
		for(int i=0; i<addHistory.size(); i++){
			int posi = addHistory.get(i).getPosi();
			if(board.testRemoveWithoutTipping(playerIdx, posi)){
				Movement m = Movement.getRemoveMove(playerIdx, posi, board.getWeightAt(posi));
				addHistory.remove(i);
				return m;
			}
		}

		// may cause illegal move when player2 have no blue block to remove
		List<Integer> list = board.getAllPossibleRemove(playerIdx);
		if(list.size() > 0){
			int posi = 0;
			// remove from the block far away from board center
			if(list.get(0) < -2){ posi = list.get(0); }
			else{ posi = list.get(list.size()-1); }
			return  Movement.getRemoveMove(playerIdx, posi, board.getWeightAt(posi));
		}else{
			// lose the game
			return getRandomRemove(board, playerIdx);
		}
	}
	
	// used when no possible non-tipping move found
	private Movement getRandomRemove(Board board, int playerIdx){
		//System.out.println("I lose...:(");
		for(int i=0-board.maxPosi; i<=board.maxPosi; i++){
			if(board.getWeightAt(i) > 0){
				return  Movement.getRemoveMove(playerIdx, i, board.getWeightAt(i));
			}
		}
		return null;
	}
	
	
	private int tryMinMaxRemove(List<Movement> currMoves, int depth, boolean maxPlayer, Board board, int playerIdx){
		int thisPlayerIdx = maxPlayer ? playerIdx : (playerIdx % 2)+1;
		List<Integer> removeSpot = board.getAllPossibleRemove(thisPlayerIdx);
		if(removeSpot.size() == 0){  if(maxPlayer){ return -1; } else { return 1; } }
		if(depth == 0){ return 0; }
		
		int bestScore = maxPlayer? -1 : 1;
		for(int i=0; i<removeSpot.size(); i++){
			int posi = removeSpot.get(i);
			int thisW = board.getWeightAt(posi);
			int owner = board.getBlockOwnerAt(posi);
			Movement m = Movement.getRemoveMove(thisPlayerIdx, posi, board.getWeightAt(posi));
			currMoves.add(m);
			board.makeAMove(m);
			int thisScore = tryMinMaxRemove(currMoves, depth-1, !maxPlayer, board, playerIdx);
			board.add(owner, posi, thisW);
			
			if(maxPlayer){
				if(thisScore > bestScore){
					bestScore = thisScore; 
					bestMove = currMoves.get(0);
					if(bestScore == 1){
						return 1; 
					}
				}
			} else {
				if(thisScore < bestScore){
					bestScore = thisScore; 
					bestMove = currMoves.get(0);
					if(bestScore == -1){
						return -1; 
					}
				}
			}
			currMoves.remove(currMoves.size()-1);
		}
		return bestScore;
	}
	
	
	@Override
	public String getName() {
		return name;
	}
	
	public void printMoveList(List<Movement> list){
		for(Movement ele: list){
			System.out.print(ele.getPosi() + " ");;
		}
		System.out.println();
	}

	public void printList(List<Integer> list){
		for(int ele: list){
			System.out.print(ele + " ");;
		}
		System.out.println();
	}
}
