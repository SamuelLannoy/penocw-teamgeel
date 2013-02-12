package field;

public class BorderPosition {

	private Position position1 = null;
	private Position position2 = null;
	
	public BorderPosition(Position position1, Position position2) {
		super();
		setPosition1(position1);
		setPosition2(position2);
	}
	
	public int getLowestX() {
		return Math.min(getPosition1().getX(), getPosition2().getX());
	}
	
	public int getLowestY() {
		return Math.min(getPosition1().getY(), getPosition2().getY());
	}
	
	public int getHighestX() {
		return Math.max(getPosition1().getX(), getPosition2().getX());
	}
	
	public int getHighestY() {
		return Math.max(getPosition1().getY(), getPosition2().getY());
	}
	
	public Position getOtherPosition(Position pos) {
		if (position1.equals(pos)) {
			return getPosition2();
		}
		if (position2.equals(pos)) {
			return getPosition1();
		}
		throw new IllegalArgumentException("borderpos does not have " + pos);
	}
	
	public Position getPosition1() {
		return position1;
	}
	public void setPosition1(Position position1) {
		if (!canHaveAsPosition(position1, 1)) {
			throw new IllegalArgumentException();
		}
		this.position1 = position1;
	}
	public Position getPosition2() {
		return position2;
	}
	public void setPosition2(Position position2) {
		if (!canHaveAsPosition(position2, 2)) {
			throw new IllegalArgumentException();
		}
		this.position2 = position2;
	}
	public boolean canHaveAsPosition(Position position, int pos)
		throws IllegalArgumentException{
		if (position == null) {
			return false;
		}
		if (pos == 1) {
			if (position2 == null || position.manhattanDistance(position2) == 1) {
				return true;
			}
		} else if (pos == 2){
			if (position1 == null || position.manhattanDistance(position1) == 1) {
				return true;
			}
			return !position.equals(position1);
		} else {
			throw new IllegalArgumentException();
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		Position pos = new Position(position1, position2);
		result = prime * result
				+ ((pos == null) ? 0 : pos.hashCode());
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
		BorderPosition other = (BorderPosition) obj;
		if (position1 == null && other.position1 != null) {
			return false;
		}
		if (position2 == null && other.position2 != null) {
			return false;
		}
		if (position1 != null && position2 != null && other.position1 != null && other.position2 != null) {
			if (position1.equals(other.position1) && position2.equals(other.position2)){
				return true;
			} else if (position1.equals(other.position2) && position2.equals(other.position1)) {
				return true;
			}
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "1:[" + position1 + "] 2:["
				+ position2 + "]";
	}
	

}
