package pong;

public class Score {
	public int value;
	
	public int x, y;
	
	public Score(int x, int y){
		this.x = x;
		this.y = y;
		
		value = 0;
	}
	
	public void increase(){
		value++;
	}
	
	public int getValue(){
		return value;
	}
}
