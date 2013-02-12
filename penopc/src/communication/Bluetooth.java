package communication;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;

import robot.DebugBuffer;
import robot.Robot;
import robot.SensorBuffer;

import lejos.pc.comm.*;

/**
 * @author  Samuel
 */
public class Bluetooth {
	private NXTConnector nxtConnector;
	private DataInputStream dataIn;
	private DataOutputStream dataOut;
	
	private static Bluetooth instance;
	
	private static final String NXT_NAME = "Leonard";
	private static final String NXT_ADDRESS = "00:16:53:13:54:1C";
	
	/**
	 * Creates a new Bluetooth object.
	 * Because this is a singleton object, this constructor is defined private.
	 */
	private Bluetooth() {
	}
	
	/**
	 * Method to get an instance of our Bluetooth singleton object.
	 */
	public static synchronized Bluetooth getInstance() {
		if(instance == null)
			instance = new Bluetooth();
		return instance;
	}
	
	/**
	 * This method sets up a connection with the NXT brick.
	 */
	public void setUpConnection() throws NXTCommException {
		boolean connected = false;
		
		nxtConnector = new NXTConnector();
		connected = nxtConnector.connectTo(NXT_NAME, NXT_ADDRESS, NXTCommFactory.BLUETOOTH);
		
		if(!connected) throw new NXTCommException();
	    
		dataIn = new DataInputStream(nxtConnector.getInputStream());
	    dataOut = new DataOutputStream(nxtConnector.getOutputStream());
	}
	
	/**
	 * This method breaks down the connection with the NXT brick.
	 */
	public void breakDownConnection() {
		try {
			nxtConnector.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Send instructions or data to the NXT-brick over the already instantiated bluetooth connection.
	 * 
	 * @param 	code
	 * 			Type of instruction
	 * @param 	param1
	 * @param 	param2
	 * @param 	immediateReturn
	 */
	public void send(int code, double param1, double param2, boolean immediateReturn){
		try{
			dataOut.writeInt(code);
			dataOut.writeDouble(param1);
			dataOut.writeDouble(param2);
			dataOut.writeBoolean(immediateReturn);
			dataOut.flush();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Recieve data about the status of the robot
	 */
	public void receive() {
		try {
			if(dataIn.read(new byte[0], 0, 0) != -1) {
				// Receive data about the movement of the robot
				boolean isMoving = dataIn.readBoolean();
				double movementIncrement = dataIn.readDouble();
				double angleIncrement = dataIn.readDouble();
				
				// Receive flag about scanning barcodes
				boolean isScanning = dataIn.readBoolean();
				
				// Receive flag about centering on tile
				boolean isCentering = dataIn.readBoolean();
				
				// Receive information about start tile and finish tile
				boolean isStartTile = dataIn.readBoolean();
				boolean isFinishTile = dataIn.readBoolean();
				
				if((Double.compare(movementIncrement, Double.NaN) != 0) && (Double.compare(angleIncrement, Double.NaN) != 0))
					Status.update(movementIncrement, angleIncrement, isMoving, isScanning, isCentering, isStartTile, isFinishTile);
				
				// Receive data about the distances to objects
				int numberOfDistances = dataIn.readInt();
				ArrayList<Integer> distances = new ArrayList<Integer>();
				for(int i = 0; i < numberOfDistances; i++) {
					distances.add(dataIn.readInt());
				}
				SensorBuffer.updateDistances(distances);
				
				// Receive data about the distances to objects
				int numberOfDistancesAD = dataIn.readInt();
				ArrayList<Integer> distancesAD = new ArrayList<Integer>();
				for(int i = 0; i < numberOfDistancesAD; i++) {
					distancesAD.add(dataIn.readInt());
				}
				SensorBuffer.updateDistancesAD(distancesAD);
				
				// Receive data about the light sensor values
				int numberOfLightValues = dataIn.readInt();
				ArrayList<Integer> lightValues = new ArrayList<Integer>();
				for(int i = 0; i < numberOfLightValues; i++) {
					lightValues.add(dataIn.readInt());
				}
				SensorBuffer.updateLightValues(lightValues);
				
				// Receive data about whether the touch sensors are pushed
				boolean touches = dataIn.readBoolean();
				SensorBuffer.updateTouches(touches);
				
				// Receive data about light updates
				int numberOfLightUpdates = dataIn.readInt();
				ArrayList<Integer> lightUpdates = new ArrayList<Integer>();
				for(int i = 0; i < numberOfLightUpdates; i++) {
					lightUpdates.add(dataIn.readInt());
				}
				SensorBuffer.updateLightUpdates(lightUpdates);
				
				// Receive barcodes
				int numberOfBarcodes = dataIn.readInt();
				ArrayList<String> barcodes = new ArrayList<String>();
				for(int i = 0; i < numberOfBarcodes; i++) {
					barcodes.add(dataIn.readUTF());
				}
				SensorBuffer.updateBarcodes(barcodes);
				
				// Receive debug information
				int numberOfDebugMsg = dataIn.readInt();
				ArrayList<String> debug = new ArrayList<String>();
				for(int i = 0; i < numberOfDebugMsg; i++) {
					debug.add(dataIn.readUTF());
				}
				DebugBuffer.updateDebuginfo(debug);
			} else {
				System.out.println("No values received.");
			}
		} catch (EOFException e) {
			 breakDownConnection();
			 System.out.println("Verbinding verbroken EOF.");
			//e.printStackTrace();
		} catch (IOException e) {
			 breakDownConnection();
			 System.out.println("Verbinding verbroken IO.");
			//e.printStackTrace();
		}
	}
	
}
