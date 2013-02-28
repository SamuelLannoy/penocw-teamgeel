package communication;

/**
 * @author  Samuel
 */
public class Status {
	
	private static double movementIncrement = 0;
	private static double angleIncrement = 0;
	private static boolean isMoving = false;
	private static boolean isScanning = false;
	private static boolean isCentering = false;
	private static boolean isStartTile = false;
	private static boolean isFinishTile = false;
	private static boolean hasBall = false;
	private static int teamNr = -1;
	private static int objectNr = -1;
	
	public static double getMovementIncrement() {
		double increment = movementIncrement/10;
		movementIncrement = 0;
		return increment;
	}
	
	public static double getAngleIncrement() {
		double increment = -angleIncrement;
		angleIncrement = 0;
		return increment;
	}
	
	public static boolean isMoving() {
		return isMoving;
	}
	
	public static boolean isScanning() {
		return isScanning;
	}
	
	public static boolean isCentering() {
		return isCentering;
	}
	
	public static boolean isStartTile() {
		return isStartTile;
	}
	
	public static boolean isFinishTile() {
		return isFinishTile;
	}
	
	public static void update(double movementIncrement, double angleIncrement, boolean isMoving, 
			boolean isScanning, boolean isCentering, boolean isStartTile, boolean isFinishTile){
		Status.movementIncrement = movementIncrement;
		Status.angleIncrement = angleIncrement;
		Status.isMoving = isMoving;
		Status.isScanning = isScanning;
		Status.isCentering = isCentering;
		Status.isStartTile = isStartTile;
		Status.isFinishTile = isFinishTile;
	}
	
	public static void updateObjectInfo(boolean hasBall, int teamNr){
		Status.hasBall = hasBall;
		Status.teamNr = teamNr;
	}
	
	public static void setObjectNr(int i){
		objectNr = i;
	}
	
	public static int getObjectNr(){
		return objectNr;
	}
	
	public static boolean getHasBall(){
		return hasBall;
	}
	
	public static int getTeamNr(){
		return teamNr;
	}
	
	
}
