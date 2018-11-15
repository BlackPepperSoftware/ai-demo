package uk.co.blackpepper.neuroevolution.demo.pong;

import javax.swing.JFrame;

public class Pong extends JFrame {
	
	public Pong() {
		super("Pong");
		
		setLocationByPlatform(true);
		setSize(1280, 1024);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setContentPane(new PongPanel());
	}
	
	public static void main(String[] args) {
		new Pong().setVisible(true);
	}
}
