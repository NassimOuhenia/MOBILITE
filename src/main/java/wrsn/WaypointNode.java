package wrsn;

import io.jbotsim.core.Node;
import io.jbotsim.core.Point;

import java.util.LinkedList;
import java.util.Queue;

public class WaypointNode extends Node {
	Queue<Point> destinations = new LinkedList<>();
	Queue<Point> urgences = new LinkedList<>();//les points prioritaires
	double speed = 1;

	public void addDestination(double x, double y) {
		destinations.add(new Point(x, y));
	}

	public void addPriorityDestination(Point p) {
		urgences.add(p);
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}

	@Override
	public void onClock() {
		if(!urgences.isEmpty()) {
			//System.out.println("urgencesssssssssssssssss");
			Point dest =  urgences.peek();
			if (distance(dest) > speed) {
				
				setDirection(dest);
				move(speed);
			} else {
				setLocation(dest);
				urgences.poll();
				onArrival();
			}
		}else {
			if (!destinations.isEmpty()) {
				Point dest = destinations.peek();
				if (distance(dest) > speed) {
					setDirection(dest);
					move(speed);
				} else {
					setLocation(dest);
					destinations.poll();
					onArrival();
				}
			}
		}
	}

	public void onArrival() { // to be overridden
	}
}