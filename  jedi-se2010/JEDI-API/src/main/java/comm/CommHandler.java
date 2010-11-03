package comm;

import java.io.InputStream;
import java.io.OutputStream;

import data.Axis;
import data.FixedQueue;

public interface CommHandler extends Runnable{
	
	/**
	 * Communication handler that interfaces with the method of communication to the JEDI, such as UART, Bluetooth, etc 
	 * @param inputStream InputStream to receive the data from the JEDI
	 * @param outputStream OutputStream to send the data to the JEDI
	 * @param fixedQueue Queue where the received INFORM packets will be left
	 * @throws Exception
	 */
	void dataAvailableHandler(InputStream inputStream, OutputStream outputStream, FixedQueue<Axis> fixedQueue) throws Exception;
	
	/**
	 * Sets the InputStream of the communication with the JEDI
	 * @param inputStream InputStream to be setted
	 */
	public void setInputStream(InputStream inputStream);
	
	/**
	 * Sets the OutputStream of the communication with the JEDI
	 * @param outputStream OutputStream to be setted
	 */
	public void setOutputStream(OutputStream outputStream);
}
