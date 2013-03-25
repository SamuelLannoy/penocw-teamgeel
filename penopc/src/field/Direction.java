package field;

public enum Direction {
	TOP {
		@Override
		public BorderPosition getBorderPositionInDirection(Position pos) {
			return new BorderPosition(pos, pos.getNorthPosition());
		}

		@Override
		public Position getPositionInDirection(Position pos) {
			return pos.getNorthPosition();
		}

		@Override
		public int getDistance(double x, double y, Tile tile, BorderPosition borderPos) {
			return (int)(20 - y) + 40*Math.abs(tile.getPosition().getY() - borderPos.getLowestY());
		}

		@Override
		public double toAngle() {
			return 0;
		}

		@Override
		public Direction opposite() {
			return BOTTOM;
		}

		@Override
		public int turnTo(Direction other) {
			switch (other) {
				case TOP:
					return 0;
				case LEFT:
					return -90;
				case RIGHT:
					return 90;
				case BOTTOM:
					return 180;
			}
			return 0;
		}
	},
	BOTTOM {
		@Override
		public BorderPosition getBorderPositionInDirection(Position pos) {
			return new BorderPosition(pos, pos.getSouthPosition());
		}

		@Override
		public Position getPositionInDirection(Position pos) {
			return pos.getSouthPosition();
		}

		@Override
		public int getDistance(double x, double y, Tile tile, BorderPosition borderPos) {
			return (int)(20 + y) + 40*Math.abs(tile.getPosition().getY() - borderPos.getHighestY());
		}

		@Override
		public double toAngle() {
			return 180;
		}

		@Override
		public Direction opposite() {
			return TOP;
		}

		@Override
		public int turnTo(Direction other) {
			switch (other) {
				case TOP:
					return 180;
				case LEFT:
					return 90;
				case RIGHT:
					return -90;
				case BOTTOM:
					return 0;
			}
			return 0;
		}
	},
	LEFT {
		@Override
		public BorderPosition getBorderPositionInDirection(Position pos) {
			return new BorderPosition(pos, pos.getLeftPosition());
		}

		@Override
		public Position getPositionInDirection(Position pos) {
			return pos.getLeftPosition();
		}

		@Override
		public int getDistance(double x, double y, Tile tile, BorderPosition borderPos) {
			return (int)(20 + x) + 40*Math.abs(tile.getPosition().getX() - borderPos.getHighestX());
		}

		@Override
		public double toAngle() {
			return 270;
		}

		@Override
		public Direction opposite() {
			return RIGHT;
		}

		@Override
		public int turnTo(Direction other) {
			switch (other) {
				case TOP:
					return 90;
				case LEFT:
					return 0;
				case RIGHT:
					return 180;
				case BOTTOM:
					return -90;
			}
			return 0;
		}
	},
	RIGHT {
		@Override
		public BorderPosition getBorderPositionInDirection(Position pos) {
			return new BorderPosition(pos, pos.getRightPosition());
		}

		@Override
		public Position getPositionInDirection(Position pos) {
			return pos.getRightPosition();
		}

		@Override
		public int getDistance(double x, double y, Tile tile, BorderPosition borderPos) {
			return (int)(20 - x) + 40*Math.abs(tile.getPosition().getX() - borderPos.getLowestX());
		}

		@Override
		public double toAngle() {
			return 90;
		}

		@Override
		public Direction opposite() {
			return LEFT;
		}

		@Override
		public int turnTo(Direction other) {
			switch (other) {
				case TOP:
					return -90;
				case LEFT:
					return 180;
				case RIGHT:
					return 0;
				case BOTTOM:
					return 90;
			}
			return 0;
		}
	};
	
	public static Direction fromPos(robot.Position pos) {
		/*if (pos.getPosX() > -19 && pos.getPosX() < 19) {
			if (pos.getPosY() > 0) {
				return TOP;
			} else {
				return BOTTOM;
			}
		}
		if (pos.getPosY() > -19 && pos.getPosY() < 19) {
			if (pos.getPosX() > 0) {
				return RIGHT;
			} else {
				return LEFT;
			}
		}
		return TOP;*/
		//throw new IllegalArgumentException("x: " + pos.getPosX() + " y: " + pos.getPosY());
		//return null;
		
		
		double rotation = (pos.getRotation() % 360);
		return fromAngle(rotation);
	}
	
	public static Direction fromAngle(double rotation) {
		while (rotation < 0){
			rotation += 360;
		}
		rotation = rotation % 360;
		if (rotation > 225 && rotation <= 315){
			// WEST (links)
			return LEFT;
		}
		if (rotation > 135 && rotation <= 225){
			// ZUID (onder)
			return BOTTOM;
		}
		if (rotation > 45 && rotation <= 135){
			// OOST (rechts)
			return RIGHT;
		}
		if (rotation > 315 || rotation <= 45){
			// NOORD (boven)
			return TOP;
		}
		throw new RuntimeException();
	}
	
	public static Direction fromDiffPos(int dx, int dy) {
		if (dx == 0 && dy == -1) {
			return BOTTOM;
		} else if (dx == 0 && dy == 1) {
			return TOP;
		} else if (dx == 1 && dy == 0) {
			return RIGHT;
		} else if (dx == -1 && dy == 0) {
			return LEFT;
		}
		throw new IllegalArgumentException();
	}
	
	public abstract BorderPosition getBorderPositionInDirection(Position pos);
	public abstract Position getPositionInDirection(Position pos);
	public abstract int getDistance(double x, double y, Tile tile, BorderPosition borderPos);
	public abstract double toAngle();
	public abstract Direction opposite();
	public abstract int turnTo(Direction other);
	
	public static Direction fromString(String in) {
		if (in.equals("N")) {
			return TOP;
		} else if (in.equals("E")) {
			return RIGHT;
		} else if (in.equals("S")) {
			return BOTTOM;
		} else if (in.equals("W")) {
			return LEFT;
		} else {
			return null;
		}
	}
}