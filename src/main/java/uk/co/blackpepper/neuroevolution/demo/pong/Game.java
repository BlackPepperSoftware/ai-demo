package uk.co.blackpepper.neuroevolution.demo.pong;

import java.awt.Dimension;

public class Game {
	
	private final Bat bat1;
	
	private final Bat bat2;
	
	private final Ball ball;
	
	private boolean active;
	
	public Game(Dimension screenSize) {
		int batY = (screenSize.height - Bat.LENGTH) / 2;
		bat1 = new Bat(0, batY, screenSize.height);
		bat2 = new Bat(screenSize.width - 1, batY, screenSize.height);
		ball = new Ball(screenSize.width / 2, screenSize.height / 2, screenSize);
		active = true;
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
