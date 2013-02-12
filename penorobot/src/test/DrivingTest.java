package test;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;

public class DrivingTest {
	
		public static void main(String args[]) {
			System.out.println("Hello Team Geel");
			Button.waitForAnyPress();

			DifferentialPilot PILOT = new DifferentialPilot(54.4, 54.3, 164.1, Motor.B, Motor.C, false);
			PILOT.setTravelSpeed(150);
			PILOT.setRotateSpeed(50);
			PILOT.setAcceleration(400);
			
			// Afstand rijden
			while(true) {
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
			}
			
			// Draaien
			
			/*while(true) {
				PILOT.rotate(90);
				Button.waitForAnyPress();
				System.out.println("Starting...");
				Button.waitForAnyPress(2000);
				
				PILOT.rotate(1080);
				Button.waitForAnyPress();
				System.out.println("Starting...");
				Button.waitForAnyPress(2000);
				PILOT.rotate(2160);
				Button.waitForAnyPress();
				System.out.println("Starting...");
				Button.waitForAnyPress(2000);
				Button.waitForAnyPress();
				
				for(int i = 0; i<36; i++)
					PILOT.rotate(30);
				Button.waitForAnyPress();
				System.out.println("Starting...");
				Button.waitForAnyPress(2000);
				Button.waitForAnyPress();
			}*/

			// Einde
			//Button.waitForAnyPress();
		}
		
}
