package p3_NoTippingGame;

import java.util.List;

public interface Strategy {
	
	String name = "Strategy";
	public Movement addABlock(Board board, List<Integer> blocks, List<Integer> blocksAdver, int playerIdx);
	public Movement removeABlock(Board board, int playerIdx);
	public String getName();
	
}
