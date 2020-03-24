package wrsn;

import io.jbotsim.core.Color;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

public class Sensor extends PerimeterTreeNode {
	int battery = 255;

	boolean getMinMaxLocations = false;

	@Override
	public void onMessage(Message message) {

		super.onMessage(message);

		if (message.getFlag().equals("SENSING")) {
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
				// double sensedValue = Math.random(); // sense a value
				if (battery < 100) {
					// getCommonLinkWith(parent).setColor(Color.blue);
					send(parent, new Message(getLocation(), "ALERT"));
					// System.out.println("Sensor : ALERT");
					return;
				}
				send(parent, new Message(battery, "SENSING")); // send it to parent
			}
		}
	}

	protected void updateColor() {
		setColor(battery == 0 ? Color.red : new Color(255 - battery, 255 - battery, 255));
	}
}