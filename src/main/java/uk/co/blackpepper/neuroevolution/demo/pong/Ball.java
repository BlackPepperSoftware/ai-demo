package uk.co.blackpepper.neuroevolution.demo.pong;

import java.awt.Dimension;

public class Ball {
	
	private int x0;
	
	private int y0;
	
	private final Dimension screenSize;
	
	private int dx;
	
	private int dy;
	
	public Ball(int x0, int y0, Dimension screenSize) {
		this.x0 = x0;
		this.y0 = y0;
		this.screenSize = screenSize;
		dx = 1;
		dy = 1;
	}
	
	public int getX() {
		return x0;
	}
	
	public int getY() {
		return y0;
	}
	
	public boolean out() {
		return x0 == 0 || x0 == screenSize.getWidth() - 1;
	}
	
	public void plot(Screen screen) {
		screen.plotPixel(x0, y0);
	}
	
	public void move() {
		x0 = Math.min(Math.max(x0 + dx, 0), screenSize.width - 1);
		y0 += dy;
		
		if (y0 == 0 || y0 == screenSize.getHeight() - 1) {
			dy = -dy;
		}
	}
	
	public void bounce() {
		dx = -dx;
	}
}
