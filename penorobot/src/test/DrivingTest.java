package test;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import robot.DifferentialPilot;

public class DrivingTest {
	
		public static void main(String args[]) {
			System.out.println("Hello Team Geel");
			Button.waitForAnyPress();

			DifferentialPilot PILOT;
			
			// Standaard differentialpilot: 54.3, 54.7, 128.65
			/*DifferentialPilot PILOT = new robot.DifferentialPilot(54.3, 54.7, 128.3, Motor.B, Motor.C, false);
			PILOT.setTravelSpeed(170);
			PILOT.setRotateSpeed(100);
			PILOT.setAcceleration(500);*/
			
			// Afstand rijden
//			while(true) {
//				System.out.println("Starting");
//				Button.waitForAnyPress(2000);
//				for(int i = 0; i < 7; i++)
//					PILOT.travel(400);
//				Button.waitForAnyPress();
//			}
			
			// Draaien
			boolean higher=false;
			boolean alot=false;
			double def = 128.3;
			while(true) {
				if(higher){
					if(alot){
						PILOT = new robot.DifferentialPilot(54.3, 54.7, def+1, Motor.B, Motor.C, false);
						PILOT.setTravelSpeed(170);
						PILOT.setRotateSpeed(100);
						PILOT.setAcceleration(500);
						def=def+1;
					}
						
					else{
						PILOT = new robot.DifferentialPilot(54.3, 54.7, def+0.1, Motor.B, Motor.C, false);
						def=def+0.1;
					}
						
				}
				else{
					if(alot){
						PILOT = new robot.DifferentialPilot(54.3, 54.7, def-1, Motor.B, Motor.C, false);
						def=def-1;
					}
						
					else{
						PILOT = new robot.DifferentialPilot(54.3, 54.7, def-0.1, Motor.B, Motor.C, false);
						def=def-0.1;
					}
						
				}				
				System.out.println(def);
				System.out.println("Starting...");
				Button.waitForAnyPress(2000);
				for(int i = 0; i<20; i++)
					PILOT.rotate(90);
				Button.waitForAnyPress();
				Button.waitForAnyPress(2000);
				for(int i = 0; i<20; i++)
					PILOT.rotate(-90);
				Button.waitForAnyPress();
				if(Button.LEFT.isPressed())
					higher=true;
				Button.waitForAnyPress();
				if(Button.RIGHT.isPressed())
					alot=true;
			}

			// Einde
			//Button.waitForAnyPress();
		}
		
}
