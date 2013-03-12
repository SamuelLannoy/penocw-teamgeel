package test;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;

public class DrivingTest {
	
		public static void main(String args[]) {
			System.out.println("Hello Team Geel");
			Button.waitForAnyPress();

			DifferentialPilot PILOT = new DifferentialPilot(54.3, 54.9, 122, Motor.B, Motor.C, false);
			PILOT.setTravelSpeed(150);
			PILOT.setRotateSpeed(50);
			PILOT.setAcceleration(500);
			
			// Afstand rijden
			/*while(true) {
				System.out.println("Starting");
				Button.waitForAnyPress(2000);
				PILOT.travel(800);
				Button.waitForAnyPress();
				System.out.println("Starting");
				Button.waitForAnyPress(2000);
				PILOT.travel(1600);
				Button.waitForAnyPress();
				System.out.println("Starting");
				Button.waitForAnyPress(2000);
				PILOT.travel(2400);
				Button.waitForAnyPress();
			}*/
			
			// Draaien
			
			while(true) {
				PILOT.rotate(900);
				Button.waitForAnyPress();
				System.out.println("Starting...");
				Button.waitForAnyPress(2000);
				
				PILOT.rotate(1800);
				Button.waitForAnyPress();
				System.out.println("Starting...");
				Button.waitForAnyPress(2000);
				PILOT.rotate(2700);
				Button.waitForAnyPress();
				System.out.println("Starting...");
				Button.waitForAnyPress(2000);
				
				for(int i = 0; i<20; i++)
					PILOT.rotate(90);
				Button.waitForAnyPress();
				System.out.println("Starting...");
				Button.waitForAnyPress(2000);
				Button.waitForAnyPress();
			}

			// Einde
			//Button.waitForAnyPress();
		}
		
}
