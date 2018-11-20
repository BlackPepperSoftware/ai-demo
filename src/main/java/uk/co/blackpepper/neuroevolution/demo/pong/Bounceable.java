package uk.co.blackpepper.neuroevolution.demo.pong;

public interface Bounceable {
	
	int getY();
	
	boolean touches(Ball ball);
	
	void move(int dy);
	
	void plot(Screen screen);
}
