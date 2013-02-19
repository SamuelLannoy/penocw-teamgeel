package communication;

import java.util.List;

import lejos.pc.comm.NXTCommException;
import exception.CommunicationException;
import field.Border;
import robot.AbstractRobotConnector;
import robot.Robot;

public class CommandEncoder implements AbstractRobotConnector {
	
	private static CommandEncoder instance;
	
	public static CommandEncoder getInstance() {
		return instance;
	}

	@Override
	public void initialize() throws CommunicationException {
		try {
			Bluetooth.getInstance().setUpConnection();
		} catch(NXTCommException e) {
			throw new CommunicationException();
		}
	}

	@Override
	public void terminate() {
		Bluetooth.getInstance().breakDownConnection();
	}

	@Override
	public void startMovingForward() {
		Bluetooth.getInstance().send(Encoding.FORWARD.ordinal(),0,0,false);
	}

	@Override
	public void startMovingBackward() {
		Bluetooth.getInstance().send(Encoding.BACKWARD.ordinal(), 0, 0, false);
	}

	@Override
	public void moveForward(double distance) {
		Bluetooth.getInstance().send(Encoding.TRAVEL.ordinal(),(float)distance,0,false);
	}
	
	public void moveForward(double distance, boolean immediateReturn){
		Bluetooth.getInstance().send(Encoding.TRAVEL.ordinal(),(float)distance,0,immediateReturn);
	}

	@Override
	public void moveBackward(double distance) {
		Bluetooth.getInstance().send(Encoding.TRAVELBACKWARD.ordinal(),(float)distance,0,false);
	}

	@Override
	public void stopMoving() {
		Bluetooth.getInstance().send(Encoding.STOPMOVING.ordinal(),0,0,false);
	}

	@Override
	public void startTurningLeft() {
		Bluetooth.getInstance().send(Encoding.ROTATELEFT.ordinal(),0,0,false);
		
	}

	@Override
	public void startTurningRight() {
		Bluetooth.getInstance().send(Encoding.ROTATERIGHT.ordinal(),0,0,false);
		
	}

	@Override
	public void turnLeft(double angle) {
		Bluetooth.getInstance().send(Encoding.ROTATELEFTANGLE.ordinal(),(float)angle,0,false);		
	}

	@Override
	public void turnRight(double angle) {
		Bluetooth.getInstance().send(Encoding.ROTATERIGHTANGLE.ordinal(),(float)angle,0,false);
		
	}

	@Override
	public void setTurnSpeed(double turnSpeed) {
		Bluetooth.getInstance().send(Encoding.SETTURNSPEED.ordinal(), turnSpeed, 0, false);
	}

	@Override
	public void setMoveSpeed(double moveSpeed) {
		Bluetooth.getInstance().send(Encoding.SETMOVESPEED.ordinal(), moveSpeed, 0, false);
	}
	
	@Override
	public boolean isMoving() {
		return Status.isMoving();
	}

	@Override
	public double getDistanceMoved() {
		return Status.getMovementIncrement();
	}

	@Override
	public double getRotationTurned() {
		return Status.getAngleIncrement();
	}
	
	@Override
	public boolean isStartTile() {
		return Status.isStartTile();
	}
	
	@Override
	public boolean isFinishTile() {
		return Status.isFinishTile();
	}
	
	//scanning a barcode
	@Override
	public boolean isScanning() {
		return Status.isScanning();
	}

	@Override
	public List<Border> getBorderSurroundings() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void scanSonar(){
		Bluetooth.getInstance().send(Encoding.SCANFORWALLS.ordinal(), 0, 0, false);
	}
	
	@Override
	public void newTileScan() {
		Bluetooth.getInstance().send(Encoding.NEWTILESCAN.ordinal(), 0.0, 0.0, false);
	}
	
	@Override
	public void orientOnWhiteLine(boolean scan){
		Bluetooth.getInstance().send(Encoding.ORIENTONWHITELINE.ordinal(), 0, 0, scan);
	}

	@Override
	public void setOnCenterTile(Robot robot){
		Bluetooth.getInstance().send(Encoding.SETONCENTERTILE.ordinal(), 0, 0, false);
	}

	@Override
	public void scanOnlyLines(boolean flag) {
		Bluetooth.getInstance().send(Encoding.ONLYLINES.ordinal(), 0, 0, flag);
	}

	@Override
	public void checkScan() {
		Bluetooth.getInstance().send(Encoding.CHECKSCAN.ordinal(), 0, 0, false);
	}

}
