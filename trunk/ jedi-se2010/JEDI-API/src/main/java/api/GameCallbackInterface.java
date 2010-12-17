package api;

public interface GameCallbackInterface {
	
	/**
	 * Method to be called before the start of a game 
	 */
	public void onStart();
	
	/**
	 * Method to be called after the game finished
	 */
	public void onFinish();
}
