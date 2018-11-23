package uk.co.blackpepper.neuroevolution.demo.pong;

import java.awt.Color;
import java.awt.Graphics;

public class Wall implements Bounceable {
	
	private static final int WIDTH = 32;
	
	private final int x0;
	
	private final int screenHeight;
	
	public Wall(int x0, int screenHeight) {
		this.x0 = x0;
		this.screenHeight = screenHeight;
	}
	
	@Override
	public int getX() {
		return x0;
	}
	
	@Override
	public int getY() {
		return 0;
	}
	
	@Override
	public int getWidth() {
		return WIDTH;
	}
	
	@Override
	public int getHeight() {
		return screenHeight;
	}
	
	@Override
	public void moveUp() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void moveDown() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void plot(Graphics graphics) {
		graphics.setColor(Color.WHITE);
		graphics.fillRect(x0, 0, getWidth(), getHeight());
	}
}
