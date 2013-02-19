package communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import barcode.Action;
import barcode.BarcodeAction;

import robot.Robot;
import touchsensor.TouchSensor;
import ultrasonicsensor.UltrasonicSensor;

import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lightsensor.LightSensorVigilante;

public class PilotController {
	
	private BTConnection connection;
	private DataInputStream dataIn;
	private DataOutputStream dataOut;
	
	private int code;
	private double param1;
	private double param2;
	private boolean immediateReturn;
	private static boolean stopStream = false;
	
	public static final int TIME_OUT = 98;
	
	private static PilotController instance;
	
	/**
	 * Constructor
	 */
	private PilotController(){
	}
	
	public synchronized static PilotController getInstance() {
		if(instance == null)
			instance = new PilotController();
		return instance;
	}
	
	public static void main(String args[]){
		PilotController pilot = new PilotController();
		pilot.go();
	}
	
	private LightSensorVigilante lightSensorVigilante = new LightSensorVigilante();
	
	/**
	 * Connect and execute commands from the controller
	 */
	public void go(){
		Robot.getInstance().calibrateLS();
		
		connect();
		
		Thread read = new Thread(new Runnable() {
			public void run() {
				while(true){
					try {
						readData();
						executeCommand();
					} catch (IOException e) {
						LCD.drawString("Conn fail", 0, 0);
					}
				}
			}
		});
		
		Thread write = new Thread(new Runnable() {
			public void run() {
				while(true){
					try {
						writeData();
						Thread.sleep(TIME_OUT);
					} catch (InterruptedException e) {
					}
				}
			}
		});
		
		Thread touchSensorVigilante = new Thread(new Runnable() {
			public void run() {
				while(true){
					Buffer.setTouched(TouchSensor.getInstance().isPressed());
					try {
						Thread.sleep(TIME_OUT);
					} catch (InterruptedException e) {
					}
				}
			}
		});
		
		Thread ultrasonicSensor = new Thread(new Runnable() {
			public void run() {
				while(true){
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
					UltrasonicSensor.getInstance().readValue();
				}
			}
		});
		
		ultrasonicSensor.start();
		read.start();
		write.start();
		lightSensorVigilante.start();
		touchSensorVigilante.start();
	}
	
