package field.fieldmerge;

import field.Barcode;
import field.TilePosition;

public class BarcodeNode {
	
	public BarcodeNode(Barcode barcode, TilePosition position) {
		setBarcode(barcode);
		setPosition(position);
	}
	
	private Barcode barcode;
	
	private TilePosition position;

	public Barcode getBarcode() {
		return barcode;
	}

	public void setBarcode(Barcode barcode) {
		this.barcode = barcode;
	}

	public TilePosition getPosition() {
		return position;
	}

	public void setPosition(TilePosition position) {
		this.position = position;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((barcode == null) ? 0 : barcode.hashCode());
		result = prime * result
				+ ((position == null) ? 0 : position.hashCode());
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
		BarcodeNode other = (BarcodeNode) obj;
		if (barcode == null) {
			if (other.barcode != null)
				return false;
		} else if (!barcode.equals(other.barcode))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "bc " + barcode.getDecimal() + " pos " + getPosition();
	}
}
