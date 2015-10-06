package p3_NoTippingGame;

import java.util.*;
public class Board {
	
	public int[] board;
	int[] blockOwner;
	int length;
	int weight;
	public int maxPosi;
	int leftSup;
	int rightSup;
	int p1BlockNo = 0;
	int p2BlockNo = 0;
	
	public Board(int length, int weight, int leftSup, int rightSup){
		board = new int[length+1];
		blockOwner = new int[length+1];
		maxPosi = length/2;
		this.length = length;
		this.weight = weight;
		this.leftSup = posiToIdx(leftSup);
		this.rightSup = posiToIdx(rightSup);
	}
	

	public boolean testAddWithoutTipping(int playerIdx, int posi, int weight){
		if(!canAdd(playerIdx, posi, weight)){ return false; }
		boolean result = false;
		if(add(playerIdx, posi, weight) && !isTipping()){
			result = true;
		}
		remove(playerIdx, posi);
		return result;
	}
	
	public boolean testRemoveWithoutTipping(int playerIdx, int posi){
		if(!canRemove(playerIdx, posi)){ return false; }
		boolean result = false;
		int w = getWeightAt(posi);
		int owner = getBlockOwnerAt(posi);
		if(remove(playerIdx, posi) && !isTipping()){
			result = true;
		}
		add(owner, posi, w);
		return result;
	}
	
	// test if can add in this position, but not really add
	public boolean canAdd(int playerIdx, int posi, int weight){
		int idx = posiToIdx(posi);
		if(board[idx] != 0){ return false; }
		return true;
	}
	
	// test if can remove this position, but not really remove
	public boolean canRemove(int playerIdx, int posi){
		int idx = posiToIdx(posi);
		if(board[idx] == 0){ return false; }
		if(playerIdx == 2 && blockOwner[idx] == 1 && p2BlockNo > 0){ return false; }
		return true;
	}
	
	// shouldn't be used directly by player
	public boolean add(int playerIdx, int posi, int weight){
		int idx = posiToIdx(posi);
		if(board[idx] != 0){ return false; }
		board[idx] = weight;
		blockOwner[idx] = playerIdx;
		if(playerIdx == 1){ p1BlockNo++; } else if(playerIdx == 2) { p2BlockNo++; }
		return true;
	}
	
	public boolean makeAMove(Movement m){
		if(m == null){ return false; }
		if(m.op == "add"){
			boolean success = add(m.playerIdx, m.posi, m.weight);
			return success;
		} else {
			return remove(m.playerIdx, m.posi);
		}
	}
	
	// shouldn't be used directly by player
	public boolean remove(int playerIdx, int posi){
		int idx = posiToIdx(posi);
		if(board[idx] == 0){ return false; }
		if(playerIdx == 2 && blockOwner[idx] == 1 && p2BlockNo > 0){ return false; }
		
		int thisowner = getBlockOwnerAt(posi);
		if(thisowner == 1){ p1BlockNo--; } else if(thisowner == 2) { p2BlockNo--; }
		board[idx] = 0;
		blockOwner[idx] = 0;
		return true;
	}
	
	public int posiToIdx(int posi){
		return posi+maxPosi;
	}
	
	public int IdxToPosi(int idx){
		return idx-maxPosi;
	}
	
	public void printBoard(){
		for(int i=0; i<board.length; i++){
			System.out.print(IdxToPosi(i) + ":" + blockOwner[i] + "_" + board[i] + " ");
		}
		System.out.println();
	}
	
	public List<Integer> getAllPossiblePosi(int playerIdx, int weight){
		List<Integer> result = new ArrayList<Integer>();
		for(int i=0-maxPosi; i<=maxPosi; i++){
			if(testAddWithoutTipping(playerIdx, i, weight)){
				result.add(i); 
			}
		}
		return result;
	}
	
	public int getMostMedianPosi(int playerIdx, int weight){
		int posi = Integer.MAX_VALUE;
		for(int i=0-maxPosi; i<=maxPosi; i++){
			if(testAddWithoutTipping(playerIdx, i, weight)){
				if(Math.abs(i+2) < Math.abs(posi+2)){
					posi = i;
				}
			}
		}
		return posi;
	}
	
	public int getMostMedianBalancePosi(int playerIdx, int weight, boolean toLeft){
		int posi = 26;
		int mid = -2;
		
		List<Integer> list = getAllPossiblePosi(playerIdx, weight);
		for(int i=0; i<list.size(); i++){
			int thisPosi = list.get(i);
			if(toLeft){
				if(thisPosi < mid){ posi = thisPosi; }
				else if(posi == 26){ posi = thisPosi; break; }
			}else{
				if(thisPosi >= mid && posi == 26){ posi = thisPosi; }
			}
		}
		
		if(posi == 26 && list.size() > 0){ posi = list.get(list.size()-1); }
		return posi;
	}
	
	
	public List<Integer> getAllPossibleRemove(int playerIdx){
		List<Integer> result = new ArrayList<Integer>();
		for(int i=0-maxPosi; i<=maxPosi; i++){
			if(testRemoveWithoutTipping(playerIdx, i)){
				result.add(i);
			}
		}
		return result;
	}
	
    public boolean isTipping() {
        int left_torque = 0;
        int right_torque = 0;
        
        for(int i=0-maxPosi; i<=maxPosi; i++){
            left_torque -= (i - (-3)) * getWeightAt(i);
            right_torque -= (i - (-1)) * getWeightAt(i);
        }
        
        //calculate the board weight
        left_torque -= (0 - (-3)) * 3;
        right_torque -= (0 - (-1)) * 3;
        
        boolean gameOver = (left_torque > 0 || right_torque < 0);
        return gameOver;
    }
	
	public int getWeightAt(int posi){
		return board[posiToIdx(posi)];
	}
	
	public int getBlockOwnerAt(int posi){
		return blockOwner[posiToIdx(posi)];
	}
	
	public int getP2BlockNo(){
		return p2BlockNo;
	}

}
