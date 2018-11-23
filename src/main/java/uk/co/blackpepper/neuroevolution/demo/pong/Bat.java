package uk.co.blackpepper.neuroevolution.demo.pong;

import java.awt.Color;
import java.awt.Graphics;

public class Bat implements Bounceable {
	
	public static final int WIDTH = 32;
	
	public static final int HEIGHT = 128;
	
	public static final int SPEED = 8;
	
	private static final int OUT_LENGTH = 64;
	
	private final int x0;
	
	private int y0;
	
	private final int screenHeight;
	
	public Bat(int x0, int y0, int screenHeight) {
		this.x0 = x0;
		this.y0 = y0;
		this.screenHeight = screenHeight;
	}
	
	@Override
	public int getX() {
		return x0;
	}
	
	@Override
	public int getY() {
		return y0;
	}
	
	@Override
	public int getWidth() {
		return WIDTH;
	}
	
	@Override
	public int getHeight() {
		return HEIGHT;
	}
	
	@Override
	public void moveUp() {
		y0 = Math.max(y0 - SPEED, OUT_LENGTH);
	}
	
	@Override
	public void moveDown() {
		y0 = Math.min(y0 + SPEED, screenHeight - HEIGHT - OUT_LENGTH);
	}
	
	@Override
	public void plot(Graphics graphics) {
		graphics.setColor(Color.WHITE);
		
		for (int y = y0; y < y0 + HEIGHT; y++) {
			graphics.fillRect(x0, y0, WIDTH, HEIGHT);
		}
	}
}
