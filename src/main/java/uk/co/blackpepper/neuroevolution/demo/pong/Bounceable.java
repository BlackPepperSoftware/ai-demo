package uk.co.blackpepper.neuroevolution.demo.pong;

import java.awt.Graphics;

public interface Bounceable {
	
	int getX();
	
	int getY();
	
	int getWidth();
	
	int getHeight();
	
	void moveUp();
	
	void moveDown();
	
	void plot(Graphics graphics);
}
