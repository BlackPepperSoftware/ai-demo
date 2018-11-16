package uk.co.blackpepper.neuroevolution.demo.pong;

import java.awt.Dimension;
import java.util.Random;

public class Game {
	
	private final Dimension screenSize;
	
	private final Bat bat1;
	
	private final Bat bat2;
	
	private final Ball ball;
	
	private boolean active;
	
	public Game(Dimension screenSize) {
		this.screenSize = screenSize;

		int batY = (screenSize.height - Bat.LENGTH) / 2;
		bat1 = new Bat(0, batY, screenSize.height);
		bat2 = new Bat(screenSize.width - 1, batY, screenSize.height);
		
		Random random = new Random();
		int ballY = 1 + random.nextInt(screenSize.height - 2);
		int ballDeltaY = random.nextBoolean() ? -1 : 1;
		ball = new Ball(screenSize.width / 2, ballY, 1, ballDeltaY, screenSize);
		
		active = true;
	}
	
	public Dimension getScreenSize() {
		return screenSize;
	}
	
	public Bat getBat1() {
		return bat1;
	}
	
	public Bat getBat2() {
		return bat2;
	}
	
	public Ball getBall() {
		return ball;
	}
	
	public void plot(Screen screen) {
		screen.clear();
		bat1.plot(screen);
		bat2.plot(screen);
		ball.plot(screen);
	}
	
	public void tick() {
		if (!active) {
			return;
		}
		
		ball.move();
		
		if (bat1.touches(ball) || bat2.touches(ball)) {
			ball.bounce();
		}
		
		if (ball.out()) {
			active = false;
		}
	}
	
	public void moveBat(int index, int dy) {
		Bat bat = index == 0 ? bat1 : bat2;
		bat.move(dy);
	}
}
