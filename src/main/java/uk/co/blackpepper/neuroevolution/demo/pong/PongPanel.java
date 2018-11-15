package uk.co.blackpepper.neuroevolution.demo.pong;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

public class PongPanel extends JComponent {
	
	private static final Dimension SIZE = new Dimension(40, 32);
	
	private static final int ANIMATION_MILLIS = 100;
	
	private final Screen screen;
	
	private Game game;
	
	private final ScheduledExecutorService executor;
	
	private Image image;
	
	public PongPanel() {
		screen = new Screen(SIZE);
		restart();
		
		executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(this::tick, 0, ANIMATION_MILLIS, TimeUnit.MILLISECONDS);
		
		bindActions();
	}
	
	@Override
	protected void paintComponent(Graphics graphics) {
		graphics.drawImage(getImage(), 0, 0, getWidth(), getHeight(), this);
	}
	
	@Override
	public void invalidate() {
		super.invalidate();
		image = null;
		game.plot(screen);
	}
	
	private void bindActions() {
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "bat1Up");
		getActionMap().put("bat1Up", moveBatAction("bat1Up", (event) -> game.moveBat(0, -1)));
		
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, 0), "bat1Down");
		getActionMap().put("bat1Down", moveBatAction("bat1Down", (event) -> game.moveBat(0, 1)));
		
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_QUOTE, 0), "bat2Up");
		getActionMap().put("bat2Up", moveBatAction("bat2Up", (event) -> game.moveBat(1, -1)));
		
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, 0), "bat2Down");
		getActionMap().put("bat2Down", moveBatAction("bat2Down", (event) -> game.moveBat(1, 1)));
		
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "restart");
		getActionMap().put("restart", restartAction("restart"));
	}
	
	private Action moveBatAction(String name, ActionListener listener) {
		return new AbstractAction(name) {
			@Override
			public void actionPerformed(ActionEvent event) {
				listener.actionPerformed(event);
			}
		};
	}
	
	private Action restartAction(String name) {
		return new AbstractAction(name) {
			@Override
			public void actionPerformed(ActionEvent event) {
				restart();
			}
		};
	}
	
	private void restart() {
		game = new Game(screen.getSize());
	}
	
	private Image getImage() {
		if (image == null) {
			image = createImage(screen.getImageSource());
		}
		return image;
	}
	
	private void tick() {
		if (game.tick()) {
			refresh();
		}
	}
	
	private void refresh() {
		invalidate();
		repaint(50);
	}
}
