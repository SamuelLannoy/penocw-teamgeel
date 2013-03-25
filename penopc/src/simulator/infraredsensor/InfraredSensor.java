package simulator.infraredsensor;

import communication.SeesawStatus;

import robot.DebugBuffer;
import robot.SensorBuffer;
import simulator.VirtualRobotConnector;
import field.Barcode;
import field.BarcodeType;
import field.Border;
import field.Direction;
import field.Field;
import field.SeesawBorder;
import field.Tile;


public class InfraredSensor {
	private static InfraredSensor infraredSensor;
	private static VirtualRobotConnector connector = VirtualRobotConnector.getInstance(); 
	private static Field maze = connector.getMaze();
	private int lastInfrared = 0;
	
	/**
	 * Creates a new infrared sensor as a singleton object.
	 */
	private InfraredSensor() {
	}
	
	public static InfraredSensor getInstance() {
		if(infraredSensor == null) {
			return new InfraredSensor();
		} else {
			return infraredSensor;
		}
	}
	
	public int getInfrared(){
		Boolean isSeesawTile = maze.isSeesawTile(maze.getCurrentTile(connector.getTDistanceX(), connector.getTDistanceY()));
		Boolean isOnBarcode = maze.isOnBarcode(connector.getTDistanceX(), connector.getTDistanceY());
		Boolean isFacedToSeesaw = maze.getBorderInDirection(maze.getCurrentTile(connector.getTDistanceX(), connector.getTDistanceY()), Direction.fromAngle(connector.getTRotation())).isSeeSawBorder();
		Boolean seesawIsUp = !maze.getBorderInDirection(maze.getCurrentTile(connector.getTDistanceX(), connector.getTDistanceY()), Direction.fromAngle(connector.getTRotation())).isPassable();
		if (isSeesawTile && isOnBarcode && isFacedToSeesaw && seesawIsUp) {
			lastInfrared = 20;
			return 20;
			//TODO randomize values?
		} else {
			lastInfrared = 0;
			return 0;
		}
	}
	
	private int getLastInfrared() {
		return lastInfrared;
	}
}