package comm.usart;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

import api.ButtonListenerInterface;
import api.impl.FakeJEDI;

import comm.CommHandler;

import data.Axis;
import data.Buttons;
import data.FixedQueue;

public class USART implements SerialPortEventListener{
	
	// port identifier
	CommPortIdentifier portID;
	
	// connection to the port
	SerialPort serialPort;
	
	// receive data
	InputStream inputStream;
	
	// send data
	static OutputStream outputStream = null;
	
	// thread in charge of reading from the serial port
	Thread readThread;
	
	// thread in charge of the listening packets 
	Thread listeningThread;
	
	// handler in charge of receiving and procesing the data when available
	CommHandler handler;
	
	// fixed length queue where the received data will be left
	FixedQueue<Axis> fixedQueue;
	
	Buttons buttons;
	
	ButtonListenerInterface myEventListener;
    
    public USART(String portName, FixedQueue<Axis> fixedQueue, CommHandler handler, Buttons buttons, ButtonListenerInterface myEventListener) throws NoSuchPortException, PortInUseException, IOException, TooManyListenersException, UnsupportedCommOperationException{
    	
    	this.fixedQueue = fixedQueue;
    	this.handler = handler;
    	this.buttons = buttons;
    	this.myEventListener = myEventListener;
    	
    	// get the port to listen to
    	try {
			portID = CommPortIdentifier.getPortIdentifier(portName);
		}
    	catch (NoClassDefFoundError n) {
			
			// force exception
			throw new NoSuchPortException();
		}
		catch (NoSuchPortException e) {
			e.printStackTrace();
			throw new NoSuchPortException();
		}

    	
    	// initalize serial port
        try {
           serialPort = (SerialPort) portID.open("SimpleReadApp", 2000);
        } catch (PortInUseException e) {
        	e.printStackTrace();
        	throw new PortInUseException();
        }
     
        // get the input and output streams
        try {
           inputStream = serialPort.getInputStream();
           this.handler.setInputStream(inputStream);
           outputStream = serialPort.getOutputStream();
           this.handler.setOutputStream(outputStream);
        } catch (IOException e) {
        	e.printStackTrace();
        	throw new IOException();
        }
     
        try {
           serialPort.addEventListener(this);
        } catch (TooManyListenersException e) {
        	e.printStackTrace();
        	throw new TooManyListenersException();
        }
     
        try {
           // set port parameters
           serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, 
                       SerialPort.STOPBITS_2, 
                       SerialPort.PARITY_NONE);
        } catch (UnsupportedCommOperationException e) {
        	e.printStackTrace();
        	throw new UnsupportedCommOperationException();
        }
        
        listeningThread = new Thread(handler);
        listeningThread.start();
    }
    
    public void startTransmission(){
    	// activate the DATA_AVAILABLE notifier
        serialPort.notifyOnDataAvailable(true);
        
        // activate the OUTPUT_BUFFER_EMPTY notifier
        serialPort.notifyOnOutputEmpty(true);	
    }
    
    public void initWriteToPort() {
        // initWriteToPort() assumes that the port has already been opened and initialized
        try {
           // get the outputstream
           outputStream = serialPort.getOutputStream();
        } catch (IOException e) {
        	e.printStackTrace();
        }

        try {
           // activate the OUTPUT_BUFFER_EMPTY notifier
           serialPort.notifyOnOutputEmpty(true);
        } catch (Exception e) {
//           System.out.println("Error setting event notification");
//           System.out.println(e.toString());
//           System.exit(-1);
        	e.printStackTrace();
        }
        
     }

	@Override
	public void serialEvent(SerialPortEvent event) {
		switch (event.getEventType()) {
	   		case SerialPortEvent.BI:
			case SerialPortEvent.OE:
			case SerialPortEvent.FE:
			case SerialPortEvent.PE:
			case SerialPortEvent.CD:
			case SerialPortEvent.CTS:
			case SerialPortEvent.DSR:
			case SerialPortEvent.RI:
			case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
				break;
			case SerialPortEvent.DATA_AVAILABLE:
				try {
					//use the handler to process the received data
					handler.dataAvailableHandler(inputStream, outputStream, fixedQueue);
		            
				} catch (Exception e) {
					e.printStackTrace();
				}
		   
				break;
		}
	}
	
	public static OutputStream getOutputStream() {
		return outputStream;
	}
}