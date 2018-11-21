package uk.co.blackpepper.neuroevolution.demo.pong;

public class Bat implements Bounceable {
	
	public static final int LENGTH = 6;
	
	private final int x0;
	
	private int y0;
	
	private final int screenHeight;
	
	public Bat(int x0, int y0, int screenHeight) {
		this.x0 = x0;
		this.y0 = y0;
		this.screenHeight = screenHeight;
	}
	
	@Override
	public int getY() {
		return y0;
	}
	
	@Override
	public boolean touches(Ball ball) {
		if (ball.getX() < x0 - 1 || ball.getX() > x0 + 1) {
			return false;
		}
		
		return ball.getY() >= y0 - 1 && ball.getY() < y0 + LENGTH + 1;
	}
	
	@Override
	public void move(int dy) {
		y0 = Math.min(Math.max(y0 + dy, 0), screenHeight - LENGTH);
	}
	
	@Override
	public void plot(Screen screen) {
		for (int y = y0; y < y0 + LENGTH; y++) {
			screen.plotPixel(x0, y);
		}
	}
}
