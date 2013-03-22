package test;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;

public class DrivingTest {
	
		public static void main(String args[]) {
			System.out.println("Hello Team Geel");
			Button.waitForAnyPress();

			DifferentialPilot PILOT = new DifferentialPilot(54.3, 54.7, 128.65, Motor.B, Motor.C, false);
			PILOT.setTravelSpeed(150);
			PILOT.setRotateSpeed(50);
			PILOT.setAcceleration(500);
			
			// Afstand rijden
			while(true) {
				System.out.println("Starting");
				Button.waitForAnyPress(2000);
				for(int i = 0; i < 7; i++)
					PILOT.travel(400);
				Button.waitForAnyPress();
			}
			
			// Draaien
			
//			while(true) {
//				System.out.println("Starting...");
//				Button.waitForAnyPress(2000);
//				for(int i = 0; i<20; i++)
//					PILOT.rotate(90);
//				for(int i = 0; i<20; i++)
//					PILOT.rotate(-90);
//				Button.waitForAnyPress();
//			}

			// Einde
			//Button.waitForAnyPress();
		}
		
}
