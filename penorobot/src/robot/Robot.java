package robot;

import barcode.Barcode;

import communication.Buffer;
import communication.PilotController;

import ultrasonicsensor.UltrasonicSensor;
import lejos.nxt.*;
import lightsensor.Color;
import touchsensor.TouchSensor;
import lightsensor.*;
import lightsensor.LightSensor;

/**
 * @author  Samuel
 */
public class Robot {
		
		private static final robot.DifferentialPilot PILOT = new robot.DifferentialPilot(54.4, 54.3, 161.6497, Motor.B, Motor.C, false);
		private static Robot instance = new Robot();
		private boolean isScanning;
		private boolean isCentering;
		private LightSensor lightSensor = LightSensor.getInstance();
		private TouchSensor touchSensor = TouchSensor.getInstance();
		private String ourBarcode;
		
		public static final int DEFAULT_TRAVEL_SPEED = 150;
		public static final int DEFAULT_ROTATE_SPEED = 50;
		public static final int DEFAULT_ACCELERATION = 500;
		
		public static double speed;

		private Robot() {
			PILOT.setTravelSpeed(DEFAULT_TRAVEL_SPEED);
			PILOT.setRotateSpeed(DEFAULT_ROTATE_SPEED);
			PILOT.setAcceleration(DEFAULT_ACCELERATION);
			speed = DEFAULT_TRAVEL_SPEED;
		}

		public static Robot getInstance(){
			return instance;
		}
		
		public static double getSpeed(){
			return speed;
		}
			
		public void forward(){
			PILOT.forward();
		}
		
		public void backward(){
			PILOT.backward();
		}

		public void rotateLeft() {
			PILOT.rotateLeft();
		}
		
		public void rotateRight() {
			PILOT.rotateRight();
		}
				
		public void rotateLeft(double angle, boolean immediateReturn){
			PILOT.rotate(angle, immediateReturn);
		}
		
		public void rotateRight(double angle, boolean immediateReturn){
			PILOT.rotate(-angle, immediateReturn);
		}
		
		public void travel(double distance, boolean immediateReturn){
			PILOT.travel(distance, immediateReturn);
		}
		
		public void stop(){
			PILOT.stop();
		}
		
		public void setTravelSpeed(double travelSpeedInUnitsPerSecond){
			PILOT.setTravelSpeed(travelSpeedInUnitsPerSecond);
		}
		
		public void setRotateSpeed(double rotateSpeedInDegreesPerSecond){
			PILOT.setRotateSpeed(rotateSpeedInDegreesPerSecond);
		}
		
		public boolean isMoving(){
			return PILOT.isMoving();
		}
		
		public float getMovementIncrement(){
			return PILOT.getMovementIncrement();
		}
		
		public float getAngleIncrement(){
			return PILOT.getAngleIncrement();
		}
		
		public double getTravelSpeed() {
			return PILOT.getTravelSpeed();
		}
		
		public void resetMovementAngleIncrement() {
			PILOT.reset();
		}
		
		public void calibrateLS() {
			lightSensor.calibrateLightSensor();
		}
		
		public LightSensor getLightSensor(){
			return lightSensor;
		}
		
		private void orientScan(){
			double[] distances = UltrasonicSensor.getInstance().roundScanRobotTurns(20);
			double max = -1;
			int direction = -1;
			for(int i = 0; i < distances.length; i++){
				double sum = distances[(i-1+distances.length) % distances.length]+distances[i]+distances[(i+1)%distances.length];
				if(sum > max){
						max = sum;
						direction =i;
				}
			}
			Robot.getInstance().rotateLeft(360/20*direction, false);
		}
		
		private void setCentering(boolean center){
			isCentering = center;
		}
		
		public boolean isCentering(){
			return isCentering;
		}
		
