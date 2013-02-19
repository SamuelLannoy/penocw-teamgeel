package barcode;

import java.util.ArrayList;
import java.util.List;

import communication.Buffer;


import lejos.nxt.Button;
import lightsensor.Color;
import lightsensor.LightSensorVigilante;

public enum Barcode {
	TURNLEFT {
		@Override
		public String getCode() {
			return "000101";
		}

		@Override
		public void execute() {
			LightSensorVigilante.pause();
			Button.waitForAnyPress(200);
			BarcodeAction.turnAroundLeft();		
			LightSensorVigilante.resume();
		}
	},
	TURNRIGHT {
		@Override
		public String getCode() {
			return "001001";
		}

		@Override
		public void execute() {
			LightSensorVigilante.pause();
			Button.waitForAnyPress(200);
			BarcodeAction.turnAroundRight();
			LightSensorVigilante.resume();

		}
	},
	PLAYMUSIC {
		@Override
		public String getCode() {
			return "001111";
		}

		@Override
		public void execute() {
			BarcodeAction.playMusic();
		}
	},
	WAITFIVESECONDS {
		@Override
		public String getCode() {
			return "010011";
		}

		@Override
		public void execute() {
			BarcodeAction.waitFiveSeconds();
		}
	},
	LOWSPEED {
		@Override
		public String getCode() {
			return "011001";
		}

		@Override
		public void execute() {
			BarcodeAction.setLowSpeed();
		}
	},
	HIGHSPEED {
		@Override
		public String getCode() {
			return "100101";
		}

		@Override
		public void execute() {
			BarcodeAction.setHighSpeed();
		}
	},
	FINISH {
		@Override
		public String getCode() {
			return "110111";
		}

		@Override
		public void execute() {
			BarcodeAction.finish();
		}
	},
	START {
		@Override
		public String getCode() {
			return "001101";
		}

		@Override
		public void execute() {
			BarcodeAction.start();
		}
	};
	
	public static Barcode getBarcode(String code) {
		switch(code) {
			case "000101":
				return TURNLEFT;
			case "001001":
				return TURNRIGHT;
			case "001111":
				return PLAYMUSIC;
			case "010011":
				return WAITFIVESECONDS;
			case "011001":
				return LOWSPEED;
			case "100101":
				return HIGHSPEED;
			case "110111":
				return FINISH;
			case "001101":
				return START;
		    default:
		    	return null;
		}
	}
	
	public abstract String getCode();
	
	public abstract void execute();
	
	/**
	 * Converts a list of Colors into a barcode, given that
	 * 		the list starts with Color.BLACK
	 * 		the list ends with Color.BLACK
	 * 		the barcode contains 8 sections
	 * @param colors
	 * @throws	IllegalArgumentException
	 * 			List size to small
	 * @throws	IllegalStateException
	 * 			List doesn't start and end with Color.BLACK
	 * @return	result == null
	 * 			Not a known barcode
	 */
	public static Barcode convertToBarcode(List<Color> theColors){
		List<Color> colors = new ArrayList<Color>(theColors);
		if(colors.size()>10){
			for(int i=0; i<10; i++){
				colors.remove(colors.size()-1);
			}
		}
		int sectionsLength = colors.size()/8;
		int mod = colors.size() % 8;
		int[] correction = new int[8];
		for(int i =0; i < mod; i++){
			correction[i] = 1;
		}
		if(sectionsLength == 0){
			throw new IllegalArgumentException("illegalstate: lijst te klein");
		}
		String code = "";
		int last = 0;
		for(int i = 0; i < 8; i++){
			int countWhite = 0;
			int countBlack = 0;
			for(int j = last; j < last + sectionsLength + correction[i] ; j++){
				if(colors.get(j) == Color.WHITE){
					countWhite++;
				}
				else if(colors.get(j) == Color.BLACK){
					countBlack++;
				}
			}
			code += (countWhite > countBlack ? Color.WHITE.getCodeForBarcode() : Color.BLACK.getCodeForBarcode());
			last = last + sectionsLength + correction[i];
		}
		if(!code.substring(0,1).equals("0") || !code.substring(7, 8).equals("0")){
			throw new IllegalStateException("illegalstate: lijst begint of eindigt niet met zwart");
		}
		code = code.substring(1,7);
		code = Barcode.getBarcode(code) == null ? Barcode.getReversed(code) : code;
		System.out.println("Barcode: "+code);
		Buffer.addDebug("Read barcode: "+code);
		return Barcode.getBarcode(code);
	}
	
	private static String getReversed(String code) {
		StringBuffer result =  new StringBuffer();
		for(int i = code.length(); i > 0; i--){
			result.append(code.substring(i-1, i));
		}
		return result.toString();
	}

}
