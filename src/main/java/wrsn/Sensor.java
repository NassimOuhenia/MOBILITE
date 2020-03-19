package wrsn;

import io.jbotsim.core.Color;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

public class Sensor extends Node {
	Node parent = null;
	int battery = 255;

	@Override
	public void onMessage(Message message) {
		// "INIT" flag : construction of the spanning tree
		// "SENSING" flag : transmission of the sensed values
		// You can use other flags for your algorithms
		if (message.getFlag().equals("INIT")) {
			// if not yet in the tree
			if (parent == null) {
				// enter the tree
				parent = message.getSender();
				getCommonLinkWith(parent).setWidth(4);
				// propagate further
				sendAll(message);
			}
		} else if (message.getFlag().equals("SENSING") || message.getFlag().equals("ALERT")) {
			// retransmit up the tree
			send(parent, message);
		}
	}

	@Override
	public void send(Node destination, Message message) {
		if (battery > 0) {
			super.send(destination, message);
			battery--;
			updateColor();
		}
	}

	@Override
	public void onClock() {
		if (parent != null) { // if already in the tree
			if (Math.random() < 0.02) { // from time to time...
				//double sensedValue = Math.random(); // sense a value	
				if(battery < 200) {
					//getCommonLinkWith(parent).setColor(Color.blue);
					send(parent, new Message(this.getLocation(),"ALERT"));
					//System.out.println("Sensor : ALERT");
				}
				send(parent, new Message(battery, "SENSING")); // send it to parent
			}
		}
	}

	protected void updateColor() {
		setColor(battery == 0 ? Color.red : new Color(255 - battery, 255 - battery, 255));
	}
}