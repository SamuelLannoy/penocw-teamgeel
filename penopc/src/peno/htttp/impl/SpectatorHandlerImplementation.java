package peno.htttp.impl;

import java.io.IOException;

import field.Field;
import field.Direction;
import field.TilePosition;
import field.fromfile.FieldFactory;
import field.simulation.FieldSimulation;
import peno.htttp.DisconnectReason;
import peno.htttp.PlayerDetails;
import peno.htttp.SpectatorHandler;
import robot.DebugBuffer;
import robot.Position;
import robot.RobotPool;

public class SpectatorHandlerImplementation implements SpectatorHandler {
	RobotPool robotPool;
	String ownId;
	FieldSimulation field;
	
	

	public SpectatorHandlerImplementation(RobotPool pool, String ownId, FieldSimulation field) {
		super();
		this.robotPool = pool;
		this.ownId = ownId;
		this.field = field;
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
		robotPool.getRobot(getPoolID(playerID)).setReady(isReady);
		DebugBuffer.addInfo("player " + playerID + " found object");
	}

	@Override
	public void playerFoundObject(String playerID, int playerNumber) {
		robotPool.getRobot(getPoolID(playerID)).setHasBall(true);
		DebugBuffer.addInfo("player " + playerID + " found object");
	}

	@Override
	public void playerUpdate(PlayerDetails playerID, int playerNumber, long x,
			long y, double angle, boolean foundObject) {
		//System.out.println("x " + x + " y " + y + " a " + angle);


		int[] sendPos = TilePosition.rotate(90,
				new TilePosition((int)x, (int)y),
				TilePosition.POSITION_ZERO);
		x = sendPos[0];
		y = sendPos[1];
		angle *= -1;
		//System.out.println("x " + x + " y " + y + " a " + angle);
		//playerNumber = playerNumber+1;
		/*int[] newpos = TilePosition.rotate((int)field
				.getStartDir(playerNumber).opposite().toAngle(), new TilePosition((int)x, (int)y), new TilePosition(0, 0));
		DebugBuffer.addInfo("pos= " + newpos[0] + " " + newpos[1]);*/
		//DebugBuffer.addInfo("pos= " + newpos[0] + " " + newpos[1]);
		int[] newpos = convertRelativeToAbsolutePosition((int)x, (int)y, field, playerNumber);
		robotPool.updateRobot(getPoolID(playerID.getPlayerID()),
				newpos[0],
				newpos[1],
				angle + field.getStartDir(playerNumber).toAngle());
	}
	
	private int[] convertRelativeToAbsolutePosition(int x, int y, FieldSimulation field, int playerNumber) {
		int[] newpos = new int[] {x,y};
		switch(field.getStartDir(playerNumber)) {
			case BOTTOM:
				newpos = new int[] {-(int)x,-(int)y};
				break;
			case LEFT:
				newpos = new int[] {-(int)y,(int)x};
				break;
			case RIGHT:
				newpos = new int[] {(int)y,-(int)x};
				break;
			case TOP:
				break;
			default:
				break;
			
		}
		newpos[0] += field.getStartPos(playerNumber).getX();
		newpos[1] += field.getStartPos(playerNumber).getY();
		return newpos;
	}
	
	private String getPoolID(String playerID) {
		if (playerID.equals(ownId)) {
			return "main";
		}
		return playerID;
	}

	@Override
	public void gameWon(int teamNumber) {
		field.setWinFlag(teamNumber);
	}

	@Override
	public void lockedSeesaw(String playerID, int playerNumber, int barcode) {
		DebugBuffer.addInfo("player " + playerID + " locked seesaw " + barcode);
		field.playerLockedSeesaw(barcode);
	}

	@Override
	public void unlockedSeesaw(String playerID, int playerNumber, int barcode) {
		DebugBuffer.addInfo("player " + playerID + " unlocked seesaw " + barcode);
		field.playerUnlockedSeesaw(barcode);
	}

	@Override
	public void playerRolled(PlayerDetails playerID, int playerNumber) {
		if (ownId.equals(playerID.getPlayerID())) {
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
						field.getStartDir(playerNumber).toAngle() * Math.PI / 180);
				//robotPool.getMainRobot().setPosition(new robot.Position(0, 0, field.getStartDir(playerNumber).toAngle()));
			}
		}
	}

}
