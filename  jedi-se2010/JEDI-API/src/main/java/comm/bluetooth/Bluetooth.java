package comm.bluetooth;

import java.util.Vector;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

public class Bluetooth implements DiscoveryListener{

	/** 
	 * The DiscoveryAgent for the local Bluetooth device. 
	 */
	private DiscoveryAgent agent;
	
	/** 
	 * The max number of service searches that can occur at any one time. 
	 */
	private int maxServiceSearches = 0;
	
	/** 
	 * The number of service searches that are presently in progress. 
	 */
	private int serviceSearchCount;
	
	/**
	 * Keeps track of the transaction IDs returned from searchServices. 
	 */
	private int transactionID[];
	
	/** 
	 * The service record to a printer service that can print the message
	 * provided at the command line.
	 */
	private ServiceRecord record;
	
	/** 
	 * Keeps track of the devices found during an inquiry. 
	 */
	private Vector<Object> deviceList;
	
	@Override
	public void deviceDiscovered(RemoteDevice arg0, DeviceClass arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inquiryCompleted(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void serviceSearchCompleted(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void servicesDiscovered(int arg0, ServiceRecord[] arg1) {
		// TODO Auto-generated method stub
		
	}
	
	/** 
	 * Creates a PrintClient object and prepares the object for device 
	 * discovery and service searching. 
	 * 
	 * @exception BluetoothStateException if the Bluetooth system could not be 
	 * initialized 
	 */
	public Bluetooth() throws BluetoothStateException {
		/*
		 * Retrieve the local Bluetooth device object. 
		 */
		LocalDevice local = LocalDevice.getLocalDevice();
		
		/* 
		 * Retrieve the DiscoveryAgent object that allows us to perform device 
		 * and service discovery. 
		 */
		agent = local.getDiscoveryAgent();
		
		/* 
		 * Retrieve the max number of concurrent service searches that can 
		 * exist at any one time. 
		 */
		try { 
			maxServiceSearches = Integer.parseInt(
					LocalDevice.getProperty("bluetooth.sd.trans.max")); 
		} catch (NumberFormatException e) {
				System.out.println("General Application Error"); 
				System.out.println("\tNumberFormatException: " + e.getMessage());
		} 
			
		transactionID = new int[maxServiceSearches];
		
		// Initialize the transaction list 
		for (int i = 0; i < maxServiceSearches; i++) {
			transactionID[i] = -1;
		}

		record = null; 
		deviceList = new Vector<Object>();
	
	}

	public DiscoveryAgent getAgent() {
		return agent;
	}

	public void setAgent(DiscoveryAgent agent) {
		this.agent = agent;
	}

	public int getMaxServiceSearches() {
		return maxServiceSearches;
	}

	public void setMaxServiceSearches(int maxServiceSearches) {
		this.maxServiceSearches = maxServiceSearches;
	}

	public int getServiceSearchCount() {
		return serviceSearchCount;
	}

	public void setServiceSearchCount(int serviceSearchCount) {
		this.serviceSearchCount = serviceSearchCount;
	}

	public int[] getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(int[] transactionID) {
		this.transactionID = transactionID;
	}

	public ServiceRecord getRecord() {
		return record;
	}

	public void setRecord(ServiceRecord record) {
		this.record = record;
	}

	public Vector<Object> getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(Vector<Object> deviceList) {
		this.deviceList = deviceList;
	}
}