package uk.co.blackpepper.neuroevolution.demo.pong;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

public class PongPanel extends JComponent {
	
	private final PongListener refreshListener;
	
	private Game game;
	
	public PongPanel() {
		refreshListener = new PongAdapter() {
			@Override
			public void tick(Game game) {
				repaint();
			}
		};
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
		if (game != null) {
			game.plot(graphics);
		}
	}
	
	private void bindActions() {
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "bat1Up");
		getActionMap().put("bat1Up", moveBatAction("bat1Up", (event) -> game.getBat1().moveUp()));
		
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, 0), "bat1Down");
		getActionMap().put("bat1Down", moveBatAction("bat1Down", (event) -> game.getBat1().moveDown()));
		
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_QUOTE, 0), "bat2Up");
		getActionMap().put("bat2Up", moveBatAction("bat2Up", (event) -> game.getBat2().moveUp()));
		
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, 0), "bat2Down");
		getActionMap().put("bat2Down", moveBatAction("bat2Down", (event) -> game.getBat2().moveDown()));
		
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
				// TODO: reimplement restart game!
			}
		};
	}
}
