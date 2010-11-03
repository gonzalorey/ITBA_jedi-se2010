package comm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import api.ButtonListenerInterface;
import api.ConnectionListenerInterface;
import api.impl.JEDI;
import api.impl.JEDI.buttonName;
import api.impl.JEDI.buttonState;
import api.JEDI_api;
import data.Axis;
import data.Buttons;
import data.FixedQueue;
import data.packets.DiscoverReqPacket;
import data.packets.DiscoverRespPacket;
import data.packets.DiscoverRespPacket.acc_method;
import data.packets.InformPacket;
import data.packets.ListeningPacket;
import event.ButtonEvent;
import event.ConnectionEvent;

public class SerialCommHandler implements CommHandler {
	
	// instance of the jedi to serial control
	private JEDI jedi;
	
	private InputStream inputStream = null;
	private OutputStream outputStream = null;
	private FixedQueue<Axis> fixedQueue;
	
	private File file;
	FileOutputStream fos;
	
	public SerialCommHandler(JEDI_api jedi, ButtonListenerInterface buttonEventListener, 
			ConnectionListenerInterface connectionEventListener) throws FileNotFoundException{
		
		// initialize the jedi
		this.jedi = (JEDI)jedi;
		
		this.addButtonEventListener(buttonEventListener);
		this.addConnectionEventListener(connectionEventListener);
		
		this.file = new File("test.bin");
		this.fos = new FileOutputStream(this.file);
	}
	
	public void setInputStream(InputStream inputStream){
		this.inputStream = inputStream;
	}
	
	public void setOutputStream(OutputStream outputStream){
		this.outputStream = outputStream;
	}
	
	@Override
	public void dataAvailableHandler(InputStream inputStream, OutputStream outputStream, FixedQueue<Axis> fixedQueue) throws Exception {
		byte[] readTrash = new byte[1];
		int trash;
		int zeroesCount = 0;
		
		this.inputStream = inputStream;
		this.outputStream = outputStream;
		this.fixedQueue = fixedQueue;
	 
		// read until the start of a packet
		while(true){
			while(inputStream.available() < 1);
		 
			if(inputStream.read(readTrash) != 1)
				throw new IOException("Wrong data received");
		 
			trash = new Byte(readTrash[0]).intValue();
		 
			if(trash == 0)
				zeroesCount++;
			else
				zeroesCount = 0;
		 
			if(zeroesCount == 4)
				break;
		}
	 
		// wait for the data to be available
		while(inputStream.available() < 1);

		// read the length of the data to be received
		byte[] readLength = new byte[1];
		if(inputStream.read(readLength) != 1)
			throw new IOException("Wrong data received");
	
		// convert to int
		int length = new Byte(readLength[0]).intValue() - 1;
	
//		System.out.println("Receiving a package of length " + length);
	
		// wait for the data to be available
		while(this.inputStream.available() < length && length > 0);

        // read length amount of data from the serial
        byte[] readBuffer = new byte[length];
        if(inputStream.read(readBuffer) != length)
        	throw new IOException("Wrong data received");
     
        fos.write(readBuffer);
        
        switch(readBuffer[2]){
        	case 11:	// DISCOVER_REQUEST
        		doDiscoverReq(readBuffer);
        		break;
        	case 12:	// DISCOVER_RESPONSE
        		doDiscoverResp(readBuffer);
        		break;
        	case 20:	// INFORM 
        		doInform(readBuffer);
        		break;
        	case 30:	// LISTENING
        		doListening(readBuffer);	
        	default:
        		System.out.println("Wrong type of package");
        }
        
//        System.out.println("");
      
	}
	
	private void doListening(byte[] readBuffer) {
		ListeningPacket pck = ListeningPacket.parseInform(readBuffer);
		
		if(pck == null){
			System.out.println("Wrong type of package");
			return;
		}
		
	}

