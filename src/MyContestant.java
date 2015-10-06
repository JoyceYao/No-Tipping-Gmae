
import java.io.*;
import java.util.StringTokenizer;

import p3_NoTippingGame.Board;
import p3_NoTippingGame.Movement;
import p3_NoTippingGame.Player;
import p3_NoTippingGame.strategy.AddToMedianFromMostWeight;

class MyContestant extends NoTippingPlayer {
    private static BufferedReader br;
	private int myPlayerIdx;
	private int adverPlayerIdx;
	private Board board;
	private Player thisPlayer;
	private int count;

    MyContestant(int port) {
        super(port);
    }

    public static void main(String[] args) throws Exception {
        br = new BufferedReader(new InputStreamReader(System.in));
        new MyContestant(Integer.parseInt(args[0]));
    }
    
    
	@Override
	protected String process(String command) {
        System.out.println("process[0]" + command);
		StringTokenizer tk = new StringTokenizer(command);
        command = tk.nextToken();
        int position = Integer.parseInt(tk.nextToken());
        int weight = Integer.parseInt(tk.nextToken());
        
        if(board == null || thisPlayer == null){
            board = new Board(50, 3, -3, -1);
    		// put the first block
    		board.add(0, -4, 3);
            thisPlayer = new Player(board, new AddToMedianFromMostWeight(), 1);
        }
        
        try {
        	updateBoardForAdver(board, position, weight);        	
        	Movement myMove = null;
        	if(thisPlayer.hasBlockLeft()){
        		myMove = thisPlayer.addABlock();
        		System.out.println("process[2] myMove=" + myMove.toString());
        	}else{
        		myMove = thisPlayer.removeABlock();
        		System.out.println("process[3] myMove=" + myMove.toString());
        	}
        	
        	updateBoardForMyself(myMove);
    		board.printBoard();
        	return myMove.getPosi() + " " + myMove.getWeight();
        	
        } catch (Exception ev) {
        	ev.printStackTrace();
            System.out.println(ev.getMessage());
        }
        return "";
	}
	
	private void updateBoardForAdver(Board board, int position, int weight){
		
		// get the command, and opponent's position and weight last round.
		System.out.println("updateBoardForAdver position=" + position);
		System.out.println("updateBoardForAdver weight=" + weight);
		System.out.println("updateBoardForAdver myPlayerIdx == 0=" + (myPlayerIdx == 0));
        if(myPlayerIdx == 0){
        	initialPlayerIdx(position, weight);
        }
		System.out.println("updateBoardForAdver myPlayerIdx=" + myPlayerIdx);
		System.out.println("updateBoardForAdver adverPlayerIdx=" + adverPlayerIdx);
        
        
        if(weight != 0){
        	Movement move;
        	if(count < 15){
        		move = Movement.getAddMove(adverPlayerIdx, position, weight);
        		count++;
        	}else{
        		move = Movement.getRemoveMove(adverPlayerIdx, position, weight);
        	}
        	
        	boolean canMove = board.makeAMove(move);
        	if(!canMove){
    			System.out.println("Illegal Move!!");
    		}
        }
        System.out.println("updateBoardForAdver end!!");
        
	}
	
	private void updateBoardForMyself(Movement m){
		System.out.println("updateBoardForMyself [0]");
		System.out.println("updateBoardForMyself [1] testRemoveWithoutTipping=" + board.testRemoveWithoutTipping(m.getPlayerIdx(), m.getPosi()));
		
		boolean canMove = board.makeAMove(m);
		if(!canMove){
			System.out.println("======== I lose :( ===========");
		}
	}
	
	private void initialPlayerIdx(int posi, int weight){
    	if(posi == 0 && weight == 0){
    		myPlayerIdx = 1;
    		adverPlayerIdx = 2;	
    	}else{
    		myPlayerIdx = 2;
    		adverPlayerIdx = 1;
    	}
		System.out.println("initialPlayerIdx[1] myPlayerIdx=" + myPlayerIdx);
		System.out.println("initialPlayerIdx[1] adverPlayerIdx=" + adverPlayerIdx);
		thisPlayer.setPlayerIdx(myPlayerIdx);
	}
}
