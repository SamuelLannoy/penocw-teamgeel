package communication;

import lejos.nxt.LightSensor;
import lightsensor.LightSensorVigilante;
import robot.Robot;

/**
 * @author   Samuel
 */
public enum Encoding {
	
	FORWARD {
		@Override
		public void execute(double param1, double param2, boolean immediateReturn) {
			Robot.getInstance().forward();
		}
	},
	BACKWARD {
		@Override
		public void execute(double param1, double param2, boolean immediateReturn) {
			Robot.getInstance().backward();
		}
	}, 
	STOPMOVING {
		@Override
		public void execute(double param1, double param2, boolean immediateReturn) {
			Robot.getInstance().stop();
		}
	},
	TRAVEL {
		@Override
		public void execute(double param1, double param2, boolean immediateReturn) {
			try{
				Robot.getInstance().travel(param1, immediateReturn);
			}
			catch(IllegalStateException e){
				Robot.getInstance().stop();
			}
		}
	},
	TRAVELBACKWARD {
		@Override
		public void execute(double param1, double param2, boolean immediateReturn) {
			Robot.getInstance().travel(-param1, immediateReturn);
		}
	},
	ROTATELEFT {
		@Override
		public void execute(double param1, double param2, boolean immediateReturn) {
			Robot.getInstance().rotateLeft();
		}
	},
	ROTATERIGHT {
		@Override
		public void execute(double param1, double param2, boolean immediateReturn) {
			Robot.getInstance().rotateRight();
		}
	},
	ROTATERIGHTANGLE {
		@Override
		public void execute(double param1, double param2, boolean immediateReturn) {
			Robot.getInstance().rotateRight(param1, immediateReturn);
		}
	},
	ROTATELEFTANGLE {
		@Override
		public void execute(double param1, double param2, boolean immediateReturn) {
			Robot.getInstance().rotateLeft(param1, immediateReturn);
		}
	},
	ARC {
		@Override
		public void execute(double param1, double param2, boolean immediateReturn) {
			Robot.getInstance().arc(param1, param2, immediateReturn);
			
		}
	},
	SETMOVESPEED {
		@Override
		public void execute(double param1, double param2, boolean immediateReturn) {
			Robot.getInstance().setTravelSpeed(param1);
		}
	},
	SETTURNSPEED {
		@Override
		public void execute(double param1, double param2, boolean immediateReturn) {
			Robot.getInstance().setRotateSpeed(param1);
		}
	},
	ORIENTONWHITELINE {
		public void execute(double param1, double param2, boolean flag) {
			Robot.getInstance().orientOnWhiteLine(flag);
		}
	},
	SCANFORWALLS {
		public void execute(double param1, double param2, boolean immediateReturn) {
			Robot.getInstance().scanForWalls();
		}
	}, 
	SETONCENTERTILE {
		public void execute(double param1, double param2, boolean immediateReturn){
			Robot.getInstance().setOnCenterTile();
		}
	},
	NEWTILESCAN {
		public void execute(double param1, double param2, boolean immediateReturn){
			Robot.getInstance().newTileScan();
		}
	},
	ONLYLINES {
		public void execute(double param1, double param2, boolean flag) {
			Robot.getInstance().scanOnlyLines(flag);
		}
	},
	CHECKSCAN {
		public void execute(double param1, double param2, boolean flag) {
			Robot.getInstance().checkScan();
		}
	},
	SETOBJECTNR{
		public void execute(double param1, double param2, boolean flag) {
			Robot.getInstance().setObjectNr(param1);
		}
	},
	PAUSELIGHTSENSOR{
		@Override
		public void execute(double param1, double param2,
				boolean immediateReturn) {
			LightSensorVigilante.pause();
			
		}
	},
	RESUMELIGHTSENSOR{
		@Override
		public void execute(double param1, double param2,
				boolean immediateReturn) {
			LightSensorVigilante.resume();
		}
	},
	TURNBARCODE{

		@Override
		public void execute(double param1, double param2,
				boolean immediateReturn) {
			Robot.getInstance().turnBarcode();
		}
		
	},
	ULTIMATECENTER{
		public void execute(double param1, double param2,
				boolean immediateReturn) {
			Robot.getInstance().ultimateCentering();
		}
	},
	SETONCENTERTILEAFTERSEESAW{
		@Override
		public void execute(double param1, double param2,
				boolean immediateReturn) {
			Robot.getInstance().setOnCenterTileAfterSeesaw(immediateReturn);
		}
	};
	
	public abstract void execute(double param1, double param2, boolean immediateReturn);
}
