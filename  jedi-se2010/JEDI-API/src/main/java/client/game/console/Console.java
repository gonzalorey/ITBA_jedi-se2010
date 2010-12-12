package client.game.console;

import event.ButtonEvent;
import event.ConnectionEvent;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.util.TooManyListenersException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import pong.Pong;
import api.ButtonListenerInterface;
import api.ConnectionListenerInterface;
import api.GameCallbackInterface;
import api.JEDIGame;
import api.JEDI_api;
import api.JEDI_api.JoystickNumbers;
import api.impl.FakeJEDI;
import api.impl.JEDI;
import client.game.fakejedi.Dashboard;
import client.menu.Menu;

public class Console implements ButtonListenerInterface, ConnectionListenerInterface {

	private static final long serialVersionUID = 1L;

	// ID of this server
	public static int SERVER_ID = 11;

	// jedi instances
	private JEDI_api jediOne;
	private JEDI_api jediTwo;

	private Dashboard dashboard;

	private JEDIGame currentGame;
	private JEDIGame menu;

	private JEDIGame currentPanel;
	
	private JFrame gameFrame;
	private JFrame jediFrame;

	public Console(String jediOnePort) throws NoSuchPortException, 
	PortInUseException, IOException, TooManyListenersException, UnsupportedCommOperationException {

		// initialize the firt JEDI
		try{
			// initialize the JEDI
			jediOne = new JEDI(JoystickNumbers.JEDI_ONE, SERVER_ID, jediOnePort, this, this);

			// start the jedi controller
			((JEDI)jediOne).start();
		}
		catch(Exception e){
			System.out.println("Error communicating with the real JEDI, setting up the fake JEDI instead");

			// Initialize the fake JEDI
			jediOne = new FakeJEDI(this, this, api.impl.FakeJEDI.JoystickNumbers.JEDI_ONE);
		}

		// add it to the dashboad
		dashboard = new Dashboard(jediOne, jediOne);

		// initialize the second JEDI
		//jediTwo = new FakeJEDI(this, this);

		// add them to the dashboad
		//dashboard = new Dashboard(jediOne, jediTwo);

		JFrame gameFrame = new JFrame("Juego");

		currentGame = new Pong(jediOne, jediTwo);

		gameFrame.add(currentGame);

		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setLocationRelativeTo(null);
		gameFrame.setResizable(false);
		gameFrame.setVisible(true);
		gameFrame.setLocation(500, 0);

		JFrame jediFrame = new JFrame("JEDI");

		jediFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jediFrame.setLocationRelativeTo(null);
		jediFrame.setResizable(true);
		jediFrame.setVisible(true);
		jediFrame.setLocation(0, 0);

		//if(jedi instanceof Fakejedi){
		jediFrame.add((JPanel)dashboard);
		//}   
	}

