package api;

import event.ButtonEvent;

public interface ButtonListenerInterface {
	
	/**
	 * Event that triggers when a button is pressed
	 * @param e Event sent to the handler
	 */
	public void buttonPressed(ButtonEvent e);
	
	/**
	 * Event that triggers when a button is released
	 * @param e Event sent to the handler
	 */
	public void buttonReleased(ButtonEvent e);
}

