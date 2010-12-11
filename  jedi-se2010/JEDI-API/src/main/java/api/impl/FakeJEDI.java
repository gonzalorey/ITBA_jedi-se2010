package api.impl;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import api.ButtonListenerInterface;
import api.CalibrationInterface;
import api.ConnectionListenerInterface;
import api.JEDI_api;
import data.Axis;
import data.Buttons;
import event.ButtonEvent;
import event.ConnectionEvent;

public class FakeJEDI implements JEDI_api{

	private double x = 0;
	private double y = 10;
	private double z = 0;
	
	private TAdapter adapter;
	
	public FakeJEDI(ButtonListenerInterface bl, ConnectionListenerInterface cl) {
		addButtonEventListener(bl);
		addConnectionEventListener(cl);
		adapter = new TAdapter();
	}

	public TAdapter getAdapter() {
		return adapter;
	}
	
	private class TAdapter extends KeyAdapter {
		
	    public void keyPressed(KeyEvent e) {
//	    	if (!isFakeEvents())
//	    		return;
	    	
	    	int key = e.getKeyCode();

	        if (key == KeyEvent.VK_Z) {
	            firePressedEvent(ButtonEvent.JEDI_A);
	        }
	        
	        if (key == KeyEvent.VK_X) {
	            firePressedEvent(ButtonEvent.JEDI_B);
	        }
	        
	        if (key == KeyEvent.VK_LEFT) {
	        	x -=10;
	        }

	        if (key == KeyEvent.VK_RIGHT) {
	        	x += 10;
	        }

	        if (key == KeyEvent.VK_UP) {
	        	z -= 10;
	        }

	        if (key == KeyEvent.VK_DOWN) {
	        	z += 10;
	        }
	    }
	
	    public void keyReleased(KeyEvent e) {
//	    	if (!isFakeEvents())
//	    		return;
	    	
	    	int key = e.getKeyCode();

	        if (key == KeyEvent.VK_Z) {
	            fireReleasedEvent(ButtonEvent.JEDI_A);
	        }
	        
	        if (key == KeyEvent.VK_X) {
	            fireReleasedEvent(ButtonEvent.JEDI_B);
	        }
	        
	        if (key == KeyEvent.VK_Q) {
	            fireConnectionStartedEvent();
	        }
	        
	        if (key == KeyEvent.VK_W) {
	            fireConnectionEndedEvent();
	        }

	    	
	    	
	        if (key == KeyEvent.VK_LEFT) {
	        	x += 10;
	        }

	        if (key == KeyEvent.VK_RIGHT) {
	        	x -= 10;
	        }

	        if (key == KeyEvent.VK_UP) {
	        	z += 10;
	        }

	        if (key == KeyEvent.VK_DOWN) {
	        	z -= 10;
	        }
	    }
	}

	@Override
	public Axis getAcceleration() {
		return new Axis(x,y,z);
	}

	@Override
	public Axis getVelocity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Axis getPosition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Axis getDirection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Axis getRotation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Buttons getPressedButtons() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void calibrate(int milliseconds, CalibrationInterface calibrationCallback) {
		calibrationCallback.onStart();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		calibrationCallback.onEnding();
	}
	
	
	private List<ButtonListenerInterface> _listeners = new ArrayList<ButtonListenerInterface>();
	public synchronized void addButtonEventListener(ButtonListenerInterface listener)	{
		_listeners.add(listener);
	}
	public synchronized void removeButtonEventListener(ButtonListenerInterface listener)	{
		_listeners.remove(listener);
	}

	// call this method whenever you want to notify
	//the event listeners of the particular event
	private synchronized void firePressedEvent(int buttonPressed)	{
		ButtonEvent event = new ButtonEvent(this, buttonPressed);
		Iterator<ButtonListenerInterface> i = _listeners.iterator();
		while(i.hasNext())	{
			i.next().buttonPressed(event);
		}
	}

	private synchronized void fireReleasedEvent(int buttonPressed)	{
		ButtonEvent event = new ButtonEvent(this, buttonPressed);
		Iterator<ButtonListenerInterface> i = _listeners.iterator();
		while(i.hasNext())	{
			i.next().buttonReleased(event);
		}
	}

		
	//Connection Listener
	private List<ConnectionListenerInterface> connectionListeners = new ArrayList<ConnectionListenerInterface>();

	public synchronized void addConnectionEventListener(ConnectionListenerInterface listener)  {
		connectionListeners.add(listener);
	}
	public synchronized void removeConnectionEventListener(ConnectionListenerInterface listener)   {
		connectionListeners.remove(listener);
	}

	private synchronized void fireConnectionStartedEvent() {
		ConnectionEvent event = new ConnectionEvent(this);
		Iterator<ConnectionListenerInterface> i = connectionListeners.iterator();
		while(i.hasNext())  {
			i.next().connectionStarted(event);
		}
	}

	private synchronized void fireConnectionEndedEvent() {
		ConnectionEvent event = new ConnectionEvent(this);
		Iterator<ConnectionListenerInterface> i = connectionListeners.iterator();
		while(i.hasNext())  {
			i.next().connectionEnded(event);
		}
	}

}
