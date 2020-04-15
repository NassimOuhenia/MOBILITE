package wrsn;

import java.util.ArrayList;

import io.jbotsim.core.*;
import io.jbotsim.ui.icons.Icons;

public class BaseStation extends Node {

	ArrayList<Point> emergencies = new ArrayList<>();//destinations where to go first
	@Override
	public void onStart() {
		setIcon(Icons.STATION);
		setIconSize(16);

		// Initiates tree construction with an empty message
		sendAll(new Message(null, "INIT"));
	}

	@Override
	public void onMessage(Message message) {

		String flag = message.getFlag(); //flag of the message
		
		if (flag.equals("ROBOT"))
			send(message.getSender(), new Message(null, "BASE"));
		else if (flag.equals("ASK"))
			sendEmergencies(message.getSender()); // if a robot ask for emergencies
		else if (flag.equals("SENSING")) {
			if (message.getContent() instanceof Point) { // content is not a sensing value
				Point p = ((Point) message.getContent());// emergency destination
				if (!emergencies.contains((p))) {
					emergencies.add(p);
				}		
			}
		}
	}
	
	@Override
	public void onClock() {
		super.onClock();
		sendAll(new Message(this, "INIT"));
	}
	
	/**
	 * send destinations to the Node robot
	 * @param robot
	 */
	public void sendEmergencies(Node robot) {

		if (!emergencies.isEmpty()) {
			
			if (5 <= emergencies.size()) { // if there's too much emergencies 
				int size_to_send = emergencies.size() / 2;
				ArrayList<Point> toSend = new ArrayList<>();
				for (int i = 0; i < size_to_send; i++) {
					toSend.add(emergencies.get(0));
					emergencies.remove(0);
				}
				send(robot, new Message(toSend, "EMERGENCIES"));
			} else {
				send(robot, new Message(emergencies, "EMERGENCIES")); // send all destinations to one robot
				emergencies.clear();
			}
		}
	}
	
	/**
	 * rebuild the spanning tree
	 * @param tp (getTopology())
	 */
	public static void reset(Topology tp) {
		
		for (Node node : tp.getNodes()) {
			if (node instanceof Sensor) {
				((Sensor) node).parent = null;
			}
		}
		for (Link link : tp.getLinks())
			link.setWidth(1);
	}
}