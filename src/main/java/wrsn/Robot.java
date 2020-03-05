package wrsn;

import io.jbotsim.core.Node;
import io.jbotsim.ui.icons.Icons;

public class Robot extends WaypointNode {
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
		addDestination(Math.random() * 600, Math.random() * 400);
	}
}