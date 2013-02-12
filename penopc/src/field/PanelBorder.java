package field;

public class PanelBorder extends Border {

	public PanelBorder(int x1, int y1, int x2, int y2) {
		super(x1, y1, x2, y2);
	}
	
	public PanelBorder(BorderPosition borderPos) {
		super(borderPos);
	}
	
	public PanelBorder(Tile tile, Direction dir) {
		super(tile, dir);
	}

	@Override
	public boolean isPassable() {
		return false;
	}
	
	public String toString() {
		return "panel border";
	}

}
