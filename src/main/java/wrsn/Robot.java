package wrsn;

import io.jbotsim.core.Message;
import io.jbotsim.core.Node;
import io.jbotsim.core.Point;
import io.jbotsim.ui.icons.Icons;

public class Robot extends WaypointNode {
	
	private Point stationLocation = new Point(Math.random() * 600, Math.random() * 400);;
	@Override
	public void onStart() {
		setSensingRange(30);
		setIcon(Icons.ROBOT);
		setIconSize(16);
		onArrival();
	}

	@Override
	public void onSensingIn(Node node) {
		if (node instanceof Sensor)
			((Sensor) node).battery = 255;
	}

	@Override
	public void onArrival() {
		addDestination(stationLocation.getX(), stationLocation.getY());
		addDestination(Math.random() * 600, Math.random() * 400);
	}

	@Override
	public void onMessage(Message message) {
		if(message.getFlag().equals("LOC")){
			stationLocation = (Point) message.getContent(); 
		}
		else if(message.getFlag().equals("URGENCE")) {
			//System.out.println("Robot : urgence");
			Point p = (Point) message.getContent();
			addPriorityDestination(p);
		}else if(message.getFlag().equals("DEST")) {
			Point p = (Point) message.getContent();
			addDestination(p.getX(),p.getY());
		}
	}
	
	
}