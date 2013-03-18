package ultrasonicsensor;

import robot.Robot;
import communication.Buffer;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;

public class UltrasonicSensor {
	private static lejos.nxt.UltrasonicSensor sensor;
	private static final int dangerousDistance = 50; //TODO 5 cm? 
	private static final double[] correction = {5,2.5,-1,1.5};
	private static final int MOTOR_CORRECTION = 10;
	
	private static final UltrasonicSensor instance = new UltrasonicSensor();
	
	/**
	 * Creates a new ultrasonic sensor as a singleton object.
	 */
	private UltrasonicSensor() {
		sensor = new lejos.nxt.UltrasonicSensor(SensorPort.S3);
		Motor.A.setAcceleration(800);
		Motor.A.setSpeed(250); //0,5 rev/sec
		sensor.continuous();
	}
	
	public static UltrasonicSensor getInstance() {
		return instance;
	}
	
	public void rotate(int angle){
		if(angle < 0)
			Motor.A.rotate(angle);
		else
			Motor.A.rotate(angle);
		sensor.reset();
	}
	
	public double getLeft() {
		int degreesTurned = Motor.A.getTachoCount()%360; //altijd positief
		if(degreesTurned != 0){
			rotate(360-degreesTurned);
		}
		rotate(-90);
		double result = readValue();
		rotate(90);
		return result + correction[1];
	}
	
	public double getRight() {
		int degreesTurned = Motor.A.getTachoCount()%360; //altijd positief
		if(degreesTurned != 0){
			rotate(360-degreesTurned);
		}
		rotate(90);
		double result = readValue();
		rotate(-90);
		return result + correction[3];
	}
	
	public double getDistanceAhead(){
		//lookAhead();
		return readValue()+correction[0];
	}
	
	public void lookAhead(){
		int degreesTurned = Motor.A.getTachoCount()%360; //altijd positief
		if(degreesTurned != 0){
			rotate(360-degreesTurned);
		}
	}
	
	public boolean obstacleAhead(){
		if(readValue() < dangerousDistance){
			return true;
		}
		return false;
	}
	
	/*public int getDistanceToObstacle(){
		sensor.ping();
		Button.waitForAnyPress(100);
		int distance = sensor.getDistance();
		Buffer.addDistance(distance);
		return distance;
	}
	
	public int getDistanceToObstacleNoBuffer(){
		sensor.ping();
		Button.waitForAnyPress(100);
		int distance = sensor.getDistance();
		return distance;
	}*/
	
	int lastVal = 0;
	//int counter = 0;
	
	public int getLastVal() {
		return lastVal;
	}
	
	public int readValue() {
		sensor.continuous();
		int distance = sensor.getDistance();
		lastVal = distance;
		//System.out.println("d " + distance);
		//counter++;
		//if (counter == 20) {
			//Buffer.addDistance(distance);
			//counter = 0;
		//}
		return distance;
	}
	
	/**
	 * result[0] = ahead
	 * result[1] = left
	 * result[2]= behind
	 * result[3]= right
	 */
	/*public double[] getDistancesAroundRobot(){
		double[] distances = new double[4];
		//lookAhead();
		for(int i=0; i<4; i++){
			//distances[i] = getDistanceToObstacle() + correction[i];
			distances[i] = readValue();
			if(i!=3){
				rotate(90);
			}
			Button.waitForAnyPress(50);
		}
		rotate(-270);
		return distances;
	}*/
	
	/*public double[] roundScan(int n){
		double[] distances = new double[n];
		//lookAhead();
		for(int i=0; i<n; i++){
			//sensor.ping();
			//distances[i] = sensor.getDistance();
			distances[i] = readValue();
			if(i!=n-1){
				rotate(360/n);
			}
			Button.waitForAnyPress(50);
		}
		rotate(-360/n*(n-1));
		return distances;
	}*/
	
	public double[] roundScanRobotTurns(int n){
		if(n == 0){
			throw new IllegalArgumentException();
		}
		double[] distances = new double[n];
		for(int i=0; i<n; i++){
			distances[i] = readValue();
			Buffer.addDistanceAD((int)Math.round(distances[i]));
			Robot.getInstance().rotateLeft(360/n,false);
			Button.waitForAnyPress(50);
		}
		return distances;
	}

	public double[] newTileScan() {
		double[] distances = new double[4];
		for(int i=0; i<4; i++){
			if (i != 2){
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				double v1 = lastVal;
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				double v2 = lastVal;
				double max = Math.max(v1, v2);
				double min = Math.min(v1, v2);
				distances[i] = max * .7 + min * .3;// + correction[i];
				Buffer.addDistance((int)distances[i]);
			} else {
				Buffer.addDistance(-1);
			}
			switch (i) {
				case 0:
					rotate(100);
					break;
				case 1:
					rotate(-200);
					break;
			}
		}
		rotate(100);
		return distances;
	}
	
	public double[] checkScan() {
		double[] distances = new double[4];
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		double v1 = lastVal;
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		double v2 = lastVal;
		double max = Math.max(v1, v2);
		double min = Math.min(v1, v2);
		distances[0] = max * .7 + min * .3;// + correction[i];
		Buffer.addDistance((int)distances[0]);
		Buffer.addDistance(-1);
		Buffer.addDistance(-1);
		Buffer.addDistance(-1);
		return distances;
	}
	
}