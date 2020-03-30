
import java.util.ArrayList;

import io.jbotsim.core.Message;
import io.jbotsim.core.Node;
import io.jbotsim.ui.icons.Icons;

public class BaseStation extends Node {
	
	private ArrayList<Node> dead = new ArrayList<>();
	private Node parent = this;
	
	@Override
	public void onStart() {
		setIcon(Icons.STATION);
		setIconSize(16);

		// Initiates tree construction with an empty message
		//sendAll(new Message(null, "INIT"));
	}

	@Override
	public void onClock() {
		super.onClock();
		sendAll(new Message(null, "INIT"));
	}

	/*@Override
	public void onMessage(Message message) {
		super.onMessage(message);
		if(message.getFlag().equals("DEAD")) {
			System.out.println("DEAD");
			//sendAll(new Message(null,"NULL"));
			sendAll(new Message(null,"INIT"));
			dead.add((Node) message.getContent());
		}else if(message.getFlag().equals("SENSING")) {
			if(message.getContent() instanceof Node) {
				if(dead.contains((Node) message.getContent())) {
					//sendAll(new Message(null,"NULL"));
					sendAll(new Message(null,"INIT"));
					dead.remove((Node) message.getContent());
				}
			}
		}
	}*/
	
	
	
}