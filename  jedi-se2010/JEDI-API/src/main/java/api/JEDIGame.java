package api;

import javax.swing.JPanel;

public abstract class JEDIGame extends JPanel implements ButtonListenerInterface, ConnectionListenerInterface, CalibrationInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected JEDI_api jedi1;
	protected JEDI_api jedi2;

	private GameCallbackInterface callback;
	
	public JEDIGame(JEDI_api jedi1, JEDI_api jedi2) {
		this.jedi1 = jedi1;
		this.jedi2 = jedi2;
	}

	public void run(GameCallbackInterface callback){
		callback.onStart();
		start();
		this.callback = callback;
	}
	
	public GameCallbackInterface getCallback(){
		return callback;
	}
	
	protected abstract void start();
}
