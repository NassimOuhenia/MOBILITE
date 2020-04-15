package wrsn;

import java.util.ArrayList;

import io.jbotsim.core.Message;
import io.jbotsim.core.Node;
import io.jbotsim.core.Point;
import io.jbotsim.ui.icons.Icons;

public class Robot extends WaypointNode {

	Node base = null;
	ArrayList<Point> emergenies = new ArrayList<>();

	@Override
	public void onStart() {

		setSensingRange(30);
		setIcon(Icons.ROBOT);
		setIconSize(16);

		sendAll(new Message(getID(), "ROBOT"));
	}

	@Override
	public void onSensingIn(Node node) {
		if (node instanceof Sensor)
			((Sensor) node).battery = 255;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onMessage(Message message) {
		String flag = message.getFlag();
		if (flag.equals("BASE")) {
			base = message.getSender(); // Initiate the BaseStation
			addDestination(base.getX(), base.getY());
		}
		if (flag.equals("EMERGENCIES"))
			emergenies.addAll((ArrayList<Point>) message.getContent()); // add all destinations
	}

	@Override
	public void onArrival() {
		if (getLocation().equals(base.getLocation())) {
			send(base, new Message(null, "ASK"));
		}

		if (emergenies.isEmpty()) {
			addDestination(base.getX(), base.getY());
		} else {
			choose_destination();
		}

	}

	public void choose_destination() {
		int index_min_distance = 0;
		double min_distance = distA_To_B(getLocation(), emergenies.get(0));

		for (int i = 1; i < emergenies.size(); i++) {
			double newdist = distA_To_B(getLocation(), emergenies.get(i));
			if (newdist < min_distance) {
				min_distance = newdist;
				index_min_distance = i;
			}
		}
		destinations.add(emergenies.get(index_min_distance));
		emergenies.remove(index_min_distance);
	}

	public double distA_To_B(Point a, Point b) {
		return Math.sqrt((Math.pow(b.getX() - a.getX(), 2)) + (Math.pow(b.getY() - a.getY(), 2)));
	}

}