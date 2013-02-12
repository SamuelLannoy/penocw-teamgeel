package robot;

import java.util.List;

import exception.CommunicationException;
import field.Border;
import field.Field;

public interface AbstractRobotConnector {
	
	public abstract void startMovingForward();
	public abstract void startMovingBackward();
	public abstract void moveForward(double distance);
	public abstract void moveBackward(double distance);
	public abstract void stopMoving();
	public abstract void startTurningLeft();
	public abstract void startTurningRight();
	public abstract void turnLeft(double angle);
	public abstract void turnRight(double angle);
	public abstract void setTurnSpeed(double turnSpeed);
	public abstract void setMoveSpeed(double moveSpeed);
	public abstract void orientOnWhiteLine(boolean b);
	public abstract void scanSonar();
	public abstract void setOnCenterTile(Robot robot);
	public abstract void newTileScan();
	public abstract void checkScan();
	
	public abstract double getDistanceMoved();
	public abstract double getRotationTurned();
	public abstract boolean isMoving();
	public abstract boolean isScanning();
	public abstract void scanOnlyLines(boolean flag);
	
	public abstract void initialize() throws CommunicationException;
	public abstract void terminate();
	
	public abstract List<Border> getBorderSurroundings();
	public abstract boolean isStartTile();
	public abstract boolean isFinishTile();
}
