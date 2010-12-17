package api;

import javax.swing.JPanel;

public abstract class JEDIGame extends JPanel implements ButtonListenerInterface, ConnectionListenerInterface, CalibrationInterface {

	private static final long serialVersionUID = 1L;

	/**
	 * JEDI_api instances that the game will handle
	 */
	protected JEDI_api jedi1;
	protected JEDI_api jedi2;

	/**
	 * Callback interface to be called at the start and ending of the game
	 */
	private GameCallbackInterface callback;
	
	/**
	 * Constructor of a Game with two JEDIs
	 * 
	 * @param jedi1 Instance of the JEDI_ONE
	 * @param jedi2 Instance of the JEDI_TWO
	 */
	public JEDIGame(JEDI_api jedi1, JEDI_api jedi2) {
		this.jedi1 = jedi1;
		this.jedi2 = jedi2;
	}
	
	/**
	 * Constructor of a Game with just one JEDI
	 * 
	 * @param jedi1 Instance of the JEDI_ONE
	 */
	public JEDIGame(JEDI_api jedi1) {
		this.jedi1 = jedi1;
	}

	/**
	 * Run the game with the current callback function
	 * 
	 * @param callback interface to be exectuted at the start and ending of the game
	 */
	public void run(GameCallbackInterface callback){
		callback.onStart();
		start();
		this.callback = callback;
	}
	
	/**
	 * Gets the callback reference
	 * 
	 * @return gets the callback reference
	 */
	public GameCallbackInterface getCallback(){
		return callback;
	}
	
	/**
	 * Method to be implemented by the caller of the function
	 */
	protected abstract void start();
}
