package uk.co.blackpepper.neuroevolution.demo.pong;

import javax.swing.JFrame;
import javax.swing.event.EventListenerList;

public class PongFrame extends JFrame {
	
	public PongFrame() {
		super("PongFrame");
		
		setLocationByPlatform(true);
		setSize(1280, 1024);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setContentPane(new PongPanel());
	}
	
	public void addPongListener(PongListener listener) {
		((PongPanel) getContentPane()).addPongListener(listener);
	}
}
