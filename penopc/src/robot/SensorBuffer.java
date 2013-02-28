package robot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SensorBuffer {
	
	/**
	 * ULTRASONIC SENSOR
	 */
	
	// Data from the four directions
	
	
	public static List<Integer> lastDist = Collections.synchronizedList(new ArrayList<Integer>());
	
	private static List<Integer> distances = Collections.synchronizedList(new ArrayList<Integer>());
	
	public static void updateDistances(ArrayList<Integer> distances) {
		for(int i = 0; i < distances.size(); i++) {
			SensorBuffer.distances.add(distances.get(i));
		}
		if (distances.size() > 0)
			canClear = false;
	}
	
	private static boolean canClear = false;
	
	public static boolean canClear() {
		return canClear;
	}
	
	public static void setClear(boolean clear) {
		canClear = clear;
	}
	
	public static void addDistance(int distance) {
		distances.add(distance);
	}
	
	public static List<Integer> getDistances() {
		return distances;
	}
	
	// Data from all directions
	
	private static List<Integer> distancesAD = Collections.synchronizedList(new ArrayList<Integer>());
	
	public static void updateDistancesAD(ArrayList<Integer> distances) {
		for(int i = 0; i < distances.size(); i++) {
			SensorBuffer.distancesAD.add(distances.get(i));
		}
	}
	
	public static void addDistanceAD(int distance) {
		distancesAD.add(distance);
	}
	
	public static List<Integer> getDistancesAD() {
		return distancesAD;
	}
	
	/**
	 * LIGHT SENSOR
	 */
	private static List<Integer> lightValues = Collections.synchronizedList(new ArrayList<Integer>());
	
	public static void updateLightValues(ArrayList<Integer> lightValues) {
		for(int i = 0; i < lightValues.size(); i++) {
			SensorBuffer.lightValues.add(lightValues.get(i));
		}
	}
	
	public static void addLightValue(int value) {
		lightValues.add(value);
	}
	
	public static List<Integer> getLightValues() {
		return lightValues;
	}
	
	/**
	 * LIGHT UPDATES
	 */
	private static List<Integer> lightUpdates = Collections.synchronizedList(new ArrayList<Integer>());
	
	public static void updateLightUpdates(ArrayList<Integer> lightUpdates) {
		for(int i = 0; i < lightUpdates.size(); i++) {
			SensorBuffer.lightUpdates.add(lightUpdates.get(i));
			DebugBuffer.addInfo("lightupdate: " + lightUpdates.get(i));
		}
	}
	
	public static void addLightUpdate(int update) {
		lightUpdates.add(update);
		DebugBuffer.addInfo("lightupdate: " + update);
	}
	
	public static List<Integer> getLightUpdates() {
		return lightUpdates;
	}
	
	/**
	 * BARCODES
	 */
	
	private static List<String> barcodes = Collections.synchronizedList(new ArrayList<String>());
	
	public static void updateBarcodes(ArrayList<String> barcodes) {
		for(int i = 0; i < barcodes.size(); i++) {
			SensorBuffer.barcodes.add(barcodes.get(i));
		}
	}
	
	public static List<String> getBarcodes() {
		return barcodes;
	}
	
	private static List<String> barcodetypes = Collections.synchronizedList(new ArrayList<String>());
	
	public static void updateBarcodeTypes(ArrayList<String> barcodetypes) {
		if (barcodetypes.size() > 0) {
			System.out.println("test");
		}
		for(int i = 0; i < barcodetypes.size(); i++) {
			SensorBuffer.barcodetypes.add(barcodetypes.get(i));
		}
	}
	
	public static List<String> getBarcodeTypes() {
		return barcodetypes;
	}
		
	/**
	 * TOUCH SENSOR
	 */
	private static boolean touches = false;

	public static void updateTouches(boolean touches) {
		SensorBuffer.touches = touches;
	}

	public static boolean getTouched() {
		return touches;
	}
	
}
