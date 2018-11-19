package uk.co.blackpepper.neuroevolution.demo.pong;

public class Pong {
	
	public static void main(String[] args) {
		Bot bot = new Bot();
		
		PongFrame frame = new PongFrame();
		frame.addPongListener(bot);
		frame.setVisible(true);
	}
}
