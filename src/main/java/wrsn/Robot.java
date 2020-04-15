package wrsn;

import java.util.ArrayList;

import io.jbotsim.core.*;
import io.jbotsim.ui.icons.Icons;

public class Robot extends WaypointNode {

	Node base = null;
	ArrayList<Point> emergencies = new ArrayList<>();// choose the closest neighbor in this list

	@Override
	public void onStart() {

		setSensingRange(30);
		setIcon(Icons.ROBOT);
		setIconSize(16);

		sendAll(new Message(null, "ROBOT"));
	}

	@Override
	public void onSensingIn(Node node) {
		if (node instanceof Sensor) {
			Sensor sensor = (Sensor) node;
			
			if (!sensor.isAlive()) {
				sensor.battery = 255;
				BaseStation.reset(getTopology());
				return;
			}
			sensor.battery = 255;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onMessage(Message message) {
		String flag = message.getFlag();
		if (flag.equals("BASE")) {
			base = message.getSender(); // Initiate the BaseStation and go there
			addDestination(base.getX(), base.getY());
		}
		if (flag.equals("EMERGENCIES"))
			emergencies.addAll((ArrayList<Point>) message.getContent()); // add all destinations
	}

	@Override
	public void onArrival() {
		if (getLocation().equals(base.getLocation())) {
			send(base, new Message(null, "ASK")); // ASK for emergencies
		}

		if (emergencies.isEmpty()) { // no emergencies go back to the base station
			addDestination(base.getX(), base.getY());
		} else {
			choose_destination(); // choose the closest neighbor
		}
	}

	/**
	 * go to the closest neighbor
	 */
	public void choose_destination() {
		int index_min_distance = 0;
		double min_distance = distA_To_B(getLocation(), emergencies.get(0));

		for (int i = 1; i < emergencies.size(); i++) {
			
			double newdist = distA_To_B(getLocation(), emergencies.get(i));// distance to Sensor #i
			
			if (newdist < min_distance) {
				min_distance = newdist;
				index_min_distance = i;
			}
		}
		destinations.add(emergencies.get(index_min_distance));
		emergencies.remove(index_min_distance);
	}

	/**
	 * 
	 * @param a Point
	 * @param b Point
	 * @return Euclidean distance between a b
	 */
	public double distA_To_B(Point a, Point b) {
		return Math.sqrt((Math.pow(b.getX() - a.getX(), 2)) + (Math.pow(b.getY() - a.getY(), 2)));
	}

}