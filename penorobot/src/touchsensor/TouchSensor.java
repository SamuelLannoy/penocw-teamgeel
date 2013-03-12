package touchsensor;

import lejos.nxt.SensorPort;

public class TouchSensor {
	
	private static final TouchSensor instance = new TouchSensor();
	private static lejos.nxt.TouchSensor sensorL;
	private static lejos.nxt.TouchSensor sensorR;
	
	/**
	 * Creates a new touch sensor as a singleton object.
	 */
	private TouchSensor() {
		sensorL = new lejos.nxt.TouchSensor(SensorPort.S1);
		sensorR = new lejos.nxt.TouchSensor(SensorPort.S4);
	}
	
	public static TouchSensor getInstance() {
		return instance;
	}
	
	public boolean isPressed(){
		return sensorL.isPressed() || sensorR.isPressed();
	}
	
}
