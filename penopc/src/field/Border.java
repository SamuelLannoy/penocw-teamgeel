package field;

public abstract class Border {

	public Border(BorderPosition borderPos)
			throws IllegalArgumentException {
		setBorderPos(borderPos);
	}
	
	public Border(int x1, int y1, int x2, int y2) {
		BorderPosition borderPosition = new BorderPosition(new Position(x1, y1), new Position(x2, y2));
		setBorderPos(borderPosition);
	}
	
	public Border(Tile tile, Direction dir) {
		BorderPosition borderPosition = new BorderPosition(tile.getPosition(), dir.getPositionInDirection(tile.getPosition()));
		setBorderPos(borderPosition);
	}
	
	public BorderPosition getBorderPos() {
		return borderPos;
	}

	public void setBorderPos(BorderPosition borderPos)
			throws IllegalArgumentException {
		if (!canHaveAsBorderPos(borderPos)) 
			throw new IllegalArgumentException();
		this.borderPos = borderPos;
	}
	public boolean canHaveAsBorderPos(BorderPosition borderPos) {
		return borderPos != null;
	}

	private BorderPosition borderPos;
	
	public abstract boolean isPassable();
	
	public boolean isHorizontal() {
		return (getBorderPos().getPosition1().getX() == getBorderPos().getPosition2().getX());
	}
}