		public void orientOnWhiteLine(boolean scan) {
			setNoLines(true);
			if(scan){
				orientScan();
			}

			double previousTravelSpeed = PILOT.getTravelSpeed();
			double previousRotateSpeed = PILOT.getRotateSpeed();
			
			PILOT.setTravelSpeed(100);
			PILOT.setRotateSpeed(30);
			
			Robot.getInstance().forward();
			int buffer = 0;
			
			//Buffer.addDebug("Begin brown");
			while (buffer<40) {
				//Buffer.addDebug(lightSensor.getLastColor().toString());
				//Buffer.addDebug("buffer size: "+buffer);

				if (lightSensor.getLastColor() == Color.BROWN)
						buffer++;
				else 
					buffer = 0;
			}
			
			buffer = 0;
			
			//Buffer.addDebug("Begin white");
			while (buffer<15) {
				if (lightSensor.getLastColor() == Color.WHITE)
						buffer++;
				else if (lightSensor.getLastColor() == Color.BROWN){
					buffer = 0;
				}
				if(touchSensor.isPressed()){
					PILOT.travel(-30);
					orientScan();
					Robot.getInstance().forward();
				}	
			}
			
			buffer = 0;
			
			//Buffer.addDebug("Begin brown");
			while (buffer<200) {
				//Buffer.addDebug(lightSensor.getLastColor().toString());
				//Buffer.addDebug("buffer size: "+buffer);

				if (lightSensor.getLastColor() == Color.BROWN)
						buffer++;
				else 
					buffer = 0;
			}
			
			buffer = 0;
			Robot.getInstance().stop();
			PilotController.stopStream();
			PILOT.reset();
			PILOT.rotateRight();
			buffer = 0;
			//Buffer.addDebug("Begin white 2");
			while (buffer<25) {
				//Buffer.addDebug(lightSensor.getLastColor().toString());
				//Buffer.addDebug("buffer size: "+buffer);

				if (lightSensor.getLastColor() == Color.WHITE)
						buffer++;
				else
					buffer = 0;
			}
			buffer = 0;
			PILOT.stop();
			PILOT.rotate(10, false);
			PILOT.reset();
			PILOT.rotateLeft();
			
			//Buffer.addDebug("Begin brown 2");
			while (buffer<150) {
				//Buffer.addDebug(lightSensor.getLastColor().toString());
				//Buffer.addDebug("buffer size: "+buffer);


				if (lightSensor.getLastColor() == Color.BROWN)
						buffer++;
				else buffer = 0;
			}
			buffer = 0;
			
			//Buffer.addDebug("Begin white 3");
			while (buffer<25) {
				//Buffer.addDebug(lightSensor.getLastColor().toString());
				//Buffer.addDebug("buffer size: "+buffer);


				if (lightSensor.getLastColor() == Color.WHITE)
						buffer++;
				else 
					buffer = 0;
			}
			PILOT.stop();
			double arc2 = PILOT.getAngleIncrement();
			PILOT.reset();
			PILOT.rotate(-((arc2 + 10) / 2));
			
			Robot.getInstance().backward();
			buffer = 0;
			
			//Buffer.addDebug("Begin white 4");
			while (buffer<25) {
				//Buffer.addDebug(lightSensor.getLastColor().toString());
				//Buffer.addDebug("buffer size: "+buffer);


				if (lightSensor.getLastColor() == Color.WHITE)
						buffer++;
				else 
					buffer = 0;
			}
			Robot.getInstance().stop();
			Robot.getInstance().travel(75, false);
			Robot.getInstance().stop();
			setNoLines(false);
			Buffer.addLightUpdate(LightSensorUpdate.LINE.ordinal());
			
			PILOT.setRotateSpeed(previousRotateSpeed);
			PILOT.setTravelSpeed(previousTravelSpeed);
			PilotController.startStream();
		}
		
		
		public double[] newTileScan() {
			return UltrasonicSensor.getInstance().newTileScan();
		}
		
		public double[] checkScan() {
			return UltrasonicSensor.getInstance().checkScan();
		}

