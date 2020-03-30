

import io.jbotsim.core.Color;
import io.jbotsim.core.Link;
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
		if (node instanceof Sensor) {
			((Sensor) node).battery = 255;
			if(((Sensor) node).getCouleur().equals(Color.RED)) {
				((Sensor) node).setCouleur(((Sensor) node).getColor());
				((Sensor) node).setParent(null);
				for(Node n : getTopology().getNodes()) {
					if(n instanceof Sensor) {
						try {
						if(!((Sensor) node).getFather().equals(((Sensor) n).getFather())) {
							((Sensor) n).setParent(null);
						}
						}catch(Exception e) {
							
						}
					}
				}
				for(Link l : getTopology().getLinks())
					l.setWidth(1);
			}
		}
	}

	
	@Override
	public void onArrival() {
		addDestination(Math.random() * 600, Math.random() * 400);
	}
}