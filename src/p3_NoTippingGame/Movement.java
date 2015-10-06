package p3_NoTippingGame;

public class Movement {
	String op;
	int playerIdx;
	int posi;
	int weight;
	
	private Movement(String op, int playerIdx, int posi, int weight){
		this.op = op;
		this.playerIdx = playerIdx;
		this.posi = posi;
		this.weight = weight;
	}
	
	public static Movement getAddMove(int playerIdx, int posi, int weight){
		return new Movement("add", playerIdx, posi, weight);
	}
	
	public static Movement getRemoveMove(int playerIdx, int posi, int weight){
		return new Movement("remove", playerIdx, posi, weight);
	}
	
	public int getPlayerIdx(){
		return this.playerIdx;
	}
	
	public int getWeight(){
		return this.weight;
	}
	
	public int getPosi(){
		return this.posi;
	}
	
	public String getOP(){
		return this.op;
	}
	
	public String toString(){
		return("op=" + op + " playerIdx=" + playerIdx + " posi=" + posi + " weight=" + weight);
	}
}
