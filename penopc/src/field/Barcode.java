package field;

import java.util.Arrays;

public class Barcode {

	int[] code = new int[6];
	
	BarcodeType type;
	
	public BarcodeType getType() {
		return type;
	}

	public void setType(BarcodeType type) {
		this.type = type;
	}

	public Barcode(int... code)
		throws IllegalArgumentException {
		setCode(code);
	}
	
	public Barcode(int code) {
		int[] bincode = new int[6];
		String str = Integer.toBinaryString(code);
		int l = str.length();
		for (int i = 0; i < 6 - l; i++) {
			str = "0" + str;
		}
		for (int i = 0; i < 6; i++) {
			bincode[i] = Integer.parseInt(Character.toString(str.charAt(i)));
		}
		/*int lo = code % 10;
		int hi = code / 10;
		String loStr = Integer.toBinaryString(lo);
		String hiStr = Integer.toBinaryString(hi);
		int l = loStr.length();
		for (int i = 0; i < 3 - l; i++) {
			loStr = "0" + loStr;
		}
		l = hiStr.length();
		for (int j = 0; j < 3 - l; j++) {
			hiStr = "0" + hiStr;
		}
		for (int i = 0; i < 3; i++) {
			bincode[i+3] = Integer.parseInt(Character.toString(loStr.charAt(i)));
		}
		for (int i = 0; i < 3; i++) {
			bincode[i] = Integer.parseInt(Character.toString(loStr.charAt(i)));
		}*/
		setCode(bincode);
	}
	
	public Barcode(String stringCode){
		for (int i = 0; i < 6; i++){
			code[i] = Integer.parseInt(stringCode.substring(i,i+1));
		}
	}
	
	public int[] getCode() {
		return code;
	}
	
	public int getDecimal() {
		int dec = 0;
		for (int i = 0; i < getCode().length; i++) {
			dec += Math.pow(2, i) * getCode()[getCode().length - i - 1];
		}
		return dec;
	}
	
	private void setCode(int... code)
			throws IllegalArgumentException {
		if (!isValidCode(code))
			throw new IllegalArgumentException();
		this.code = code;
	}
	
	public static boolean isValidCode(int... code) {
		if (code.length != 6) {
			return false;
		}
		for (int i : code) {
			if (i != 0 && i != 1)
				return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(code);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Barcode other = (Barcode) obj;
		if (!Arrays.equals(code, other.code))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < getCode().length; i++){
			sb.append(getCode()[i]);
		}
		
		return sb.toString();
	}
	
	public boolean isSeesaw() {
		return getDecimal() >= 11 && getDecimal() <= 25;
	}
	
	public boolean isObject() {
		return getDecimal() >= 0 && getDecimal() <= 7;
	}
	
	public boolean isCheckPoint() {
		return getDecimal() >= 26;
	}
	
}
