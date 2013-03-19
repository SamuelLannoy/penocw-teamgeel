package robot;


public class Collision {
	public static boolean collides(RobotModel robot1, double[][] corners2) {
		double[][] corners1 = robot1.getCorners();
		
		double maxX1 = Double.MIN_VALUE, maxX2 = Double.MIN_VALUE, maxY1 = Double.MIN_VALUE, maxY2 = Double.MIN_VALUE;
		double minX1 = Double.MAX_VALUE, minX2 = Double.MAX_VALUE, minY1 = Double.MAX_VALUE, minY2 = Double.MAX_VALUE;
		
		for (int i = 0; i < 4; i++) {
			if (corners1[i][0] > maxX1) {
				maxX1 = corners1[i][0];
			}

			if (corners1[i][0] < minX1) {
				minX1 = corners1[i][0];
			}
			
			if (corners1[i][1] > maxY1) {
				maxY1 = corners1[i][1];
			}

			if (corners1[i][1] < minY1) {
				minY1 = corners1[i][1];
			}
			
			if (corners2[i][0] > maxX2) {
				maxX2 = corners2[i][0];
			}

			if (corners2[i][0] < minX2) {
				minX2 = corners2[i][0];
			}
			
			if (corners2[i][1] > maxY2) {
				maxY2 = corners2[i][1];
			}

			if (corners2[i][1] < minY2) {
				minY2 = corners2[i][1];
			}
		}
		
		if ((minX1 > minX2 && minX1 < maxX2) ||
				(maxX1 > minX2 && maxX1 < maxX2) || 
				(minX2 > minX1 && maxX2 < maxX1) || 
				(maxX2 > minX1 && maxX2 < maxX1) || 
				(minY1 > minY2 && minY1 < maxY2) ||
				(maxY1 > minY2 && maxY1 < maxY2) || 
				(minY2 > minY1 && maxY2 < maxY1) || 
				(maxY2 > minY1 && maxY2 < maxY1)
				) {
			return true;
		}
		
		return false;
	}
}
