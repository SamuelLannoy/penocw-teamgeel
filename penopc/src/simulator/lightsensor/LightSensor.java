package simulator.lightsensor;

import robot.DebugBuffer;
import robot.SensorBuffer;
import simulator.VirtualRobotConnector;
import field.Barcode;
import field.BarcodeType;
import field.Field;

public class LightSensor {
	private static LightSensor lightSensor;
	private static VirtualRobotConnector connector = VirtualRobotConnector.getInstance(); 
	private static Field maze = connector.getMaze();
	
	/**
	 * Creates a new light sensor as a singleton object.
	 */
	private LightSensor() {
	}
	
	public static LightSensor getInstance() {
		if(lightSensor == null) {
			return new LightSensor();
		} else {
			return lightSensor;
		}
	}
	
	public void calibrateLightSensor(){
		
	}
	
	public Color getColor(){
		if (maze.isOnWhiteBorder(connector.getTDistanceX(), connector.getTDistanceY()))
			return Color.WHITE;
		else 
			return Color.BROWN;
	}
	
	private Color lastColor = Color.BROWN;
	
	private Color getLastColor() {
		readValue();
		return lastColor;
	}
	private int lastVal = 63;
	private int counter;
	private int hoeveel;
	private boolean wasOnBarcode = false;
	private Barcode lastBarcode;
	public int readValue(){
		int randomness = !connector.isMoving() ? (int)(Math.random()*2) : ((int)(Math.random()*7) < 6 ? (int)(Math.random()*6) : (int)(Math.random()*15));
		int posneg = Math.random() > 0.5 ? 1 : -1;
		int value=0;
		if(randomness>7)
			System.out.println(hoeveel++);
		if(connector.isMoving()){
			if (maze.isOnWhiteBorder(connector.getTDistanceX(), connector.getTDistanceY())){
				if (!maze.isOnBarcode(connector.getTDistanceX(), connector.getTDistanceY())) {
						wasOnBarcode = false;
				}
				value =  100 + posneg * randomness;
				lastColor = Color.WHITE;
				//System.out.println("1 " + "wit");
			}
			else if (maze.isOnBarcode(connector.getTDistanceX(), connector.getTDistanceY())){
				DebugBuffer.addInfo("ON BARCODE");
				if (!wasOnBarcode) {
					wasOnBarcode = true;
					SensorBuffer.addLightUpdate(1);
					Barcode bc = maze.getTileMap().getObjectAtId(Field.convertToTilePosition(connector.getTDistanceX(), connector.getTDistanceY())).getBarcode();
					DebugBuffer.addInfo("code " + bc.getDecimal());
					if (bc.getDecimal() <= 7) {
						SensorBuffer.addBarcodeType(BarcodeType.OBJECT.toString());
						if ((bc.getDecimal() % 4) == connector.getObjectNr()) {
							DebugBuffer.addInfo("TEST");
							if (!connector.hasBall()) {
								//connector.turnLeft(180);
								//connector.setSimAngle((connector.getTRotation() + 180) % 360);
								connector.setHasBall(true);
								
								/*connector.moveForward(60);
								connector.moveBackward(10);
								connector.turnLeft(180);
								connector.moveForward(20);*/
							}
							if (bc.getDecimal() <= 3) {
								connector.setTeamNr(0);
							} else if (bc.getDecimal() <= 7) {
								connector.setTeamNr(1);
							}
						}
					} else if (bc.getDecimal() >= 55){
						SensorBuffer.addBarcodeType(BarcodeType.CHECKPOINT.toString());
					} else if (bc.getDecimal() > 7 && bc.getDecimal() < 55) {
						SensorBuffer.addBarcodeType(BarcodeType.SEESAW.toString());
					} else {
						SensorBuffer.addBarcodeType(BarcodeType.ILLEGAL.toString());
					}
					SensorBuffer.addBarcode(bc.toString());
					
				}
				if (maze.isOnBlack(connector.getTDistanceX(), connector.getTDistanceY())) {
					value =  15 + posneg * randomness;
					//lastColor = Color.BLACK;
					lastColor = Color.BROWN;
				} else {
					value =  100 + posneg * randomness;
					//lastColor = Color.WHITE;
					lastColor = Color.BROWN;
				}
			}
			else{
				wasOnBarcode = false;
				value = 63 + posneg * randomness;
				lastColor = Color.BROWN;
			}
			lastVal = value;
		}
		else
			value = lastVal + posneg*randomness;
		
		counter++;
		if (counter >= 10){
			SensorBuffer.addLightValue(value);
			counter = 0;
		}
		//if (Math.random() > 0.01) SensorBuffer.addLightValue(value);
		return value;
		}
	
	private static boolean wasWhite;
	
	public LightSensorUpdate lightSensorVigilanteLoop() {
		//while(!maze.isOnWhiteBorder(connector.getTDistanceX(), connector.getTDistanceY())){
		while (getLastColor() == Color.BROWN) {
			waitX();
			//System.out.println("1 " + lastColor);
		}
		//while(maze.isOnWhiteBorder(connector.getTDistanceX(), connector.getTDistanceY())){
		while (getLastColor() == Color.WHITE) {
			waitX();
			//System.out.println("2 " + lastColor);
		}
		System.out.println("line");
		return LightSensorUpdate.LINE;
		//SensorBuffer.addLightUpdate(LightSensorUpdate.LINE.ordinal());
	}
	
	private void waitX() {
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/*is zonder barcodes gezien barcodes blijkbaar nog geen posities hebben binnen tiles*/
	public void lightSensorVigilante(){
		/*while(!maze.isOnWhiteBorder(connector.getTDistanceX(), connector.getTDistanceY())){try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}
		while(maze.isOnWhiteBorder(connector.getTDistanceX(), connector.getTDistanceY())){try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}
		SensorBuffer.addLightUpdate(LightSensorUpdate.LINE.ordinal());
		System.out.println("line");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		//return LightSensorUpdate.LINE;
		//System.out.println("ww " + wasWhite);
			if (maze.isOnWhiteBorder(connector.getTDistanceX(), connector.getTDistanceY())) {
				wasWhite = true;
				//System.out.println("white");
			}
			if (wasWhite && !maze.isOnWhiteBorder(connector.getTDistanceX(), connector.getTDistanceY())) {
				SensorBuffer.addLightUpdate(LightSensorUpdate.LINE.ordinal());
				//System.out.println("line");
				wasWhite = false;
			}
		}
}