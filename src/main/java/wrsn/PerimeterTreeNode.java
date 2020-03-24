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

			setPerimeter((double[]) message.getContent());
			numChildren++;
			if (numChildren == children.size()) {
				if (parent != this) {
					send(parent, new Message(perimeter, "XY"));
				} else {
					sendPerimeter();
				}
			}
		}
	}
	
	/**
	 * définir deux perimètres différent pour les deux robots
	 */
	public void sendPerimeter() {
		
		double middle_x = (perimeter[0] + perimeter[2]) / 2;
		double middle_y = (perimeter[1] + perimeter[3]) / 2;
		
		double [] bot1 = {perimeter[0], middle_x, perimeter[1], perimeter[3], getX(), getY()};
		double [] bot2 = {middle_x, perimeter[2], perimeter[1], perimeter[3], getX(), getY()};
							
		if (perimeter[2] < perimeter[3]) {
			bot1[1] = perimeter[2];
			bot1[3] = middle_y;
			
			bot2[0] = perimeter[0];
			bot2[2] = middle_y;
		} 
		
		sendAll(new Message(bot1, "BOT1"));
		sendAll(new Message(bot2, "BOT2"));
	}
	
	/**
	 * mis à jour afin d'obtenir les extrémités de la topologie
	 * @param content
	 */
	public void setPerimeter(double content[]) {

		for (int i = 0; i < 4; i++) {
			perimeter[i] = (i < 2) ? Math.min(perimeter[i], content[i]) : Math.max(perimeter[i], content[i]);
		}
	}
	
	/**
	 * l'envoie des positions des capteurs commençant par les feuilles
	 */
	public void getPerimeter() {

		for (int i = 0; i < 4; i++) {
			perimeter[i] = (i % 2 == 0) ? getX() : getY();
		}

		if (children.isEmpty()) {
			send(parent, new Message(perimeter, "XY"));
		}
	}
}