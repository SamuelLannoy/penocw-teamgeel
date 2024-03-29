package communication;

import java.util.ArrayList;

import robot.Robot;

public class Buffer {
	
	/**
	 * ULTRASONIC SENSOR
	 */
	
	// Data from the four directions
	private static ArrayList<Integer> distances = new ArrayList<Integer>();

	public static ArrayList<Integer> getDistances() {
		return distances;
	}
	
	public static void addDistance(int distance) {
		synchronized(distances) {
			distances.add(distance);
		}
	}

	// Data from all directions
	private static ArrayList<Integer> distancesAD = new ArrayList<Integer>();
	
	public static ArrayList<Integer> getDistancesAD() {
		return distancesAD;
	}
	
	public static void addDistanceAD(int distance) {
		synchronized(distancesAD) {
			distancesAD.add(distance);
		}
	}
	
	/**
	 * LIGHT SENSOR
	 */
	private static ArrayList<Integer> lightValues = new ArrayList<Integer>();
	
	public static ArrayList<Integer> getLightValues() {
		return lightValues;
	}
	
	public static void addLightValue(int value) {
		synchronized(lightValues) {
			lightValues.add(value);
		}
	}
	
	
	/**
	 * LIGHT UPDATES
	 */
	private static ArrayList<Integer> lightUpdates = new ArrayList<Integer>();
	
	public static ArrayList<Integer> getLightUpdates() {
		return lightUpdates;
	}
	
	public static void addLightUpdate(int update) {
		synchronized(lightUpdates) {
			lightUpdates.add(update);
		}
	}
	
	/**
	 * BARCODE
	 */
	private static ArrayList<String> barcodes = new ArrayList<String>();
	
	public static ArrayList<String> getBarcodes() {
		return barcodes;
	}
	
	public static void addBarcode(String barcode) {
		synchronized(barcodes) {
			barcodes.add(barcode);
		}
	}
	
	private static ArrayList<String> barcodetypes = new ArrayList<String>();
	
	public static ArrayList<String> getBarcodetypes() {
		return barcodetypes;
	}
	
	public static void addBarcodeType(String barcodeType) {
		synchronized(barcodetypes) {
			barcodetypes.add(barcodeType);
		}
	}
	
	/**
	 * TOUCH SENSOR
	 */
	private static boolean touched = false;
	
	public static boolean isTouched() {
		return touched;
	}
	
	public static void setTouched(boolean touched) {
		Buffer.touched = touched;
	}
	
	/**
	 * INFRARED SENSOR
	 */
	private static int infrared;
	
	public static int getInfrared(){
		return infrared;
	}
	
	public static void setInfrared(int infrared) {
		Buffer.infrared = infrared;
	}
	
	/**
	 * DEBUG
	 */
	private static ArrayList<String> debug = new ArrayList<String>();
	
	public static ArrayList<String> getDebug() {
		return debug;
	}
	
	public static void addDebug(String debugUpdate) {
		synchronized(debug) {
			debug.add(debugUpdate);
		}
	}
	
	/**
	 * HASBALL
	 */
	private static boolean hasBall;
	
	public static boolean hasBall(){
		return Robot.getInstance().hasBall();
	}
	
	public static void setHasBall(){
		Buffer.hasBall = true;
	}
	
	/**
	 * SETTEAMNR
	 */
	
	public static boolean isTeamKnown(){
		return Robot.getInstance().getTeamNr() != -1;
	}
	
	public static int getTeamNr(){
		return Robot.getInstance().getTeamNr();
	}
	
	/**
	 * SEESAW STATUS
	 */
	
	private static SeesawStatus status;
	
	public static SeesawStatus getSeesawStatus(){
		return status;
	}
	
	public static void setSeesawStatus(SeesawStatus stat){
		status = stat;
	}
	
	
}
