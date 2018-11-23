package uk.co.blackpepper.neuroevolution.demo.pong;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class Ball {
	
	public static final int SIZE = 32;
	
	private int x0;
	
	private int y0;
	
	private final Dimension screenSize;
	
	private int dx;
	
	private int dy;
	
	public Ball(int x0, int y0, int dx, int dy, Dimension screenSize) {
		this.x0 = x0;
		this.y0 = y0;
		this.screenSize = screenSize;
		this.dx = dx;
		this.dy = dy;
	}
	
	public int getX() {
		return x0;
	}
	
	public int getY() {
		return y0;
	}
	
	public int getDeltaX() {
		return dx;
	}
	
	public int getDeltaY() {
		return dy;
	}
	
	public boolean out() {
		return x0 == 0 || x0 == screenSize.getWidth() - SIZE - 1;
	}
	
	public void plot(Graphics graphics) {
		graphics.setColor(Color.WHITE);
		graphics.fillRect(x0, y0, SIZE, SIZE);
	}
	
	public void move() {
		x0 = Math.min(Math.max(x0 + dx, 0), screenSize.width - SIZE - 1);
		
		y0 += dy;
		
		if (y0 < 0) {
			y0 = -y0;
			dy = -dy;
		}
		
		if (y0 + Ball.SIZE >= screenSize.height) {
			y0 = 2 * (screenSize.height - Ball.SIZE) - y0;
			dy = -dy;
		}
	}
	
	public void collide(Bounceable bat) {
		int x1 = x0 + SIZE;
		int y1 = y0 + SIZE;
		
		int batX0 = bat.getX();
		int batX1 = batX0 + bat.getWidth();
		int batY0 = bat.getY();
		int batY1 = batY0 + bat.getHeight();
		
		if (y1 < batY0 || y0 >= batY1) {
			return;
		}
		
		if (x1 >= batX0 && x1 < batX1) {
			x0 -= 2 * (x1 - batX0);
			dx = -dx;
		}
		else if (x0 >= batX0 && x0 < batX1) {
			x0 += 2 * (batX1 - x0);
			dx = -dx;
		}
	}
}
