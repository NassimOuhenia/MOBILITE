package wrsn;

import io.jbotsim.core.Message;
import io.jbotsim.core.Node;
import io.jbotsim.core.Point;
import io.jbotsim.ui.icons.Icons;

public class BaseStation extends Node {
	@Override
	public void onStart() {
		setIcon(Icons.STATION);
		setIconSize(16);

		// Initiates tree construction with an empty message
		sendAll(new Message(null, "INIT"));
		sendAll(new Message(this.getLocation(),"LOC"));
	}

	@Override
	public void onMessage(Message message) {
		super.onMessage(message);
		if(message.getFlag().equals("ALERT")) {
			//System.out.println("Base station : Alert");
			if(message.getContent() instanceof Point) {
				sendAll(new Message(message.getContent(), "URGENCE"));
			}
		}else if(message.getFlag().equals("SENSING")) {
			if(message.getContent() instanceof Point) {
				sendAll(new Message(message.getContent(), "DEST"));
			}
		}
	}
	
	
}