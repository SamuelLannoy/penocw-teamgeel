package team.communication;

import java.io.IOException;

import field.TilePosition;
import field.representation.FieldRepresentation;

import robot.Robot;

public abstract class TeamCommunicator {
	
	private boolean connected = false;
	
	protected void connectionSuccesful() {
		connected = true;
	}
	
	public boolean isConnected() {
		return connected;
	}
	
	protected void checkConnected() {
		if (!isConnected()) {
			throw new IllegalStateException("TeamCommunicator not connected!");
		}
	}
	
	
	public abstract void connect(Robot robot, String gameId, String playerId)
			throws IOException;
	
	public abstract void joinGame();
	
	public abstract void setReady(boolean isReady);
	
	public abstract void sendInitialField(FieldRepresentation fieldRepresentation);
	
	public abstract void sendNewTiles(FieldRepresentation fieldRepresentation, TilePosition... tilePositions);
	
	public abstract void lockSeesaw(int barcode);
	
	public abstract void unlockSeesaw();
	
	public abstract void foundObject();
	
	public abstract void joinTeam(int teamNr);
	
	public abstract void updatePosition(double x, double y, double angle);
	
	

}