		public double[] scanForWalls(){
			double[] distances = new double[4];
			for(int i=0; i<4; i++){
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				double v1 = UltrasonicSensor.getInstance().getLastVal();
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				double v2 = UltrasonicSensor.getInstance().getLastVal();
				double max = Math.max(v1, v2);
				double min = Math.min(v1, v2);
				distances[i] = max * .7 + min * .3;
				if(i!=3){
					rotateLeft(90, false);
				}
			}
			rotateLeft(90, false);
			for (int i = 0; i < 4; i++) {
				Buffer.addDistance((int)distances[i]);
			}
			return distances;
			//return UltrasonicSensor.getInstance().getDistancesAroundRobot();
//			if(getGrid() == null){
//				return determineGrid();
//			}
//			else{
//				return defaultScanForWalls();
//			}
		}
		
		public void arc(double radius, double angle, boolean immediateReturn){
			PILOT.arc(radius, angle, immediateReturn);
		}
		
		public void setOnCenterTile(){
			Robot.getInstance().setCentering(true);
			orientOnWhiteLine(true);
			this.travel(-200, false);
			//double distances[] = UltrasonicSensor.getInstance().newTileScan();
			double distances[] = UltrasonicSensor.getInstance().roundScanRobotTurns(4);
			boolean wallInSomeDirection = false;
			int direction = -1;
			int maxi = -1;
			double maxDistance = -1;
			for(int i=1; i<distances.length; i++){
				System.out.println("distance i: "+distances[i]);
				if(i != 2) {
					if(distances[i]<20){
						if(wallInSomeDirection){
							if(distances[direction] > distances[i]){
								direction = i;
							}
						}
						else{
							wallInSomeDirection = true;
							direction = i;
						}
					}
					if(distances[i]> maxDistance){
						maxi = i;
						maxDistance = distances[i];
					}
				}
			}
			if(wallInSomeDirection){ // walls in current Tile
				Robot.getInstance().rotateLeft(360/4*direction,false);
				Robot.getInstance().forward();
				while(!touchSensor.isPressed()){System.out.println("waiting to touch wall.");};
				System.out.println("isTouched!");
				Robot.getInstance().stop();
				System.out.println("stopped.");
				Robot.getInstance().travel(-100, false);
				System.out.println("end of setCenter before turns");
				Robot.getInstance().rotateLeft(-360/4*direction,false);
				System.out.println("The End 1");
			}
			else{ // no walls in current Tile
				Robot.getInstance().rotateLeft(360/4*maxi,false);
				System.out.println("noWallsOnCurrTile --> orientWhiteLine");
				Robot.getInstance().orientOnWhiteLine(false);
				System.out.println("endOfOrient in center method");
				Robot.getInstance().travel(-200, false);
				Robot.getInstance().rotateLeft(-360/4*maxi,false);
				System.out.println("The End 2");
			}
			Robot.getInstance().setCentering(false);
		}

		public void sendAndExecuteBarcode(Barcode barcode) {
			barcode.execute();
			sendBarcode(barcode);
		}
		
		public void sendBarcode(Barcode barcode) {
			Buffer.addBarcode(barcode.getCode());
		}

		public boolean isScanning() {
			return isScanning;
		}

		public void setScanning(boolean isScanning) {
			this.isScanning = isScanning;
		}

		public void scanOnlyLines(boolean flag) {
			if(flag == true)
				LightSensorVigilante.scanOnlyLines();
			else
				LightSensorVigilante.scanBarcodesAndLines();
		}
	
		private boolean noLines;

		public boolean isNoLines() {
			return noLines;
		}

		public void setNoLines(boolean noLines) {
			this.noLines = noLines;
		}

		public void setBarcodeForObject(double param1) {
			ourBarcode = "";
			while(param1 != 1 ){
				if(param1 % 2 == 0){
					ourBarcode = "0" + ourBarcode;
				}
				else{
					ourBarcode = "1" + ourBarcode;
				}
			}
		}
		
}
