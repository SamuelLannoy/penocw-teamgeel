package barcode;

import java.io.File;

import communication.Buffer;

import lejos.nxt.Button;
import lejos.nxt.Sound;
import robot.Robot;

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
		/*Sound.buzz();
		Button.waitForAnyPress(3200);*/
		// Super Mario Bros Theme Song
		//TODO test super_mario.wav
		File file = new File("super_mario.wav");
		int ret = lejos.nxt.Sound.playSample(file,3000);
		Buffer.addDebug("music returned: " + ret);
//		Sound.playNote(Sound.XYLOPHONE, 330, 1600);
//		Button.waitForAnyPress(800);
//		Sound.playNote(Sound.XYLOPHONE, 330, 1600);
//		Button.waitForAnyPress(800);
//		Sound.playNote(Sound.XYLOPHONE, 330, 1600);
//		Button.waitForAnyPress(800);
//		Sound.playNote(Sound.XYLOPHONE, 262, 1600);
//		Button.waitForAnyPress(800);
//		Sound.playNote(Sound.XYLOPHONE, 330, 1600);
//		Button.waitForAnyPress(800);
//		Sound.playNote(Sound.XYLOPHONE, 392, 1600);
//		Button.waitForAnyPress(800);
//		Sound.playNote(Sound.XYLOPHONE, 392, 1600);
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
	
}