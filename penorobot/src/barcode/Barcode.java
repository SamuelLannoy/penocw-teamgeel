package barcode;

import communication.Buffer;

import robot.Robot;

public class Barcode {

	private BarcodeType type;
	private String code;
	private int objectNr;
	private int teamNr;
	
	public Barcode(BarcodeType type, String code){
		setCode(code);
		this.type = type;
		if (type == BarcodeType.OBJECT){
			determineTeamAndObjectnr(code);
			Buffer.addDebug("OBJECT barcode: object "+getObjectNr()+", team "+getTeamNr());
		}
	}

	public void execute(){
		Buffer.addDebug("Type: "+type);
		if(type == BarcodeType.OBJECT){
			Buffer.addDebug("ObjectNr Barocode" + objectNr);
			Buffer.addDebug("Our ObjectNr" + Robot.getInstance().getObjectNr());
			if (objectNr == Robot.getInstance().getObjectNr()){
				Robot.getInstance().setTeamNr(teamNr);
				BarcodeType.PICKUP.execute();
				Buffer.addDebug("Executing barcode: "+this.getCode()+", type: "+BarcodeType.PICKUP);
			}
			else{
				BarcodeType.OTHERPLAYERBARCODE.execute();
				Buffer.addDebug("Executing barcode: "+this.getCode()+", type: "+BarcodeType.OTHERPLAYERBARCODE);
			}
		}
		else{
			Buffer.addDebug("Executing barcode: "+this.getCode()+", type: "+this.getBarcodeType());
			type.execute();
		}
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public BarcodeType getBarcodeType(){
		return type;
	}
	
	public int getTeamNr(){
		if (type!=BarcodeType.OBJECT)
				throw new IllegalArgumentException();
		return teamNr;
	}
	
	public int getObjectNr(){
		if (type!=BarcodeType.OBJECT)
			throw new IllegalArgumentException();
		return objectNr;
	}
	
	private void determineTeamAndObjectnr(String code) {
		switch(code){
			case "000000": 
				objectNr = 0; 
				teamNr = 0;
				break;
			case "000100": 
				objectNr = 0;
				teamNr = 1;
				break;
			case "000001": 
				objectNr = 1; 
				teamNr = 0;
				break;
			case "000101": 
				objectNr = 1; 
				teamNr = 1;
				break;
			case "000010": 
				objectNr = 2; 
				teamNr = 0;
				break;
			case "000110": 
				objectNr = 2;
				teamNr = 1;
				break;
			case "000011": 
				objectNr = 3; 
				teamNr = 0;
				break;
			case "000111": 
				objectNr = 3; 
				teamNr = 1;
				break;
		}
	}

}
