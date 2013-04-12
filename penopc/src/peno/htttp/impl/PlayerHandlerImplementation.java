package peno.htttp.impl;

import java.util.List;

import field.representation.FieldRepresentation;
import field.simulation.FieldSimulation;

import peno.htttp.DisconnectReason;
import peno.htttp.PlayerHandler;
import peno.htttp.Tile;
import robot.DebugBuffer;
import robot.Position;
import robot.Robot;

public class PlayerHandlerImplementation implements PlayerHandler {
	
	private Robot robot;

	public PlayerHandlerImplementation(Robot robot) {
		super();
		this.robot = robot;
	}
	
	@Override
	public void gameStarted() {
		/*for (String id : players) {
			if (!id.equals(ownId)) {
				robotPool.addRobot(new Robot(1), id);
			}
		}*/
		DebugBuffer.addInfo("game started");
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
		robot.setObjectNr(objectNr);
		//playerNumber += 1;
		DebugBuffer.addInfo("player number: " + playerNumber + " object nr " + objectNr);
		robot.setPlayerNr(playerNumber);
	}

	@Override
	public void playerJoining(String playerId) {
		DebugBuffer.addInfo("player " + playerId + " joining");
	}

	@Override
	public void playerDisconnected(String playerId, DisconnectReason disconnectReason) {
		DebugBuffer.addInfo("player " + playerId + " disconnected because of " + disconnectReason.toString());
	}

	@Override
	public void playerReady(String playerId, boolean isReady) {
		DebugBuffer.addInfo("player " + playerId + " ready: " + isReady);
	}

	@Override
	public void playerFoundObject(String playerID, int playerNumber) {
		
	}

	@Override
	public void playerJoined(String playerId) {
		DebugBuffer.addInfo("player " + playerId + " joined");
	}

	@Override
	public void teamConnected(String partnerId) {
		robot.setTeamMateID(partnerId);
	}

	@Override
	public void teamTilesReceived(List<Tile> tiles) {
		if (!robot.receivedTeamTiles()) {
			FieldRepresentation teamMateField = new FieldRepresentation(tiles);
			robot.getTeamMate().setField(teamMateField);			
			robot.setReceivedTeamTiles(true);
		} else {
			robot.getTeamMate().getField().addFromTeammate(tiles);
		}
	}

	@Override
	public void gameWon(int teamNumber) {
		DebugBuffer.addInfo("victory for " + teamNumber);
	}
	

	@Override
	public void teamPosition(double x, double y, double angle) {
		//DebugBuffer.addInfo("teammate orig: " + x + " " + y);
		x = x + robot.getField().getTranslX() * 40;
		y = y + robot.getField().getTranslY() * 40;
		angle = angle + robot.getField().getRotation();
		//DebugBuffer.addInfo("teammate new: " + x + " " + y);
		
		//DebugBuffer.addInfo("teammate pos: " + (int)(x / 40) + " " + (int)(y / 40));

		double[] tilePos = FieldSimulation.convertToInTilePos(new double[]{x,y});
		
		robot.getTeamMate().setPosition(new Position(
				tilePos[0], tilePos[1], angle
				), new field.Tile(FieldSimulation.convertToTilePosition(x, y)));
		robot.getTeamMate()
			.setCurrTile(new field.Tile(FieldSimulation.convertToTilePosition(x, y)));
	}

}
