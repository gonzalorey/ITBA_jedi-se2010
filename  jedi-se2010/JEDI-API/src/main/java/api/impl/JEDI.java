package api.impl;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TooManyListenersException;

import api.ButtonListenerInterface;
import api.CalibrationInterface;
import api.ConnectionListenerInterface;
import api.JEDI_api;

import comm.CommHandler;
import comm.SerialCommHandler;
import comm.usart.USART;

import data.Axis;
import data.Axis.TypeOfData;
import data.Buttons;
import data.FixedQueue;

public class JEDI implements JEDI_api{

	// jedi id
	private int jediID;
	
	// josyick number, 1 or 2
	public static enum JoystickNumbers{JEDI_ONE, JEDI_TWO};
	private JoystickNumbers joystickNumber;
	
	// server id 
	private int serverID = 11;
	
	// port used to the jedi connection
	private String portName;
	
	// jedi tick
	private int jediTick = 0;
	
	// fixed length queue where the data received will be left
	private FixedQueue<Axis> fixedQueue;
	
	// handler for the data available at the USART
	private CommHandler handler;
	
	// buttons state
	private Buttons buttons;
	public static enum buttonState{UP, DOWN};
	public static enum buttonName{JEDI_A, JEDI_B};
	private static buttonState currentStateA = buttonState.UP;
	private static buttonState currentStateB = buttonState.UP;
	
	// USART connection
	private USART usart;
	
	// connection state
	private enum ConnectionState {NOT_CONNECTED, CONNECTED};
	private ConnectionState connectionState;
	
	/**
	 * JEDI controller object
	 * @param jediID ID of the JEDI controller to connect to
	 * @param joystickNumber Number of the joystick to handle, either JEDI_ONE or JEDI_TWO
	 * @param serverID ID of the server that communicates to the JEDI
	 * @param portName Name of the port where the JEDI is connected
	 * @param buttonEventListener Handler of the button events
	 * @param connectionEventLister Handler of the connection events
	 * @throws NoSuchPortException 
	 * @throws PortInUseException
	 * @throws IOException
	 * @throws TooManyListenersException
	 * @throws UnsupportedCommOperationException
	 */
	public JEDI(int jediID, JoystickNumbers joystickNumber, int serverID, String portName, ButtonListenerInterface buttonEventListener, ConnectionListenerInterface connectionEventLister) throws NoSuchPortException, PortInUseException, IOException, TooManyListenersException, UnsupportedCommOperationException{
		
		// id of the jedi to communicate to
		this.jediID = jediID;
		
		// joystick Number, either 1 or 2
		this.joystickNumber = joystickNumber;
		
		// set the port name
		this.portName = portName;
		
		// initialize the fixed queue that will accumulate the acceleration values
		fixedQueue = new FixedQueue<Axis>(10 * 2);
		
		// initialize the buttons of the jedi
		buttons = new Buttons(false, false);
		
		// initialize the handler
		handler = new SerialCommHandler(this, buttonEventListener, connectionEventLister);
	
		// initialize the connection to the server
		this.usart = new USART(this.portName, fixedQueue, handler, buttons, buttonEventListener);
		
		// set the initial connection state as false
		this.connectionState = ConnectionState.NOT_CONNECTED;
	}
	
	/**
	 * Starts the communication with the JEDI
	 */
	public void start(){
		usart.startTransmission();
	}
	
	/**
	 * Sets the state of the selected JEDI button
	 * @param name Name of the selected button (A or B)
	 * @param state Set the state of the button (UP or DOWN)
	 */
	public synchronized void setButtonState(buttonName name, buttonState state){
		if(name == buttonName.JEDI_A)
			currentStateA = state;
		else
			currentStateB = state;
	}
	
	/**
	 * Get the state of the selected JEDI button
	 * @param name Name of the selected button (A or B)
	 * @return The state of the selected button (UP or DOWN)
	 */
	public synchronized buttonState getButtonState(buttonName name){
		if(name == buttonName.JEDI_A)
			return currentStateA;
		else
			return currentStateB;
	}
	
	/**
	 * Gets the JEDI ID
	 * @return An integer with the JEDI ID
	 */
	@Override
	public int getJediID() {
		return jediID;
	}
	
	/**
	 * Sets the Joystick number
	 * @param enum with the joystick desired (either ONE or TWO) 
	 */
	public void setJoystickNumber(JoystickNumbers joystickNumber) {
		this.joystickNumber = joystickNumber;
	}
	
	/**
	 * Gets the Joystick number
	 * @return an enum with the selected joystick
	 */
	public JoystickNumbers getJoystickNumber() {
		return joystickNumber;
	}
	
	/**
	 * Gets the Joystick number
	 * @return an enum with the selected joystick
	 */
	public int getJoystickNumberAsInt() {
		if(joystickNumber == JoystickNumbers.JEDI_ONE)
			return 1;
		else
			return 2;
	}
	
	/**
	 * Sets the JEDI ID (BE VERY CAREFULL! It may leed to a complete lost of communications with the JEDI)
	 * @param jediID New JEDI ID 
	 */
	public void setJediID(int jediID) {
		this.jediID = jediID;
	}
	
	/**
	 * Gets the Server ID that is communicating with the JEDI
	 * @return An integer with the Server ID
	 */
	public int getServerID() {
		return serverID;
	}

	/**
	 * Gets the tick counter of the JEDI
	 * @return An integer with the current tick
	 */
	public int getJediTick() {
		return jediTick;
	}

	/**
	 * Sets the tick of the JEDI
	 * @param tick Integer with the new Tick
	 */
	public void setJediTick(int tick) {
		this.jediTick = tick;
	}
	
