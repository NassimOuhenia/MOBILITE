

import java.util.*;

import io.jbotsim.core.Color;
import io.jbotsim.core.Link;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

public class Sensor extends Node {
	Node parent = null;
	int battery = 255;
	boolean sent = false;
	private List<Node> children = new ArrayList<>();
	Color color = getColor();
	@Override
	public void onMessage(Message message) {
		color = getColor();
		// "INIT" flag : construction of the spanning tree
		// "SENSING" flag : transmission of the sensed values
		// You can use other flags for your algorithms
		if (message.getFlag().equals("INIT")) {
			// if not yet in the tree
			if (parent == null) {
				// enter the tree
				parent = message.getSender();
				if(battery != 0) 
					getCommonLinkWith(parent).setWidth(4);
				// propagate further
				sendAll(message);
			}
		} else if (message.getFlag().equals("SENSING")) {
			if(parent!=null)
				send(parent,message);
		}
	}

	@Override
	public void send(Node destination, Message message) {
		if (battery > 0) {		
			super.send(destination, message);
			battery--;
			updateColor();
			if(!(battery > 0)) {
				color = this.getColor();
				getCommonLinkWith(parent).setWidth(1);
				this.parent = this;
				for(Node n : this.getNeighbors()) {
					if(n instanceof Sensor) {
						if(this.equals(((Sensor) n).getFather())) {
							getCommonLinkWith(n).setWidth(1);
							((Sensor) n).setParent(null);
						}
					}
				}
				
				for(Node n : getTopology().getNodes()) {
					if(n instanceof Sensor) {
						if(!this.equals(((Sensor) n).getFather())) {
							//getCommonLinkWith(n).setWidth(1);
							((Sensor) n).setParent(null);
						}
					}
				}
				for(Link l : getTopology().getLinks())
					l.setWidth(1);
			}
		}
	}

	@Override
	public void onClock() {
		
		if (parent != null) { // if already in the tree
			if (Math.random() < 0.02) { // from time to time...
				double sensedValue = Math.random(); // sense a value
				send(parent, new Message(sensedValue, "SENSING")); // send it to parent
			}
		}
	}

	protected void updateColor() {
		setColor(battery == 0 ? Color.red : new Color(255 - battery, 255 - battery, 255));
	}
	
	public Node getFather() {
		return parent;
	}
	
	public void setParent(Node n) {
		parent = n;
	}
	
	public Color getCouleur() {
		return color;
	}

	public void setCouleur(Color c) {
		color =c;
	}
}