package simulator.touchsensor;

import robot.SensorBuffer;
import simulator.VirtualRobotConnector;
import field.*;
import field.simulation.FieldSimulation;

public class TouchSensor {
	private static TouchSensor touchSensor;
	private static VirtualRobotConnector connector = VirtualRobotConnector.getInstance(); 
	private static FieldSimulation maze = connector.getMaze();
	
	/**
	 * Creates a new touch sensor as a singleton object.
	 */
	private TouchSensor() {
	}
	
	public static TouchSensor getInstance() {
		if(touchSensor == null) {
			return new TouchSensor();
		} else {
			return touchSensor;
		}
	}
	
	public boolean isPressed(){
		boolean isPressed =	
				maze.distanceFromPanel(Direction.TOP) < 11 ||
				maze.distanceFromPanel(Direction.BOTTOM) < 11 ||
				maze.distanceFromPanel(Direction.RIGHT) < 11 ||
				maze.distanceFromPanel(Direction.LEFT) < 11 ;
		
		SensorBuffer.updateTouches(isPressed);

		return isPressed;
	}
	
}
