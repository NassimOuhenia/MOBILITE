package wrsn;

import java.util.ArrayList;

import io.jbotsim.core.Message;
import io.jbotsim.core.Node;
import io.jbotsim.core.Point;
import io.jbotsim.ui.icons.Icons;

public class Robot extends WaypointNode {

	private Point stationLocation = new Point(Math.random() * 600, Math.random() * 400);
	private String name;

	double inf_x = 0;
	double sup_x = 600;
	double inf_y = 0;
	double sup_y = 400;

	public Robot(String _name) {
		name = _name;
	}

	public void setExtrimities(double[] extrimities) {

		setInf_x(extrimities[0]);
		setSup_x(extrimities[1]);
		setInf_y(extrimities[2]);
		setSup_y(extrimities[3]);

		System.out.println("Start.......");
		onArrival();
	}

	@Override
	public void onStart() {
		setSensingRange(30);
		setIcon(Icons.ROBOT);
		setIconSize(16);
	}

	@Override
	public void onSensingIn(Node node) {
		if (node instanceof Sensor)
			((Sensor) node).battery = 255;
	}

	public void randomDest() {
		double range_x = sup_x - inf_x + 1;
		double range_y = sup_y - inf_y + 1;

		double x = (Math.random() * range_x) + inf_x;
		double y = (Math.random() * range_y) + inf_y;

		addDestination(x, y);
	}

	@Override
	public void onArrival() {

		if (getLocation().equals(stationLocation)) {
			System.out.println(name + " > Is there emergencies?");
			sendAll(new Message(null, name));
		}

		if (Math.random() < 0.2) {
			addDestination(stationLocation.getX(), stationLocation.getY());
		}

		randomDest();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onMessage(Message message) {
		if (message.getFlag().equals(name)) {
			double[] content = (double[]) message.getContent();
			setExtrimities(content);
			setStationLocation(new Point(content[4], content[5]));
		} else if (message.getFlag().equals("URGENCES" + name)) {
			urgences.addAll((ArrayList<Point>) message.getContent());
		}
	}

	// setters
	public void setStationLocation(Point stationLocation) {
		this.stationLocation = stationLocation;
	}

	public void setInf_x(double inf_x) {
		this.inf_x = inf_x;
	}

	public void setSup_x(double sup_x) {
		this.sup_x = sup_x;
	}

	public void setInf_y(double inf_y) {
		this.inf_y = inf_y;
	}

	public void setSup_y(double sup_y) {
		this.sup_y = sup_y;
	}

}