	private void doDiscoverReq(byte[] readBuffer) {
		DiscoverReqPacket reqPck = DiscoverReqPacket.parseInform(readBuffer);
		
		if(reqPck == null){
			System.out.println("Wrong type of package");
			return;
		}
		
		if(reqPck.getOrig() == jedi.getJediID()){
			jedi.setJediTick(130);
			jedi.setConnectionState(true);
			fireConnectionStartedEvent();
		}
		
		DiscoverRespPacket respPck = new DiscoverRespPacket(jedi.getServerID(), reqPck.getOrig(), 2, acc_method.ACC_METHOD_MEDIAN,(byte) 0xFF);
		
		try {
			outputStream.write(respPck.serialize());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void doDiscoverResp(byte[] readBuffer) {
		DiscoverRespPacket pck = DiscoverRespPacket.parseInform(readBuffer, jedi.getServerID());
		
		if(pck == null){
			System.out.println("Wrong type of package");
			return;
		}
		
	}

	private void doInform(byte[] readBuffer) {
		InformPacket pck = InformPacket.parseInform(readBuffer, jedi.getServerID());
		
		if(pck == null){
			System.out.println("Wrong type of package");
			return;
		}
		
		fixedQueue.add(pck.getAxis());
		
		handleButtons(pck.getButtons());
		
		// set the jedi tick
		jedi.setJediTick(130);
	}
	
	@Override
	public void run() {
		int count = 0;
		while(true)
		{
			// if the jedi is connected, dec his tick
			if(jedi.isConnected())
				jedi.decJediTick();
			
			// if the tick reached zero, set the jedi as disconnected
			if(jedi.getJediTick() == 0){
				jedi.setConnectionState(false);
				fireConnectionEndedEvent();
				System.out.println("JEDI" + jedi.getJediID() + " disconnected!");
			}
				
			if((count % 40) == 0){
				if(jedi.isConnected()){
					// ---------------------------------------------------------------------
		     		ListeningPacket respPck = new ListeningPacket(jedi.getServerID(), jedi.getJediID(), (byte) 0xFF);
		     		try {
		     			outputStream.write(respPck.serialize());
		     		} catch (IOException e) {
		     			e.printStackTrace();
		     		}
		     		// ---------------------------------------------------------------------
				}
			}
			
			try {
				Thread.sleep(1);
				count++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private synchronized void handleButtons(Buttons buttons){
		
		if(buttons.isPressedA() && jedi.getButtonState(buttonName.JEDI_A) == buttonState.UP){
			jedi.setButtonState(buttonName.JEDI_A, buttonState.DOWN);
			this.firePressedEvent(ButtonEvent.JEDI_A);
		}
		
		if(!buttons.isPressedA() && jedi.getButtonState(buttonName.JEDI_A) == buttonState.DOWN){
			jedi.setButtonState(buttonName.JEDI_A, buttonState.UP);
			this.fireReleasedEvent(ButtonEvent.JEDI_A);
		}
		
		if(buttons.isPressedB() && jedi.getButtonState(buttonName.JEDI_B) == buttonState.UP){
			jedi.setButtonState(buttonName.JEDI_B, buttonState.DOWN);
			this.firePressedEvent(ButtonEvent.JEDI_B);
		}
		
		if(!buttons.isPressedB() && jedi.getButtonState(buttonName.JEDI_B) == buttonState.DOWN){
			jedi.setButtonState(buttonName.JEDI_B, buttonState.UP);
			this.fireReleasedEvent(ButtonEvent.JEDI_B);
		}
	}
	
	//Button Listener
	private List<ButtonListenerInterface> buttonListeners = new ArrayList<ButtonListenerInterface>();
	
	public synchronized void addButtonEventListener(ButtonListenerInterface listener)  {
		buttonListeners.add(listener);
	}
	public synchronized void removeButtonEventListener(ButtonListenerInterface listener)   {
		buttonListeners.remove(listener);
	}
	
	private synchronized void firePressedEvent(int actionButton) {
		ButtonEvent event = new ButtonEvent(this, actionButton);
		Iterator<ButtonListenerInterface> i = buttonListeners.iterator();
		while(i.hasNext())  {
			i.next().buttonPressed(event);
		}
	}
	
	private synchronized void fireReleasedEvent(int actionButton) {
		ButtonEvent event = new ButtonEvent(this, actionButton);
		Iterator<ButtonListenerInterface> i = buttonListeners.iterator();
		while(i.hasNext())  {
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
