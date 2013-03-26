package messenger;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import field.Field;
import field.fieldmerge.TileConverter;
import field.fromfile.FieldFactory;

import peno.htttp.DisconnectReason;
import peno.htttp.PlayerHandler;
import peno.htttp.Tile;
import robot.DebugBuffer;
import robot.Position;
import robot.Robot;
import robot.RobotModel;
import robot.RobotPool;

public class PlayerHandlerImplementation implements PlayerHandler {
	
	RobotPool robotPool;
	String ownId;
	Field field;

	public PlayerHandlerImplementation(RobotPool pool, String ownId) {
		super();
		robotPool = pool;
		this.ownId = ownId;
		try {
			field = FieldFactory.fieldFromFile("C:\\demo2.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	/*@Override
	public void playerPosition(String playerID, double x, double y, double angle) {
		robotPool.updateRobot(getPoolID(playerID), x, y, angle);
		//DebugBuffer.addInfo(playerID + ": " + x + " " + y + " " + angle);
	}*/

	@Override
	public void gamePaused() {
		DebugBuffer.addInfo("game paused");
		
	}

	@Override
	public void gameRolled(int playerNumber, int objectNr) {
		robotPool.getMainRobot().setObjectNr(objectNr);
		//playerNumber += 1;
		robotPool.getMainRobot().setPlayerNr(playerNumber);
		
		field.Tile tile = new field.Tile(field.getStartPos(playerNumber));
		
		/*robotPool.getMainRobot().setPosition(
				new Position(0,0,field.getStartDir(playerNumber+1).toAngle()),
				tile);*/
		robotPool.getMainRobot().setStartPos(new Position(tile.getPosition().getX() * 40,
				tile.getPosition().getY() * 40,
				field.getStartDir(playerNumber).toAngle()));
		if (robotPool.getMainRobot().isSim()) {
			//DebugBuffer.addInfo("real pos = " + tile.getPosition());
			robotPool.getMainRobot().setSimLoc(tile.getPosition().getX() * 40,
					tile.getPosition().getY() * 40,
					field.getStartDir(playerNumber).toAngle());
		}
	}

	@Override
	public void playerJoining(String playerID) {
		DebugBuffer.addInfo("player " + playerID + " joining");
	}

	@Override
	public void playerDisconnected(String playerID, DisconnectReason reason) {
		DebugBuffer.addInfo("player " + playerID + " disconnected because of " + reason.toString());
	}

	@Override
	public void playerReady(String playerID, boolean isReady) {
		DebugBuffer.addInfo("player " + playerID + " ready: " + isReady);
	}

	@Override
	public void playerFoundObject(String playerID, int playerNumber) {
		robotPool.getRobot(getPoolID(playerID)).setHasBall(true);
		DebugBuffer.addInfo("player " + playerID + " found object");
	}

	@Override
	public void playerJoined(String playerID) {
		DebugBuffer.addInfo("player " + playerID + " joined");
	}

	@Override
	public void teamConnected(String partnerID) {
		robotPool.getMainRobot().setTeamMateID(partnerID);
	}

	@Override
	public void teamTilesReceived(List<Tile> tiles) {
		Field tField = robotPool.getMainRobot().getTeamMateField();
		for (Tile tile : tiles) {
			TileConverter.convertToFieldTile(tile, tField);
		}
	}

	@Override
	public void gameWon(int teamNumber) {
		DebugBuffer.addInfo("victory for " + teamNumber);
	}

	@Override
	public void teamPosition(double x, double y, double angle) {
		
	}

}
