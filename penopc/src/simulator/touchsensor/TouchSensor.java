package simulator.touchsensor;

import robot.SensorBuffer;
import simulator.VirtualRobotConnector;
import field.*;

public class TouchSensor {
	private static TouchSensor touchSensor;
	private static VirtualRobotConnector connector = VirtualRobotConnector.getInstance(); 
	private static Field maze = connector.getMaze();
	
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
				maze.distanceFromPanel(connector.getTDistanceX(), connector.getTDistanceY(), Direction.TOP) < 0.1 ||
				maze.distanceFromPanel(connector.getTDistanceX(), connector.getTDistanceY(), Direction.BOTTOM) < 0.1 ||
				maze.distanceFromPanel(connector.getTDistanceX(), connector.getTDistanceY(), Direction.RIGHT) < 0.1 ||
				maze.distanceFromPanel(connector.getTDistanceX(), connector.getTDistanceY(), Direction.LEFT) < 0.1 ;
		
		SensorBuffer.updateTouches(isPressed);

		return isPressed;
	}
	
}
