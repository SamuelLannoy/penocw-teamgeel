package field;


public class Tile {
	
	public Tile() {
		this(Position.POSITION_ZERO);
	}
	
	public Tile(Position position) {
		setPosition(position);
	}
	
	public Tile(int x, int y) {
		setPosition(new Position(x, y));
	}
	
	public Position getPosition() {
		return position;
	}

	private void setPosition(Position position) {
		if (!isValidPosition(position)) {
			throw new IllegalArgumentException("given position is invalid");
		}
		this.position = position;
	}
	
	private boolean isValidPosition(Position position) {
		return position != null;
	}

	private Position position;

	private Barcode barcode;

	public Barcode getBarcode() {
		return barcode;
	}

	public void setBarcode(Barcode barcode) {
		this.barcode = barcode;
	}
	
	@Override
	public String toString() {
		return "x:" + getPosition().getX() + " y:" + getPosition().getY();
	}

}
