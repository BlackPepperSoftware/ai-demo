package uk.co.blackpepper.neuroevolution.demo.pong;

public class Pong {
	
	public static void main(String[] args) {
		Bot bot = new Bot(1);
		
		PongFrame frame = new PongFrame();
		frame.addPongListener(bot);
		frame.setVisible(true);
	}
}
