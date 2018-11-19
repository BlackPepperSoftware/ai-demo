package uk.co.blackpepper.neuroevolution.demo.pong;

import javax.swing.JFrame;

public class PongFrame extends JFrame {
	
	public PongFrame() {
		super("Pong");
		
		setLocationByPlatform(true);
		setSize(1280, 1024);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setContentPane(new PongPanel());
	}
	
	public void addPongListener(PongListener listener) {
		((PongPanel) getContentPane()).getGame().addPongListener(listener);
	}
}
