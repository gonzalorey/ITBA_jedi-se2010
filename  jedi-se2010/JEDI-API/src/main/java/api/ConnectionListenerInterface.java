package api;

import event.ConnectionEvent;

public interface ConnectionListenerInterface {

	/**
	 * Event that triggers when the connection starts
	 * @param event Event sent to the handler
	 */
	public void connectionStarted(ConnectionEvent event);
	
	/**
	 * Event that triggers when the connection ends
	 * @param event Event sent to the handler
	 */
	public void connectionEnded(ConnectionEvent event);
}

