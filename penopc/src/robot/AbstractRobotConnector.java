package robot;

import java.util.List;

import communication.SeesawStatus;

import exception.CommunicationException;
import field.Border;

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
	
	public abstract boolean isStartTile();
	public abstract boolean isFinishTile();
	
	public abstract boolean hasBall();
	public abstract int getTeam();
	public abstract int getObjectNr();
	public abstract void setObjectNr(int nr);
	
	public abstract SeesawStatus getSeesawStatus();
	public abstract void ultimateCenter();
	public abstract void pauseLightSensor();
	public abstract void resumeLightSensor();

}
