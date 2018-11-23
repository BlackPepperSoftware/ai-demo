package uk.co.blackpepper.neuroevolution.demo.pong;

import java.awt.Dimension;

import javax.swing.JFrame;

public class PongFrame extends JFrame {
	
	public PongFrame() {
		super("Pong");
		
		setLocationByPlatform(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		PongPanel panel = new PongPanel();
		panel.setPreferredSize(new Dimension(1280, 1024));
		setContentPane(panel);
		pack();
	}
	
	public void setGame(Game game) {
		((PongPanel) getContentPane()).setGame(game);
	}
}
