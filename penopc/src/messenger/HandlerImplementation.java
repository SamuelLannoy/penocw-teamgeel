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
	
	private String getPoolID(String playerID) {
		if (playerID.equals(ownId)) {
			return "main";
		}
		return playerID;
	}
	
	@Override
	public void gameStarted() {
		/*for (String id : players) {
			if (!id.equals(ownId)) {
				robotPool.addRobot(new Robot(1), id);
			}
		}*/
		DebugBuffer.addInfo("game started with " + robotPool.getRobots().toString());
	}

	@Override
	public void gameStopped() {
		DebugBuffer.addInfo("game stopped");
	}

	@Override
	public void playerPosition(String playerID, double x, double y, double angle) {
		robotPool.updateRobot(getPoolID(playerID), x, y, angle);
		//DebugBuffer.addInfo(playerID + ": " + x + " " + y + " " + angle);
	}

	@Override
	public void gamePaused() {
		DebugBuffer.addInfo("game paused");
		
	}

	@Override
	public void playerJoined(String playerID) {
		//robotPool.addRobot(new Robot(1), playerID);
		DebugBuffer.addInfo("player " + playerID + " joined");
		
	}

	@Override
	public void playerLeft(String playerID) {
		//robotPool.removeRobot(playerID);
		DebugBuffer.addInfo("player " + playerID + " left");
	}

	@Override
	public void playerFoundObject(String playerID) {
		robotPool.getRobot(getPoolID(playerID)).setHasBall(true);
		DebugBuffer.addInfo("player " + playerID + " found object");
	}

}
