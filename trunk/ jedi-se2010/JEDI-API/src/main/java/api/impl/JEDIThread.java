package api.impl;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.util.TooManyListenersException;

import api.ButtonListenerInterface;
import api.ConnectionListenerInterface;
import api.impl.JEDI.JoystickNumbers;

public class JEDIThread implements Runnable {

	private int jediID;
	private JoystickNumbers joystickNumber;
	private int serverID;
	private String portName;
	private ButtonListenerInterface buttonEventListener;
	private ConnectionListenerInterface connectionEventLister;
	
	private JEDI jedi;
	
	public JEDIThread(int jediID, JoystickNumbers joystickNumber, int serverID, String portName, ButtonListenerInterface buttonEventListener, ConnectionListenerInterface connectionEventLister) throws NoSuchPortException, PortInUseException, IOException, TooManyListenersException, UnsupportedCommOperationException{
		this.jediID = jediID;
		this.joystickNumber = joystickNumber;
		this.serverID = serverID;
		this.portName = portName;
		this.buttonEventListener = buttonEventListener;
		this.connectionEventLister = connectionEventLister;
	}
	
	@Override
	public void run() {
		jedi = null;
		try {
			jedi = new JEDI(jediID, joystickNumber, serverID, portName, buttonEventListener, connectionEventLister);
		} catch (NoSuchPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PortInUseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TooManyListenersException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedCommOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(jedi != null)
			jedi.start();

	}
	
	public JEDI getJEDI(){
		return jedi;
	}

}
