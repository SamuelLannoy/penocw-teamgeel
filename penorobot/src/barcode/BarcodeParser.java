package barcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import robot.Robot;

import communication.Buffer;


import lejos.nxt.Button;
import lightsensor.Color;
import lightsensor.LightSensorVigilante;

public class BarcodeParser {
	
	public static Barcode getBarcode(String code) {
		Buffer.addDebug("CODE TO CONVERT: "+code);
			int decimal = Integer.parseInt(code, 2);
			if(!getLegalNrsDecimal().contains(decimal)){
				return new Barcode(BarcodeType.ILLEGAL, code);
			}
			else if(decimal < 8 && decimal > 0){
				return new Barcode(BarcodeType.OBJECT, code);
			}
			else if (decimal > 10 && decimal < 26 && decimal != 14 && decimal != 22 ){
				return new Barcode(BarcodeType.SEESAW,code);
			}
			else{
				return new Barcode(BarcodeType.CHECKPOINT, code);
			}
	}
	
	private static List<Integer> getLegalNrsDecimal(){
		ArrayList<Integer> nrs = new ArrayList<Integer>();
		nrs.add(1); nrs.add(2); nrs.add(3); nrs.add(4); nrs.add(5); nrs.add(6); nrs.add(7);
		nrs.add(9); nrs.add(10); nrs.add(11); nrs.add(13); nrs.add(14); nrs.add(15); nrs.add(17);
		nrs.add(19); nrs.add(21); nrs.add(22); nrs.add(23); nrs.add(27); nrs.add(29); nrs.add(31);
		nrs.add(35); nrs.add(37); nrs.add(39); nrs.add(43); nrs.add(47); nrs.add(55);
		return nrs;
	}
	
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
		//TODO richting barcode lezing bepalen!!
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
		Buffer.addDebug("Read barcode: "+code+", dec: "+Integer.parseInt(code, 2));
		code = Integer.parseInt(code,2) < Integer.parseInt(getReversed(code), 2) ? code : getReversed(code);
		Buffer.addDebug("The barcode: "+code+", dec: "+Integer.parseInt(code, 2));
		return BarcodeParser.getBarcode(code);
	}
	
	private static String getReversed(String code) {
		StringBuffer result =  new StringBuffer();
		for(int i = code.length(); i > 0; i--){
			result.append(code.substring(i-1, i));
		}
		return result.toString();
	}

}
