package lightsensor;

import java.util.LinkedList;
import java.util.List;

import org.junit.runners.model.Statement;

import barcode.Barcode;
import barcode.BarcodeParser;

import communication.Buffer;


import robot.Robot;

import lejos.nxt.Button;
import lejos.nxt.SensorPort;

public class LightSensor {
	private static lejos.nxt.LightSensor sensor;
	private static final LightSensor instance = new LightSensor();
	private static int addCounter;
	private static int lastVal;
	
	/**
	 * Creates a new lightsensor as a singleton object.
	 */
	private LightSensor() {
		sensor = new lejos.nxt.LightSensor(SensorPort.S2);
	}
	
	public static LightSensor getInstance(){
		return instance;
	}
	
	public void calibrateLightSensor(){
//		System.out.println("calibratewhite");
//		Button.waitForAnyPress();
//		sensor.calibrateHigh();
//		System.out.println("calibrateblack");
//		Button.waitForAnyPress();
//		sensor.calibrateLow();
//		Buffer.addDebug("high:"+sensor.getHigh() + " low:"+sensor.getLow());
		sensor.setHigh(487);
		sensor.setLow(303);
	}
	
	public int readValue() {
		addCounter++;
		int value = readValueWithoutAdd();
		if(addCounter >= 30*Robot.getInstance().getTravelSpeed()/Robot.DEFAULT_TRAVEL_SPEED) {
			//System.out.println(value);
			Buffer.addLightValue(value);
			addCounter = 0;
		}
		lastVal = value;
		return value;
	}
	
	public int readValueWithoutAdd() {
		return sensor.readValue();
	}
	
	public Color getColor(){
		int value = readValue();
		if(value > 80)
			return Color.WHITE;
		else if(value < 35)
			return Color.BLACK;
		else
			return Color.BROWN;
	}
	
	/**
	 * 
	 * @param 	onlyLines
	 * 			false if you want to scan both barcodes and white lines
	 * 			true if you only want to scan the white lines
	 * @return	Line, Barcode or Unknown barcode.
	 */
	public LightSensorUpdate lightSensorVigilante(boolean onlyLines, boolean turnOnBarcode){
		List<Color> list = new LinkedList<Color>();
		int buffer = 0;
		int emptybuffer = 0;
		int counterBlack = 0;
		Color temp;
		boolean barcodehelper=false;
		boolean alreadyBarcode = false;
		if(!turnOnBarcode){
			Buffer.addDebug("in if");
			System.out.println("in if");
			while (buffer<10 && emptybuffer<10) { // 10 keer bruin na elkaar --> einde lijn of barcode
				if(Robot.getInstance().isMoving()){
					temp = Color.getColor(readValue());
					setLastColor(temp);
					if(temp == Color.BROWN){
						if(list.size()!=0){
							if(!barcodehelper){
								list.add(temp);
								buffer++;
							}
						}
						else{
							if(!barcodehelper)
							emptybuffer++;
						}
					} else{
						if(list.size() == 0){
							if(temp == Color.BLACK){
								Buffer.addDebug("barcodehelperterugfalse");
								barcodehelper=false;
								counterBlack++;
							}
						}
						list.add(temp);
						//test zodat outofmemoryexception zich niet meer voordoet
						if(list.size()>1000)
							break;
						buffer = 0;
						if(temp == Color.BLACK){
							counterBlack++;
							if(counterBlack == 2 && !alreadyBarcode && !onlyLines){
								System.out.println("isScanningBarcode");
								Robot.getInstance().setScanning(true);
								Robot.getInstance().stop();
								int buff = 0;
								Robot.getInstance().backward();
								Color tem;
								while (buff<10){
									tem = Color.getColor(readValue());
									if(tem == Color.BROWN)
										buff++;
									else
										buff=0;
								}
								Robot.getInstance().stop();
								list.clear();
								counterBlack = 0;
								buffer = 0;
								emptybuffer = 0;
								alreadyBarcode = true;
								barcodehelper=true;
								Robot.getInstance().setTravelSpeed(Robot.DEFAULT_TRAVEL_SPEED);
								Robot.getInstance().forward();
							}
						}
						if(temp == Color.WHITE){
							counterBlack = 0;
						}
					}
				}
				else{
					temp = Color.getColor(readValue());
					setLastColor(temp);
					Robot.getInstance().setScanning(false);
				}
				Button.waitForAnyPress((int)(10*Robot.DEFAULT_TRAVEL_SPEED/Robot.getInstance().getTravelSpeed()));
			}
			
			if(emptybuffer == 10){
				return LightSensorUpdate.BROWN;
			}
			
			System.out.println("list size: "+list.size());
			if(list.size() == 0){
				Robot.getInstance().setScanning(false);
				return LightSensorUpdate.BROWN; //is impossible
			}
			else if(list.get(0) == Color.BLACK && !onlyLines){
				Robot.getInstance().stop();
				Robot.getInstance().setTravelSpeed(Robot.speed);
				Buffer.addDebug("--------- Begin Barcode list");
				for(int i =0; i< list.size(); i++){
					Buffer.addDebug(list.get(i).toString());
				}
				Buffer.addDebug("--------- End Barcode list");
				try{
					Barcode barcode = BarcodeParser.convertToBarcode(list);
					if(barcode !=null){
					    Robot.getInstance().sendAndExecuteBarcode(barcode);
					    Robot.getInstance().setScanning(false);
						Buffer.addDebug("ROBOT: update = bar");
						return LightSensorUpdate.BARCODE;
					} 
					else{
						Robot.getInstance().setScanning(false);
						return LightSensorUpdate.UNKNOWN; //not a known barcode, but a complete one
					}
				}
				catch(IllegalArgumentException iae){
					Robot.getInstance().setScanning(false);
					Buffer.addDebug("ROBOT: update = unknown incomplete " + list.get(0) + " " + iae.getMessage());
					return LightSensorUpdate.UNKNOWN; //list size to small OR last and first line not black
				}
				catch(IllegalStateException ise){
					Robot.getInstance().setScanning(false);
					Buffer.addDebug("ROBOT: update = unknown incomplete " + list.get(0) + " " + ise.getMessage());
					return LightSensorUpdate.UNKNOWN; //list size to small OR last and first line not black
				}
				
			}
			else if(list.get(0) == Color.WHITE){
				Robot.getInstance().setScanning(false);
				return LightSensorUpdate.LINE;
			}
			Robot.getInstance().setScanning(false);

			Buffer.addDebug("ROBOT: barcode intercepted");
			return null; //if a barcode is scanned but we dont want to use this information
		}
		else{
			buffer=0;
			Buffer.addDebug("in else");
			System.out.println("in else");
			while(buffer < 10){
				Button.waitForAnyPress(10);
				if(Color.getColor(readValue()).equals(Color.BROWN)){
					buffer++;
				}
				else{
					buffer = 0;
				}
			}
			Robot.getInstance().setOnBrownAfterBarcode(true);
			return LightSensorUpdate.BROWN;
		}

	}
	
	private Color lastColor;

	public Color getLastColor() {
		return lastColor;
	}
	
	public int getLastValue(){
		return lastVal;
	}

	public void setLastColor(Color lastColor) {
		this.lastColor = lastColor;
	}
	
}
