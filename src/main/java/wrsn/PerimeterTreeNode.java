package wrsn;

import java.util.*;

import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

public class PerimeterTreeNode extends Node {

	double perimeter[] = new double[4];
	
	int numChildren = 0;
	Node parent = null;
	List<Node> children = new ArrayList<Node>();
	
	public void onMessage(Message message) {
		// "INIT" flag : construction of the spanning tree
		// "SENSING" flag : transmission of the sensed values
		// You can use other flags for your algorithms
		// if not yet in the tree
		if (message.getFlag().equals("INIT")) {
			if (parent == null) {
				parent = message.getSender();
				getCommonLinkWith(parent).setWidth(5);
				sendAll(new Message(parent, "INIT"));
			} else {
				if (message.getContent() == this) {
					children.add(message.getSender());
				}
			}
		} else if (message.getFlag().equals("XY")) {
			double[] content = (double[]) message.getContent();
			
			perimeter[0] = perimeter[0] < content[0] ? perimeter[0]: content[0];
			perimeter[1] = perimeter[1] < content[1] ? perimeter[1] : content[1];

			perimeter[2] = perimeter[2] > content[2] ? perimeter[2]: content[2];
			perimeter[3] = perimeter[3] > content[3] ? perimeter[3] : content[3];

			numChildren++;
			if (numChildren == children.size()) {
				if (parent != this) {
					send(parent, new Message(perimeter, "XY"));
				} else {
					System.err.println(perimeter[0]);
					System.err.println(perimeter[1]);
					System.err.println(perimeter[2]);
					System.err.println(perimeter[3]);
				}
			}

		}
	}

	public void getPerimeter() {
		
		perimeter[0] = getX();
		perimeter[1] = getY();

		perimeter[2] = getX();
		perimeter[3] = getY();
		
		if (children.isEmpty()) {
			send(parent, new Message(perimeter, "XY"));
		}
	}
	
	@Override
	public String toString() {
		return getID()+": x "+getX()+" y "+getY();
	}
}