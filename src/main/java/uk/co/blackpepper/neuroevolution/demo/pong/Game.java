package uk.co.blackpepper.neuroevolution.demo.pong;

import java.awt.Dimension;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.event.EventListenerList;

public class Game {
	
	private static final Dimension SIZE = new Dimension(40, 32);
	
	private static final boolean ONE_PLAYER = true;
	
	private final ScheduledExecutorService executor;
	
	private final EventListenerList listeners;
	
	private final Dimension screenSize;
	
	private final Bounceable bat1;
	
	private final Bounceable bat2;
	
	private final Ball ball;
	
	private boolean active;
	
	public Game() {
		executor = Executors.newSingleThreadScheduledExecutor();
		listeners = new EventListenerList();
		active = false;
		
		screenSize = SIZE;

		int batY = (screenSize.height - Bat.LENGTH) / 2;
		bat1 = ONE_PLAYER
			? new Wall(0, screenSize.height)
			: new Bat(0, batY, screenSize.height);
		bat2 = new Bat(screenSize.width - 1, batY, screenSize.height);
		
		Random random = new Random();
		int ballY = 1 + random.nextInt(screenSize.height - 2);
		int ballDeltaY = random.nextBoolean() ? -1 : 1;
		ball = new Ball(screenSize.width / 2, ballY, 1, ballDeltaY, screenSize);
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
		
		executor.scheduleAtFixedRate(this::tick, 0, tickMillis, TimeUnit.MILLISECONDS);
		active = true;
	}
	
	public void stop() {
		if (!active) {
			throw new IllegalStateException("Game not started");
		}
		
		executor.shutdown();
		active = false;
		fireStoppedEvent();
	}
	
	public void plot(Screen screen) {
		screen.clear();
		bat1.plot(screen);
		bat2.plot(screen);
		ball.plot(screen);
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
	
	public void moveBat(int index, int dy) {
		Bounceable bat = index == 0 ? bat1 : bat2;
		bat.move(dy);
	}
	
	private void tickGame() {
		if (!active) {
			return;
		}
		
		ball.move();
		
		if (bat1.touches(ball) || bat2.touches(ball)) {
			ball.bounce();
		}
		
		if (ball.out()) {
			stop();
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
