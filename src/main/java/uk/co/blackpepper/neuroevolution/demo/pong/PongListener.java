package uk.co.blackpepper.neuroevolution.demo.pong;

import java.util.EventListener;

public interface PongListener extends EventListener {
	
	void tick(Game game);
}
