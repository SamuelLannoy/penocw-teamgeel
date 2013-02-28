package messenger;

import java.util.Collection;

import peno.htttp.Handler;
import robot.DebugBuffer;
import robot.Robot;
import robot.RobotPool;

public class HandlerImplementation implements Handler {
	
	RobotPool robotPool;
	String ownId;

	public HandlerImplementation(RobotPool pool, String ownId) {
		robotPool = pool;
		this.ownId = ownId;
	}
	
	@Override
	public void gameStarted(Collection<String> players) {
		for (String id : players) {
			if (!id.equals(ownId)) {
				robotPool.addRobot(new Robot(1), id);
			}
		}
		DebugBuffer.addInfo("game started with " + robotPool.getRobots().toString());
	}

	@Override
	public void gameStopped() {
		DebugBuffer.addInfo("game stopped");
	}

	@Override
	public void playerPosition(String playerID, double x, double y, double angle) {
		robotPool.updateRobot(playerID, x, y, angle);
		DebugBuffer.addInfo(playerID + ": " + x + " " + y + " " + angle);
	}

}
