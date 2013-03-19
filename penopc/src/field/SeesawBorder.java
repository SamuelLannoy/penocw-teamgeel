package field;

public class SeesawBorder extends Border implements SolidBorder {
	
	public SeesawBorder(BorderPosition borderPos)
			throws IllegalArgumentException {
		super(borderPos);
	}

	public SeesawBorder(int x1, int y1, int x2, int y2) {
		super(x1, y1, x2, y2);
	}

	public SeesawBorder(Tile tile, Direction dir) {
		super(tile, dir);
	}
	
	private boolean isDown;
	
	public void setDown() {
		isDown = true;
	}
	
	public void setUp() {
		isDown = false;
	}
	
	@Override
	public boolean isPassable() {
		return isDown;
	}

	@Override
	public Border newBorder(BorderPosition borderPos) {
		SeesawBorder border = new SeesawBorder(borderPos);
		border.isDown = this.isDown;
		return border;
	}

}
