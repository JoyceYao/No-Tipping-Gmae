package p3_NoTippingGame;

import java.util.*;
public class Player {

	int playerIdx;
	List<Integer> blocks = new ArrayList<Integer>();
	List<Integer> blocksAdver = new ArrayList<Integer>();
	Board board;
	Strategy strategy;
	
	public Player(Board board, Strategy strategy, int playerIdx){
		this.board = board;
		this.strategy = strategy;
		this.playerIdx = playerIdx;
		for(int i=1; i<=15; i++){
			blocks.add(i);
		}
		
		for(int i=1; i<=15; i++){
			blocksAdver.add(i);
		}
	}
	public Movement addABlock(){
		return strategy.addABlock(board, blocks, blocksAdver, playerIdx);
	}
	
	public Movement removeABlock(){
		return strategy.removeABlock(board, playerIdx);
	}

	// Not in use
	public void updateAdverAddMove(Movement m){
		if(m != null){
			blocksAdver.remove(new Integer(m.weight));
		}
	}
	
	public boolean hasBlockLeft(){
		return blocks.size() > 0;
	}
	
	public void setPlayerIdx(int idx){
		playerIdx = idx;
	}
}
