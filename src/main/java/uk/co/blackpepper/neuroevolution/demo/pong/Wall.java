package uk.co.blackpepper.neuroevolution.demo.pong;

public class Wall implements Bounceable {
	
	private final int x0;
	
	private final int screenHeight;
	
	public Wall(int x0, int screenHeight) {
		this.x0 = x0;
		this.screenHeight = screenHeight;
	}
	
	@Override
	public int getY() {
		return 0;
	}
	
	@Override
	public boolean touches(Ball ball) {
		return ball.getX() >= x0 - 1 && ball.getX() <= x0 + 1;
	}
	
	@Override
	public void move(int dy) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void plot(Screen screen) {
		for (int y = 0; y < screenHeight; y++) {
			screen.plotPixel(x0, y);
		}
	}
}
