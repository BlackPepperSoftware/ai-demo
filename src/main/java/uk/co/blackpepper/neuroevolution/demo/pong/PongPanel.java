package uk.co.blackpepper.neuroevolution.demo.pong;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

public class PongPanel extends JComponent {
	
	private final PongListener refreshListener;
	
	private final Screen screen;
	
	private Game game;
	
	private Image image;
	
	public PongPanel() {
		refreshListener = new PongAdapter() {
			@Override
			public void tick(Game game) {
				refresh();
			}
		};
		
		setGame(new Game());
		screen = new Screen(game.getScreenSize());
		
		bindActions();
	}
	
	public void setGame(Game game) {
		if (this.game != null) {
			this.game.removePongListener(refreshListener);
		}
		
		this.game = game;
		
		game.addPongListener(refreshListener);
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
				setGame(new Game());
				game.start();
			}
		};
	}
	
	private Image getImage() {
		if (image == null) {
			image = createImage(screen.getImageSource());
		}
		return image;
	}
	
	private void refresh() {
		invalidate();
		repaint(50);
	}
}
