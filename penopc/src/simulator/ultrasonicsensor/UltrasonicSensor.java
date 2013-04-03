package simulator.ultrasonicsensor;

import java.util.Random;

import simulator.VirtualRobotConnector;
import field.Direction;
import field.Field;
import field.simulation.FieldSimulation;
import robot.SensorBuffer;

public class UltrasonicSensor {
	private static UltrasonicSensor ultrasonicSensor;
	private static final int dangerousDistance = 50;
	private Orientation orientation;
	private static VirtualRobotConnector connector = VirtualRobotConnector.getInstance(); 
	private static FieldSimulation maze = connector.getMaze();
	private Random randomGenerator = new Random();
	
	private static final double MEAN = -3.79;
	private static final double DEVIATION = 6.29;
	
	/**
	 * Creates a new ultrasonic sensor as a singleton object.
	 */
	private UltrasonicSensor() {

	}
	
	public static UltrasonicSensor getInstance() {
		if(ultrasonicSensor == null) {
			return new UltrasonicSensor();
		} else {
			return ultrasonicSensor;
		}
	}
	
	public Orientation getOrientation() {
		return orientation;
	}

	protected void setOrientation(Orientation orientation) {
		this.orientation = orientation;
	}
	
	public void lookLeft() {
		setOrientation(Orientation.LEFT);
	}
	
	public void lookRight() {
		setOrientation(Orientation.RIGHT);
	}
	
	public void lookForward() {
		setOrientation(Orientation.FORWARD);
	}
	
	public void lookBackward() {
		setOrientation(Orientation.BACKWARD);
	}
	
	public boolean obstacleAhead(){
		if(getDistanceToObstacle() < dangerousDistance){
			return true;
		}
		return false;
	}
	
	public int getDistanceToObstacle(){
		double randomValue = randomGenerator.nextGaussian() * DEVIATION + MEAN ;
		int distance = 0;
		
		Direction dir = null;
		
		switch(orientation) {
			case FORWARD:
				dir = Direction.fromAngle(connector.getTRotation());
				distance = (int) (maze.distanceFromPanel(connector.getTDistanceX(), connector.getTDistanceY(), dir));
				break;
			case BACKWARD:
				dir = Direction.fromAngle(connector.getTRotation() + 180);
				distance = (int) (maze.distanceFromPanel(connector.getTDistanceX(), connector.getTDistanceY(), dir));
				break;
			case LEFT:
				dir = Direction.fromAngle(connector.getTRotation() - 90);
				distance = (int) (maze.distanceFromPanel(connector.getTDistanceX(), connector.getTDistanceY(), dir));
				break;
			case RIGHT:
				dir = Direction.fromAngle(connector.getTRotation() + 90);
				distance = (int) (maze.distanceFromPanel(connector.getTDistanceX(), connector.getTDistanceY(), dir));
				break;
		}
		
		if (distance > 20) {
			distance += randomValue;
		}
		
		if(distance < 0) {
			distance = 0;
		}
		
		SensorBuffer.addDistance(distance);
		return distance;
	}
	
	public int[] scanForWalls() {
		int[] distances = new int[4];
		
		lookForward();
		distances[0] = getDistanceToObstacle();
		lookLeft();
		distances[1] = getDistanceToObstacle();
		lookBackward();
		distances[2] = getDistanceToObstacle();
		lookRight();
		distances[3] = getDistanceToObstacle();
		lookForward();
		
		return distances;
	}
	
	boolean test = false;
	
	public int[] newTileScan() {
		//System.out.println("scan command received");
		int[] distances = new int[4];
		
		lookForward();
		distances[0] = getDistanceToObstacle();
		lookLeft();
		distances[1] = getDistanceToObstacle();
		lookBackward();
		distances[2] = -1;
		SensorBuffer.addDistance(-1);
		lookRight();
		distances[3] = getDistanceToObstacle();
		lookForward();
		
		return distances;
	}
	
	public int[] checkScan() {
		//System.out.println("scan command received");
		int[] distances = new int[4];
		
		lookForward();
		distances[0] = getDistanceToObstacle();
		lookLeft();
		distances[1] = -1;
		SensorBuffer.addDistance(-1);
		lookBackward();
		distances[2] = -1;
		SensorBuffer.addDistance(-1);
		lookRight();
		distances[3] = -1;
		SensorBuffer.addDistance(-1);
		lookForward();
		
		return distances;
	}


}
