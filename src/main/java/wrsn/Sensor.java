package wrsn;

import io.jbotsim.core.*;

public class Sensor extends Node {
	Node parent = null;
	int battery = 255;

	@Override
	public void onMessage(Message message) {
		// "INIT" flag : construction of the spanning tree
		// "SENSING" flag : transmission of the sensed values
		// You can use other flags for your algorithms
		if (isAlive()) {
			String flag = message.getFlag();
			if (flag.equals("INIT")) {
				// if not yet in the tree
				if (parent == null) {
					// enter the tree
					parent = message.getSender();
					getCommonLinkWith(parent).setWidth(5);
					// propagate further
					sendAll(message);
				}
			} else if (flag.equals("SENSING")) {
				// retransmit up the tree
				if (parent != null)
					send(parent, message);
			}
		}
		
	}

	@Override
	public void send(Node destination, Message message) {
		if (isAlive()) { // the Sensor is alive
			super.send(destination, message);
			battery--;
			updateColor();

			if (!isAlive()) {
				BaseStation.reset(getTopology());
			}
		}
	}

	@Override
	public void onClock() {
		if (parent != null) { // if already in the tree
			if (Math.random() < 0.02) { // from time to time...
				Message message = new Message(battery, "SENSING");
				if (battery < 200)
					message = new Message(getLocation(), "SENSING");
				send(parent, message); // send it to parent
			}
		}
	}

	protected void updateColor() {
		setColor(battery == 0 ? Color.red : new Color(255 - battery, 255 - battery, 255));
	}

	public boolean isAlive() {
		return battery > 0;
	}
}