	/**
	 * Decrements in one (1) the tick number of the JEDI
	 */
	public void decJediTick(){
		this.jediTick--;
	}
	
	/**
	 * Sets the connection state of the JEDI
	 * @param state Boolean with the state of the JEDI connection
	 */
	public void setConnectionState(boolean state){
		if(state)
			this.connectionState = ConnectionState.CONNECTED;
		else
			this.connectionState = ConnectionState.NOT_CONNECTED;
	}
	
	/**
	 * Returns the state of the JEDI connection
	 * @return A boolean with the state of the connection
	 */
	public boolean isConnected(){
		return this.connectionState == ConnectionState.CONNECTED;
	}
	
	@Override
	public void calibrate(final int milliseconds, final CalibrationInterface calibrationCallback) {
		Thread calibrationThrd = new Thread() {
			@Override
			public void run() {
				
				// run the callback function onStart
				calibrationCallback.onStart();
				
				final List<Axis> samples = new ArrayList<Axis>();
				
				// get the first acceleration
				Axis aux = getAcceleration();
				
				// if none could be get, abort
				if(aux == null)
					return;
				
				// add it to the sample accelerations to calibrate
				samples.add(aux);
//				System.out.println("CALIBRANDOOOOOOOOOOOOOOOOOOOOOOOOOOO: (" + samples.size() + ")");
//				System.out.println(aux);
								
				TimerTask task = new TimerTask(){

					@Override
					public void run() {
						// get the last acceleration
						Axis aux = getAcceleration();
						
						// if it was different to the last inserted, add it
						if(samples.get(samples.size()- 1).getTime() != aux.getTime()){
							samples.add(aux);
//							System.out.println(aux);
						}
					}
					
				};
				
				Timer timer = new Timer();
				timer.scheduleAtFixedRate(task, 35, 35);
				
				try {
					Thread.sleep(milliseconds);
				} catch (InterruptedException e) {
					//TODO nothing should happen...
				}
				
				timer.cancel();
				
//				System.out.println("quedaron " +  samples.size());
				
				double newX = 0, newY = 0, newZ = 0;
				
				for(Axis a : samples){
					newX += a.getOriginalX();
					newY += a.getOriginalY();
					newZ += a.getOriginalZ();
				}
				
				System.out.println("x=" + newX / samples.size()+ ",y=" 
						+ newY / samples.size() + ",z=" + newZ / samples.size());
				
				Axis.setZero(newX / samples.size(), newY / samples.size() + 22, newZ / samples.size());
				
				// run the callback function onEnding
				calibrationCallback.onEnding();
			}
		};
		calibrationThrd.start();
	}

	@Override
	public Axis getAcceleration() {
		
		if(fixedQueue.size() == 0)
			return null;
		
		return fixedQueue.getLast();
	}
	
	@Override
	public Axis getVelocity() {
		
		if(fixedQueue.getArray() == null || fixedQueue.getArray().size() == 0)
			return null;
		
		LinkedList<Axis> accelerations = new LinkedList<Axis>(fixedQueue.getArray());
		
		Axis a;
		double x = 0, y = 0, z = 0;
		long prevTime = accelerations.get(0).getTime();
		
		for(int i = accelerations.size() / 2; i < accelerations.size(); i++){
			a = accelerations.get(i);
			x += a.getX()* (a.getTime() - prevTime);
			y += a.getY()* (a.getTime() - prevTime);
			z += a.getZ()* (a.getTime() - prevTime);
			prevTime = a.getTime();
		}
		
		return new Axis(x, y, z, TypeOfData.VELOCITY, prevTime);
	}
	
	private Axis getVelocity(LinkedList<Axis> accelerations, int from, int to) {
		
		if(accelerations == null || accelerations.size() == 0)
			return null;
		
		Axis a;
		double x = 0, y = 0, z = 0;
		long prevTime = accelerations.get(0).getTime();
		
		for(int i = from / 2; i < to; i++){
			a = accelerations.get(i);
			x += a.getX()* (a.getTime() - prevTime);
			y += a.getY()* (a.getTime() - prevTime);
			z += a.getZ()* (a.getTime() - prevTime);
			prevTime = a.getTime();
		}
		
		return new Axis(x, y, z, TypeOfData.VELOCITY, prevTime);
	}
	
	@Override
	public Axis getPosition() {
		
		if(fixedQueue.getArray() == null || fixedQueue.getArray().size() == 0)
			return null;
		
		LinkedList<Axis> accelerations = new LinkedList<Axis>(fixedQueue.getArray());
		LinkedList<Axis> velocities = new LinkedList<Axis>();
		
		for(int i = 0; i < accelerations.size() / 2; i++){
			velocities.add(getVelocity(accelerations, i, i + accelerations.size() / 2));
		}
		
		if(velocities.size() == 0)
			return null;
		
		Axis a;
		double x = 0, y = 0, z = 0;
		long prevTime = velocities.get(0).getTime();
		
		for(int i = 0; i < velocities.size(); i++){
			a = velocities.get(i);
			x += a.getX()* (a.getTime() - prevTime);
			y += a.getY()* (a.getTime() - prevTime);
			z += a.getZ()* (a.getTime() - prevTime);
			prevTime = a.getTime();
		}
		
		return new Axis(x, y, z, TypeOfData.POSITION, prevTime);
	}

	@Override
	public Axis getDirection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Buttons getPressedButtons(){
		return new Buttons(buttons.isPressedA(), buttons.isPressedB());
	}

	@Override
	public Axis getRotation() {
		// TODO Auto-generated method stub
		return null;
	}
}