	public void writeData() {
		try {
			double movementIncrement;
			double angleIncrement;
			boolean isMoving = false;
			
			if(stopStream) {
				movementIncrement = Double.NaN;
				angleIncrement = Double.NaN;
			} else {
				// Read data about movement of the robot
				isMoving = Robot.getInstance().isMoving();
				movementIncrement = Robot.getInstance().getMovementIncrement();
				angleIncrement = Robot.getInstance().getAngleIncrement();
				
				// Reset the movement increment and angle increment after having read this
				Robot.getInstance().resetMovementAngleIncrement();
			}
			
			// Now write it!
			dataOut.writeBoolean(isMoving);
			dataOut.writeDouble(movementIncrement);
			dataOut.writeDouble(angleIncrement);
			
			boolean isStartTile = BarcodeAction.getAction() == Action.REMEMBER_START ? true : false;
			boolean isFinishTile = BarcodeAction.getAction() == Action.REMEMBER_FINISH ? true : false;
			if (isStartTile || isFinishTile) {
				Buffer.addDebug("start " + isStartTile + " finish " + isFinishTile);
			}
			BarcodeAction.resetAction();
			
			// Write a flag that says that the robot is scanning a barcode
			dataOut.writeBoolean(Robot.getInstance().isScanning());
			
			// Write a flag that says that the robot is centering on a tile
			dataOut.writeBoolean(Robot.getInstance().isCentering());
			
			dataOut.writeBoolean(isStartTile);
			dataOut.writeBoolean(isFinishTile);
			
			// Write data about the distance to objects
			ArrayList<Integer> distances = new ArrayList<Integer>();
			synchronized(Buffer.getDistances()) {
				distances.addAll(Buffer.getDistances());
				Buffer.getDistances().clear();
			}
			
			dataOut.writeInt(distances.size());
			for(int pos: distances) {
				dataOut.writeInt(pos);
			}
			
			// Write data about the distance to objects
			ArrayList<Integer> distancesAD = new ArrayList<Integer>();
			synchronized(Buffer.getDistancesAD()) {
				distancesAD.addAll(Buffer.getDistancesAD());
				Buffer.getDistancesAD().clear();
			}
			
			dataOut.writeInt(distancesAD.size());
			for(int pos: distancesAD) {
				dataOut.writeInt(pos);
			}
			
			// Write data about the light sensor values
			ArrayList<Integer> lightValues = new ArrayList<Integer>();
			synchronized(Buffer.getLightValues()) {
				lightValues.addAll(Buffer.getLightValues());
				Buffer.getLightValues().clear();
			}
			
			dataOut.writeInt(lightValues.size());
			for(int val: lightValues) {
				dataOut.writeInt(val);
			}
			
			// Write data about the touch sensor
			dataOut.writeBoolean(Buffer.isTouched());

			// Write data about the light updates
			ArrayList<Integer> lightUpdates = new ArrayList<Integer>();
			synchronized(Buffer.getLightUpdates()) {
				lightUpdates.addAll(Buffer.getLightUpdates());
				Buffer.getLightUpdates().clear();
			}
			
			dataOut.writeInt(lightUpdates.size());
			for(int upd: lightUpdates) {
				dataOut.writeInt(upd);
			}
			
			// Write the barcodes
			ArrayList<String> barcodes = new ArrayList<String>();
			synchronized(Buffer.getBarcodes()) {
				barcodes.addAll(Buffer.getBarcodes());
				Buffer.getBarcodes().clear();
			}
			
			dataOut.writeInt(barcodes.size());
			for(String bc: barcodes) {
				dataOut.writeUTF(bc);
			}
			
			// Write the debug information
			ArrayList<String> debug = new ArrayList<String>();
			synchronized(Buffer.getDebug()) {
				debug.addAll(Buffer.getDebug());
				Buffer.getDebug().clear();
			}
			
			dataOut.writeInt(debug.size());
			for(String bc: debug) {
				dataOut.writeUTF(bc);
			}
			
			dataOut.flush();
		} catch (IOException e) {
			LCD.drawString("Conn fail", 0, 0);
		}
	}

	/**
	 * Executes the (decoded) command sent by the controller and calls the corresponding method.
	 */
	private void executeCommand() {
		if(code>=Encoding.values().length){
			System.out.println("Illegal instruction: "+code);
		}
		else{
			//System.out.println(code);
			Encoding.values()[code].execute(param1, param2, immediateReturn);
		}
	}
	
	/**
	 * Reads dataIn sent from the controller
	 * @throws IOException 
	 */
	private void readData() throws IOException {
			code = dataIn.readInt();
	        param1 = dataIn.readDouble();
	        param2 = dataIn.readDouble();
	        immediateReturn = dataIn.readBoolean();
	}

	/**
	 * Establish bluetooth connection to mission control.
	 * If connection is successful, the robot will beep.
	 */
	private void connect() {
		LCD.clear();
		LCD.drawString("Waiting for connection...", 0, 0);
		connection = Bluetooth.waitForConnection();
		LCD.clear();
		 
		LCD.drawString("Connected!", 0, 0);
		dataIn = connection.openDataInputStream();
		dataOut = connection.openDataOutputStream();
		Sound.beepSequence();
	}
	
	public static void stopStream() {
		stopStream = true;
	}
	
	public static void startStream() {
		stopStream = false;
	}
	
	public void stopLightSensorVigilante() {
		try {
			lightSensorVigilante.wait();
		} catch (InterruptedException e) {
			System.out.println("waitexception");
		}
	}
	
	public void restartLightSensorVigilante() {
		lightSensorVigilante.notify();
		System.out.println("restartvigilante");
	}
	   
	 
}
