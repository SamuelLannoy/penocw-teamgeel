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
import field.simulation.FieldSimulation;


public class InfraredSensor {
	private static InfraredSensor infraredSensor;
	private static VirtualRobotConnector connector = VirtualRobotConnector.getInstance(); 
	private static FieldSimulation maze = connector.getMaze();
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
		Boolean isSeesawTile = maze.hasSeesawBorder(maze.getCurrentTile());
		Boolean isOnBarcode = maze.isOnBarcode();
		Boolean isFacedToSeesaw = maze.getBorderInDirection(maze.getCurrentTile(), Direction.fromAngle(connector.getTRotation())).isSeeSawBorder();
		Boolean seesawIsUp = !maze.getBorderInDirection(maze.getCurrentTile(), Direction.fromAngle(connector.getTRotation())).isPassable();
		//System.out.println(isSeesawTile + " " + isOnBarcode + " " + isFacedToSeesaw + " " + seesawIsUp);
		if (isSeesawTile && isOnBarcode && isFacedToSeesaw && seesawIsUp) {
			lastInfrared = 20;
			SensorBuffer.updateInfrared(20);
			return 20;
			//TODO randomize values?
		} else {
			lastInfrared = 0;
			SensorBuffer.updateInfrared(0);
			return 0;
		}
	}
	
	private int getLastInfrared() {
		return lastInfrared;
	}
}