package field;

public class WhiteBorder extends Border {

	public WhiteBorder(BorderPosition borderPos) {
		super(borderPos);
		
	}

	public WhiteBorder(int x1, int y1, int x2, int y2) {
		super(x1, y1, x2, y2);
	}
	
	public WhiteBorder(Tile tile, Direction dir) {
		super(tile, dir);
	}

	@Override
	public boolean isPassable() {
		return true;
	}
	
	public String toString() {
		return "white border";
	}

	@Override
	public Border newBorder(BorderPosition borderPos) {
		return new WhiteBorder(borderPos);
	}

}