	public Console(String jediOnePort, String jediTwoPort) throws NoSuchPortException, 
	PortInUseException, IOException, TooManyListenersException, UnsupportedCommOperationException {

		// initialize the firt JEDI
		try{
			// initialize the JEDI
			jediOne = new JEDI(JoystickNumbers.JEDI_ONE, SERVER_ID, jediOnePort, this, this);

			// start the jedi controller
			((JEDI)jediOne).start();
		}
		catch(Exception e){
			System.out.println("Error communicating with the real 101 JEDI, setting up the fake JEDI instead");

			// Initialize the fake JEDI
			jediOne = new FakeJEDI(this, this, api.impl.FakeJEDI.JoystickNumbers.JEDI_ONE);
		}
		catch (UnsatisfiedLinkError u) {
			System.out.println("RXTX Library not found");

			jediOne = new FakeJEDI(this, this, api.impl.FakeJEDI.JoystickNumbers.JEDI_ONE);
		}

		// initialize the second JEDI
		try{
			// initialize the JEDI
			jediTwo = new JEDI(JoystickNumbers.JEDI_TWO, SERVER_ID, jediTwoPort, this, this);

			// start the jedi controller
			((JEDI)jediTwo).start();
		}
		catch(Exception e){
			System.out.println("Error communicating with the real 102 JEDI, setting up the fake JEDI instead");

			// Initialize the fake JEDI
			jediTwo = new FakeJEDI(this, this, api.impl.FakeJEDI.JoystickNumbers.JEDI_TWO);
		}

		// add it to the dashboad
		//		dashboard = new Dashboard(jediTwo);

		// add them to the dashboad
		dashboard = new Dashboard(jediOne, jediTwo);

		gameFrame = new JFrame("Juego");

		menu = new Menu(jediOne, jediTwo);
		currentPanel = menu;
		
		System.out.println("adding menu panel to frame");
		gameFrame.add(menu);

		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setSize(400, 300);
		gameFrame.setLocationRelativeTo(null);
		gameFrame.setResizable(false);
		gameFrame.setVisible(true);
		gameFrame.setLocation(500, 0);

		jediFrame = new JFrame("JEDI");

		jediFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jediFrame.setSize(400, 600);
		jediFrame.setLocationRelativeTo(null);
		jediFrame.setResizable(false);
		jediFrame.setVisible(true);
		jediFrame.setLocation(0, 0);

		jediFrame.add((JPanel)dashboard);

		System.out.println("run");
		menu.run(new MenuCallback());       
	}

	public static void main(String[] args) throws NoSuchPortException, PortInUseException, IOException, 
	TooManyListenersException, UnsupportedCommOperationException {
		new Console("COM7", "COM9");
		//new Console("COM7");


		while(true)
		{
			//System.out.println(console.api.getAcceleration());
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void buttonPressed(ButtonEvent e) {
		if(currentPanel != null)
			currentPanel.buttonPressed(e);
		if(dashboard != null)
			dashboard.buttonPressed(e);
	}

	@Override
	public void buttonReleased(ButtonEvent e) {
		if(currentPanel != null)
			currentPanel.buttonReleased(e);
		if(dashboard != null)
			dashboard.buttonReleased(e);
	}

	@Override
	public void connectionStarted(ConnectionEvent e) {
		System.out.println("tu vieja");
		if(currentPanel != null)
			currentPanel.connectionStarted(e);
		if(dashboard != null)
			dashboard.connectionStarted(e);
	}

	@Override
	public void connectionEnded(ConnectionEvent e) {
		if(currentPanel != null)
			currentPanel.connectionEnded(e);
		if(dashboard != null)
			dashboard.connectionEnded(e);
	}

	private class GameCallback implements GameCallbackInterface{

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
		}

		@Override
		public void onFinish() {
			System.out.println("game on finish");
			currentPanel.removeAll();
			gameFrame.remove(currentPanel);
			menu = new Menu(jediOne, jediTwo);
			gameFrame.add(menu);
			menu.repaint();
			currentPanel = menu;
			
			menu.run(new MenuCallback());
		}
	}

	private class MenuCallback implements GameCallbackInterface{

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
		}

		@Override
		public void onFinish() {
			System.out.println("menu on finish");
			gameFrame.remove(menu);
			currentGame = ((Menu) menu).getSelectedGame();
			gameFrame.add(currentGame);
			currentPanel = currentGame;
			gameFrame.repaint();
			
			currentGame.run(new GameCallback());
			
			if(jediOne instanceof FakeJEDI)
				((FakeJEDI) jediOne).forceConnectionEvent(true);
			else 
				if(((JEDI) jediOne).isConnected())
					((JEDI) jediOne).forceConnectionEvent(true);

			if(jediTwo instanceof FakeJEDI)
				((FakeJEDI) jediTwo).forceConnectionEvent(true);
			else 
				if(((JEDI) jediTwo).isConnected())
					((JEDI) jediTwo).forceConnectionEvent(true);
		}
	}
}
