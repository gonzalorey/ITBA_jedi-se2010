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

import api.ButtonListenerInterface;
import api.ConnectionListenerInterface;
import api.JEDI_api;
import api.impl.JEDI;
import client.game.Board;
import client.game.fakejedi.Fakejedi;

public class Console implements ButtonListenerInterface, ConnectionListenerInterface {

	// ID of this server
	public static int SERVER_ID = 11;
	
	private static final long serialVersionUID = 1L;

	private JEDI_api jedi;
	private Fakejedi dashboard;
	
	private Board game;
	
	public Console() throws NoSuchPortException, PortInUseException, IOException, TooManyListenersException, UnsupportedCommOperationException {

		try{
			
			// initialize the JEDI
			jedi = new JEDI(101, SERVER_ID, "COM7", this, this);
			((JEDI)jedi).start();
			
			// initialize the fake JEDI
			dashboard = new Fakejedi(this, this);
			dashboard.setApi(jedi);
			//throw new NoSuchPortException();
		}
		catch(Exception e){
			System.out.println("Setting the jedi as the dashboard");
			jedi = new Fakejedi(this, this);
			dashboard = (Fakejedi)jedi;
			dashboard.setApi(jedi);
			((Fakejedi)jedi).enableEvents();
		}
		
        JFrame gameFrame = new JFrame("Juego");
        
        game = new Board(jedi);
        
        gameFrame.add(game);
        
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setSize(400, 300);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setResizable(false);
        gameFrame.setVisible(true);
        gameFrame.setLocation(500, 0);
		
		JFrame jediFrame = new JFrame("JEDI");

        jediFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jediFrame.setSize(400, 300);
        jediFrame.setLocationRelativeTo(null);
        jediFrame.setResizable(false);
        jediFrame.setVisible(true);
        jediFrame.setLocation(0, 0);
        
        //if(jedi instanceof Fakejedi){
        	jediFrame.add((JPanel)dashboard);
        //}
        
    }
	
	public static void main(String[] args) throws NoSuchPortException, PortInUseException, IOException, TooManyListenersException, UnsupportedCommOperationException {
		new Console();
		
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
		if(game != null)
			game.buttonPressed(e);
		if(dashboard != null)
			dashboard.buttonPressed(e);
	}

	@Override
	public void buttonReleased(ButtonEvent e) {
		if(game != null)
			game.buttonReleased(e);
		if(dashboard != null)
			dashboard.buttonReleased(e);
	}

	@Override
	public void connectionStarted(ConnectionEvent e) {
		if(game != null)
			game.connectionStarted(e);
		if(dashboard != null)
			dashboard.connectionStarted(e);
	}

	@Override
	public void connectionEnded(ConnectionEvent e) {
		if(game != null)
			game.connectionEnded(e);
		if(dashboard != null)
			dashboard.connectionEnded(e);
	}

}
