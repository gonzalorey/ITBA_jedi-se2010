package client.test;

import api.ButtonListenerInterface;
import event.ButtonEvent;

public class ButtonEventListener implements ButtonListenerInterface {

	@Override
	public void buttonPressed(ButtonEvent e) {
		
		switch (e.getPressedButton()) {
			case ButtonEvent.JEDI_A:
				System.out.println("Pressed button A");
				break;
			case ButtonEvent.JEDI_B:
				System.out.println("Pressed button B");
				break;
			default:
				break;
		}
	}

	@Override
	public void buttonReleased(ButtonEvent e) {
		switch (e.getPressedButton()) {
			case ButtonEvent.JEDI_A:
				System.out.println("Released button A (" + Thread.currentThread().getId() + ")");
				break;
			case ButtonEvent.JEDI_B:
				System.out.println("Released button B (" + Thread.currentThread().getId() + ")");
				break;
			default:
				break;
		}
	}
}
