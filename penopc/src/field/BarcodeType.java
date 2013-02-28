package field;

import field.BarcodeType;

public enum BarcodeType {

	OBJECT{

		@Override
		public void execute() {
			throw new IllegalStateException();
			// TODO Auto-generated method stub
			
		}
		
	},
	
	PICKUP { //sub van OBJECT

		@Override
		public void execute() {
			//BarcodeAction.pickupobject();			
		}
		
	},
	
	OTHERPLAYERBARCODE{ //sub van OBJECT

		@Override
		public void execute() {
			// TODO Auto-generated method stub
			
		}
		
	},
	 
	SEESAW {

		@Override
		public void execute() {
			//BarcodeAction.seesaw();
		}
		
	},
	
	CHECKPOINT {

		@Override
		public void execute() {
			// TODO Auto-generated method stub
			
		}
		
	}, 
	
	ILLEGAL {
		@Override
		public void execute() {
			// TODO Auto-generated method stub
			
		}
	};
	
	public abstract void execute();

}
