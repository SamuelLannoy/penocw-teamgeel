package corner;

public class Corner {
	
	public static double[][] getCorners(double tx, double ty, double width, double length, double ta) {
		double[][] arr = new double[4][2];
		
		
		//left upper
		double lux = - width;
		double luy = length;
		
		arr[0][0] = tx + ( lux * Math.cos(ta) - luy * Math.sin(ta)); 
		arr[0][1] = ty + ( lux * Math.sin(ta)  + luy * Math.cos(ta)); 
		//right upper
		double rux = width;
		double ruy = length;
		
		arr[1][0] = tx + ( rux * Math.cos(ta)  - ruy * Math.sin(ta));
		arr[1][1] = ty + ( rux * Math.sin(ta)  + ruy * Math.cos(ta)); 
		//left lower
		double llx = width;
		double lly = - length;
		
		arr[2][0] = tx + ( llx * Math.cos(ta)  - lly * Math.sin(ta));  
		arr[2][1] = ty + ( llx * Math.sin(ta)  + lly * Math.cos(ta));
		//right lower
		double rlx = - width;
		double rly = - length;
		
		arr[3][0] = tx + ( rlx * Math.cos(ta)  - rly * Math.sin(ta));
		arr[3][1] = ty + ( rlx * Math.sin(ta)  + rly * Math.cos(ta)); 
		
		return arr;
	}

}
