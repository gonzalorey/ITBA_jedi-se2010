package client.test;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.util.TooManyListenersException;

import api.JEDI_api.JoystickNumbers;
import api.impl.JEDI;


public class Main {
	
	/**
	 * @param args
	 * @throws UnsupportedCommOperationException 
	 * @throws TooManyListenersException 
	 * @throws IOException 
	 * @throws PortInUseException 
	 * @throws NoSuchPortException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws NoSuchPortException, PortInUseException, IOException, TooManyListenersException, UnsupportedCommOperationException, InterruptedException {
		
		System.out.println("Starting...");
		
		//add the event handler
		ButtonEventListener buttonEventListener = new ButtonEventListener();
		ConnectionEventListener connectionEventListener = new ConnectionEventListener();
		
		JEDI jedi = new JEDI(1, JoystickNumbers.JEDI_ONE,11, "COM3", buttonEventListener, connectionEventListener);
		jedi.start();
	
		while(true){
			
			while(jedi.getAcceleration() == null);
			
//			System.out.println("Acceleration: " + jedi.getAcceleration());
//			System.out.println("Buttons: " +  jedi.getPressedButtons());
			Thread.sleep(40);
		}
	}
}
