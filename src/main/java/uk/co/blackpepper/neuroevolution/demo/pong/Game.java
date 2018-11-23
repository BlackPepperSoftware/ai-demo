package uk.co.blackpepper.neuroevolution.demo.pong;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.event.EventListenerList;

public class Game {
	
	private static final Dimension SIZE = new Dimension(1280, 1024);
	
	private static final boolean ONE_PLAYER = true;
	
	private final ScheduledExecutorService executor;
	
	private final EventListenerList listeners;
	
	private final Dimension screenSize;
	
	private final Bounceable bat1;
	
	private final Bounceable bat2;
	
	private final Ball ball;
	
	private boolean active;
	
	public Game(Random random) {
		executor = Executors.newSingleThreadScheduledExecutor();
		listeners = new EventListenerList();
		active = false;
		
		screenSize = SIZE;

		int batY = (screenSize.height - Bat.HEIGHT) / 2;
		bat1 = ONE_PLAYER
			? new Wall(0, screenSize.height)
			: new Bat(0, batY, screenSize.height);
		bat2 = new Bat(screenSize.width - Bat.WIDTH - 1, batY, screenSize.height);
		
		int ballSpeed = 8;
		int ballY = random.nextInt(screenSize.height - Ball.SIZE);
		int ballDeltaY = random.nextBoolean() ? -ballSpeed : ballSpeed;
		ball = new Ball(screenSize.width / 2, ballY, ballSpeed, ballDeltaY, screenSize);
	}
	
	public void addPongListener(PongListener listener) {
		listeners.add(PongListener.class, listener);
	}
	
	public void removePongListener(PongListener listener) {
		listeners.remove(PongListener.class, listener);
	}
	
	public Dimension getScreenSize() {
		return screenSize;
	}
	
	public Bounceable getBat1() {
		return bat1;
	}
	
	public Bounceable getBat2() {
		return bat2;
	}
	
	public Ball getBall() {
		return ball;
	}
	
	public void start(int tickMillis) {
		if (active) {
			throw new IllegalStateException("Game already started");
		}
		
		active = true;
		
		if (tickMillis == 0) {
			executor.schedule(this::tickUntilStopped, 0, TimeUnit.MILLISECONDS);
		}
		else {
			executor.scheduleAtFixedRate(this::tick, 0, tickMillis, TimeUnit.MILLISECONDS);
		}
	}
	
	public void stop() {
		if (!active) {
			throw new IllegalStateException("Game not started");
		}
		
		executor.shutdown();
		active = false;
		fireStoppedEvent();
	}
	
	public void plot(Graphics graphics) {
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, graphics.getClipBounds().width, graphics.getClipBounds().height);

		bat1.plot(graphics);
		bat2.plot(graphics);
		ball.plot(graphics);
	}
	
	public void tick() {
		try {
			tickGame();
			fireTickEvent();
		}
		catch (Throwable throwable) {
			throwable.printStackTrace();
		}
	}
	
	private void tickGame() {
		if (!active) {
			return;
		}
		
		ball.move();
		
		ball.collide(bat1);
		ball.collide(bat2);
		
		if (ball.out()) {
			stop();
		}
	}
	
	private void tickUntilStopped() {
		while (active) {
			tick();
		}
	}
	
	private void fireTickEvent() {
		for (PongListener listener : listeners.getListeners(PongListener.class)) {
			listener.tick(this);
		}
	}
	
	private void fireStoppedEvent() {
		for (PongListener listener : listeners.getListeners(PongListener.class)) {
			listener.stopped();
		}
	}
}
