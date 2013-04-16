package lightsensor;

import lejos.nxt.Button;
import robot.Robot;
import communication.Buffer;

public class LightSensorVigilante extends Thread {
	
	public LightSensorVigilante() {
		
	}
	
	private static boolean pause = false;
	private static boolean onlyLines = false;
	private static boolean turnBarcode = false;
	
	/**
	 * Pause the lightsensor
	 * The lightsensor is set inactive when the current scan has finished.
	 */
	public static void pause() {
		pause = true;
	}
	
	/**
	 * Resume the lightsensor
	 */
	public static void resume() {
		pause = false;
	}
	
	public static void setTurningOnBarcode(boolean set){
		turnBarcode = set;
	}
	
	/**
	 * Only lines will be scanned, barcodes are ignored.
	 * The change will only take effect after the current scan has finished.
	 */
	public static void scanOnlyLines(){
		Buffer.addDebug("ROBOT: only lines set to true");
		onlyLines = true;
	}
	
	/*public static void setNoLines(boolean set){
		noLines = set;
	}*/
	
	/**
	 * Default mode, scan both barcodes and white lines.
	 * The change will only take effect after the current scan has finished.
	 */
	public static void scanBarcodesAndLines(){
		Buffer.addDebug("ROBOT: only lines set to false");
		onlyLines = false;
	}
	
	boolean pauseTemp = false;
	
	/**
	 * Constantly scans for lines and barcodes (depending on the variable
	 * onlyLines) and updates the buffer to what the lightsensor scanned.
	 */
	public void run() {
		while (true) {
			Button.waitForAnyPress(5);
			if (pause && !pauseTemp) {
				Buffer.addDebug("Vigilante Pause " + pause);
				pauseTemp = true;
				
			}
			if (!pause) {
				pauseTemp = false;
				LightSensorUpdate update = null;
				boolean curOnlyLines = onlyLines; //not possible to change this variable if a scan is in progress
				//boolean curNoLines = noLines;
					update = lightsensor.LightSensor.getInstance().lightSensorVigilante(curOnlyLines, turnBarcode);
					Robot.getInstance().setScanning(false);
//					if(update.equals(LightSensorUpdate.UNKNOWN)){ //barcode not read correctly or not known?
//						int i = 0;
//						while(update == LightSensorUpdate.UNKNOWN && i < 4){
//							int sign = ((i % 2) == 0) ? -1 : 1;
//							Robot.getInstance().travel(sign*250, true);
//							update = lightsensor.LightSensor.getInstance().lightSensorVigilante(curOnlyLines);
//							i++;
//						}
//						if((i%2) == 1){
//							Robot.getInstance().travel(250, false);
//						}
//					}
					if(Robot.getInstance().isNoLines() && update == LightSensorUpdate.LINE){
						//do nothing
					}
					else if (update == LightSensorUpdate.BROWN){
						// do nothing
					}
					else if(update != null){ //update == null when barcode was scanned but the mode was onlyLines
						Buffer.addLightUpdate(update.ordinal());
						System.out.println("lightsensorType: "+ update.toString());
					}
			}
		}
	}
}