package barcode;

import infrared.IRSeeker;
import communication.Buffer;
import communication.PilotController;
import communication.SeesawStatus;

import lejos.nxt.Button;
import lejos.nxt.Sound;
import lightsensor.LightSensorVigilante;
import robot.Robot;
import touchsensor.TouchSensor;

public class BarcodeAction {
	private static Action action;
	
	public static Action getAction() {
		return action;
	}

	private static void setAction(Action action) {
		Buffer.addDebug("ROBOT: action= " + action);
		BarcodeAction.action = action;
	}
	
	public static void resetAction() {
		BarcodeAction.action = null;
	}
	
	public static void turnAroundLeft() {
		Robot.getInstance().rotateLeft(360, false);
	}
	
	public static void turnAroundRight() {
		Robot.getInstance().rotateRight(360, false);
	}
	
	public static void playMusic() {
		Sound.buzz();
	}
	
	public static void waitFiveSeconds() {
		Button.waitForAnyPress(5000);
	}
	
	public static void setHighSpeed() {
		Robot.speed = 250;
		Robot.getInstance().setTravelSpeed(250);
		Robot.getInstance().setRotateSpeed(100);
	}
	
	public static void setLowSpeed() {
		Robot.speed = 100;
		Robot.getInstance().setTravelSpeed(100);
		Robot.getInstance().setRotateSpeed(40);
	}
	
	public static void start() {
		setAction(Action.REMEMBER_START);
	}
	
	public static void finish() {
		setAction(Action.REMEMBER_FINISH);
	}
	
	public static void pickupobject(){
//		Buffer.addDebug("STOPSTREAM");
//		PilotController.stopStream();
		LightSensorVigilante.pause();
		Robot.getInstance().rotateLeft(90,false);
		Robot.getInstance().forward();
		while(!TouchSensor.getInstance().isPressed());
		Robot.getInstance().stop();
		Robot.getInstance().travel(-100, false);
		Robot.getInstance().rotateRight(90,false);
		Robot.getInstance().forward();
		while(!TouchSensor.getInstance().isPressed());
		Button.waitForAnyPress(500);
		PilotController.startStream();
//		Buffer.addDebug("STARTSTREAM");
		Robot.getInstance().stop();
//		Robot.getInstance().travel(800, false);
		Robot.getInstance().travel(-100, false);
		Robot.getInstance().rotateLeft(180, false);
		Robot.getInstance().travel(400,false);
		LightSensorVigilante.resume();
		Robot.getInstance().setHasBall();
	}
	
	public static void seesaw(){
		// check stand van de wip
		if(IRSeeker.getInstance().getValueAhead() > 5){ //TODO check of test goed werkt
			Buffer.setSeesawStatus(SeesawStatus.ISOPEN);
			double prev = Robot.speed;
			Robot.getInstance().setTravelSpeed(250);
			Robot.getInstance().travel(850,false);
			Button.waitForAnyEvent(1000);
			Robot.getInstance().travel(350,false);
			Robot.getInstance().setTravelSpeed(prev);
			Buffer.setSeesawStatus(SeesawStatus.ISOVER);
			Button.waitForAnyEvent(1000);
			Buffer.setSeesawStatus(SeesawStatus.ISNOTAPPLICABLE);
		}
		else{
			Buffer.setSeesawStatus(SeesawStatus.ISCLOSED);
			Button.waitForAnyEvent(1000);
			Buffer.setSeesawStatus(SeesawStatus.ISNOTAPPLICABLE);
		}		
	}
	
}
