package wrsn;

import io.jbotsim.core.Message;
import io.jbotsim.core.Node;
import io.jbotsim.ui.icons.Icons;

public class BaseStation extends Node {
	@Override
	public void onStart() {
		setIcon(Icons.STATION);
		setIconSize(16);

		// Initiates tree construction with an empty message
		sendAll(new Message(null, "INIT"));
	}
}