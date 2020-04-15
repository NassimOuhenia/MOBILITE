package wrsn;

import java.util.ArrayList;

import io.jbotsim.core.Message;
import io.jbotsim.core.Node;
import io.jbotsim.core.Point;
import io.jbotsim.ui.icons.Icons;

public class BaseStation extends Node {

	ArrayList<Point> emergencies = new ArrayList<>();

	@Override
	public void onStart() {
		setIcon(Icons.STATION);
		setIconSize(16);

		// Initiates tree construction with an empty message
		sendAll(new Message(null, "INIT"));
	}

	@Override
	public void onMessage(Message message) {

		String flag = message.getFlag();
		if (flag.equals("ROBOT"))
			send(message.getSender(), new Message(null, "BASE"));
		else if (flag.equals("ASK"))
			sendEmergences(message.getSender());
		else if (flag.equals("SENSING")) {
			if (message.getContent() instanceof Point) {
				Point p = ((Point) message.getContent());// emergency destination
				if (!emergencies.contains((p))) {
					emergencies.add(p);
					System.out.println(emergencies.size());
				}		
			}
		}
	}
	
	public void sendEmergences(Node robot) {

		if (!emergencies.isEmpty()) {
			if (5 <= emergencies.size()) {
				int size_to_send = emergencies.size() / 2;
				ArrayList<Point> toSend = new ArrayList<>();
				for (int i = 0; i < size_to_send; i++) {
					toSend.add(emergencies.get(0));
					emergencies.remove(0);
				}
				send(robot, new Message(toSend, "EMERGENCIES"));
			} else {
				send(robot, new Message(emergencies, "EMERGENCIES"));
				emergencies.clear();
			}
		} 
	}
}