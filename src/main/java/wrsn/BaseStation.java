package wrsn;

import java.util.ArrayList;

import io.jbotsim.core.Message;
import io.jbotsim.core.Point;
import io.jbotsim.ui.icons.Icons;

public class BaseStation extends PerimeterTreeNode {

	ArrayList<Point> urgences = new ArrayList<>();

	@Override
	public void onStart() {
		setIcon(Icons.STATION);
		setIconSize(16);

		parent = this;
		// Initiates tree construction with an empty message
		sendAll(new Message(this, "INIT"));
	}

	public void sendUrgences(String name) {

		if (urgences.isEmpty()) {
			System.out.println("BaseStation> No emergencies!!");
			return;
		}
			
		String flag = "URGENCES" + name;
		if (5 < urgences.size()) {
			int size_to_send = urgences.size() / 2;

			ArrayList<Point> toSend = new ArrayList<>();

			for (int i = 0; i < size_to_send; i++) {
				toSend.add(urgences.get(0));
				urgences.remove(0);
			}
			
			sendAll(new Message(toSend, flag));
			System.out.println("BaseStation> Yes A Lot !");
		} else {
			sendAll(new Message(urgences, flag));
			System.out.println("BaseStation> Yes some !");
			urgences.clear();
		}
	}

	@Override
	public void onMessage(Message message) {
		super.onMessage(message);
		if (message.getFlag().equals("ALERT")) {
			// System.out.println("Base station : Alert");
			Point dest = (Point) message.getContent();
			if (!urgences.contains(dest))
				urgences.add(dest);
		}
		if (message.getFlag().equals("BOT1") || message.getFlag().equals("BOT2")) {
			sendUrgences(message.getFlag());
		}
	}

}