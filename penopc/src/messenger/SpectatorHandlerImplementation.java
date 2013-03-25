package messenger;

import java.io.IOException;

import field.Field;
import field.fromfile.FieldFactory;
import peno.htttp.DisconnectReason;
import peno.htttp.SpectatorHandler;
import robot.DebugBuffer;
import robot.RobotPool;

public class SpectatorHandlerImplementation implements SpectatorHandler {
	RobotPool robotPool;
	String ownId;
	Field field;
	
	

	public SpectatorHandlerImplementation(RobotPool pool, String ownId) {
		super();
		this.robotPool = pool;
		this.ownId = ownId;
		try {
			field = FieldFactory.fieldFromFile("C:\\demo2.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void gameStarted() {

	}

	@Override
	public void gameStopped() {

	}

	@Override
	public void gamePaused() {

	}

	@Override
	public void playerJoining(String playerID) {

	}

	@Override
	public void playerJoined(String playerID) {

	}

	@Override
	public void playerDisconnected(String playerID, DisconnectReason reason) {

	}

	@Override
	public void playerReady(String playerID, boolean isReady) {
	}

	@Override
	public void playerFoundObject(String playerID, int playerNumber) {

	}

	@Override
	public void playerUpdate(String playerID, int playerNumber, double x,
			double y, double angle, boolean foundObject) {
		playerNumber = playerNumber+1;
		robotPool.updateRobot(getPoolID(playerID),
				x + field.getStartPos(playerNumber).getX() * 40,
				y + field.getStartPos(playerNumber).getY() * 40,
				angle + field.getStartDir(playerNumber).toAngle());
	}
	
	private String getPoolID(String playerID) {
		if (playerID.equals(ownId)) {
			return "main";
		}
		return playerID;
	}

	@Override
	public void gameWon(int teamNumber) {
		
	}

	@Override
	public void lockedSeesaw(String playerID, int playerNumber, int barcode) {
		
	}

	@Override
	public void unlockedSeesaw(String playerID, int playerNumber, int barcode) {
		
	}

}
