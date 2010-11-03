package client.test;

import event.ConnectionEvent;
import api.ConnectionListenerInterface;

public class ConnectionEventListener implements ConnectionListenerInterface {

	@Override
	public void connectionStarted(ConnectionEvent event) {
		System.out.println("Disconnected JEDI");
	}

	@Override
	public void connectionEnded(ConnectionEvent event) {
		System.out.println("Connected JEDI");
	}
}
