package field.fromfile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import field.*;

public enum MazePart {
	STRAIGHT {
		@Override
		public List<Border> getBorders(String orientation, Tile tile, double rotation) {
			orientation = orientation.toLowerCase();
			List<Border> map = new ArrayList<Border>();
			if (orientation.equals("s") || orientation.equals("n")) {
				map.add(new PanelBorder(tile, Direction.LEFT.offset(rotation)));
				map.add(new PanelBorder(tile, Direction.RIGHT.offset(rotation)));
				map.add(new WhiteBorder(tile, Direction.TOP.offset(rotation)));
				map.add(new WhiteBorder(tile, Direction.BOTTOM.offset(rotation)));
			} else {
				map.add(new WhiteBorder(tile, Direction.LEFT.offset(rotation)));
				map.add(new WhiteBorder(tile, Direction.RIGHT.offset(rotation)));
				map.add(new PanelBorder(tile, Direction.TOP.offset(rotation)));
				map.add(new PanelBorder(tile, Direction.BOTTOM.offset(rotation)));
			}
			return map;
		}
	}, CORNER {
		@Override
		public List<Border> getBorders(String orientation, Tile tile, double rotation) {
			orientation = orientation.toLowerCase();
			List<Border> map = new ArrayList<Border>();
			if (orientation.equals("n")) {
				map.add(new PanelBorder(tile, Direction.LEFT.offset(rotation)));
				map.add(new WhiteBorder(tile, Direction.RIGHT.offset(rotation)));
				map.add(new PanelBorder(tile, Direction.TOP.offset(rotation)));
				map.add(new WhiteBorder(tile, Direction.BOTTOM.offset(rotation)));
			} else if (orientation.equals("s")) {
				map.add(new WhiteBorder(tile, Direction.LEFT.offset(rotation)));
				map.add(new PanelBorder(tile, Direction.RIGHT.offset(rotation)));
				map.add(new WhiteBorder(tile, Direction.TOP.offset(rotation)));
				map.add(new PanelBorder(tile, Direction.BOTTOM.offset(rotation)));
			} else if (orientation.equals("e")) {
				map.add(new WhiteBorder(tile, Direction.LEFT.offset(rotation)));
				map.add(new PanelBorder(tile, Direction.RIGHT.offset(rotation)));
				map.add(new PanelBorder(tile, Direction.TOP.offset(rotation)));
				map.add(new WhiteBorder(tile, Direction.BOTTOM.offset(rotation)));
			} else if (orientation.equals("w")) {
				map.add(new PanelBorder(tile, Direction.LEFT.offset(rotation)));
				map.add(new WhiteBorder(tile, Direction.RIGHT.offset(rotation)));
				map.add(new WhiteBorder(tile, Direction.TOP.offset(rotation)));
				map.add(new PanelBorder(tile, Direction.BOTTOM.offset(rotation)));
			}
			return map;
		}
	}, T {
		@Override
		public List<Border> getBorders(String orientation, Tile tile, double rotation) {
			orientation = orientation.toLowerCase();
			List<Border> map = new ArrayList<Border>();
			if (orientation.equals("n")) {
				map.add(new WhiteBorder(tile, Direction.LEFT.offset(rotation)));
				map.add(new WhiteBorder(tile, Direction.RIGHT.offset(rotation)));
				map.add(new PanelBorder(tile, Direction.TOP.offset(rotation)));
				map.add(new WhiteBorder(tile, Direction.BOTTOM.offset(rotation)));
			} else if (orientation.equals("s")) {
				map.add(new WhiteBorder(tile, Direction.LEFT.offset(rotation)));
				map.add(new WhiteBorder(tile, Direction.RIGHT.offset(rotation)));
				map.add(new WhiteBorder(tile, Direction.TOP.offset(rotation)));
				map.add(new PanelBorder(tile, Direction.BOTTOM.offset(rotation)));
			} else if (orientation.equals("e")) {
				map.add(new WhiteBorder(tile, Direction.LEFT.offset(rotation)));
				map.add(new PanelBorder(tile, Direction.RIGHT.offset(rotation)));
				map.add(new WhiteBorder(tile, Direction.TOP.offset(rotation)));
				map.add(new WhiteBorder(tile, Direction.BOTTOM.offset(rotation)));
			} else if (orientation.equals("w")) {
				map.add(new PanelBorder(tile, Direction.LEFT.offset(rotation)));
				map.add(new WhiteBorder(tile, Direction.RIGHT.offset(rotation)));
				map.add(new WhiteBorder(tile, Direction.TOP.offset(rotation)));
				map.add(new WhiteBorder(tile, Direction.BOTTOM));
			}
			return map;
		}
	}, DEADEND {
		@Override
		public List<Border> getBorders(String orientation, Tile tile, double rotation) {
			orientation = orientation.toLowerCase();
			List<Border> map = new ArrayList<Border>();
			if (orientation.equals("n")) {
				map.add(new PanelBorder(tile, Direction.LEFT.offset(rotation)));
				map.add(new PanelBorder(tile, Direction.RIGHT.offset(rotation)));
				map.add(new PanelBorder(tile, Direction.TOP.offset(rotation)));
				map.add(new WhiteBorder(tile, Direction.BOTTOM.offset(rotation)));
			} else if (orientation.equals("s")) {
				map.add(new PanelBorder(tile, Direction.LEFT.offset(rotation)));
				map.add(new PanelBorder(tile, Direction.RIGHT.offset(rotation)));
				map.add(new WhiteBorder(tile, Direction.TOP.offset(rotation)));
				map.add(new PanelBorder(tile, Direction.BOTTOM.offset(rotation)));
			} else if (orientation.equals("e")) {
				map.add(new WhiteBorder(tile, Direction.LEFT.offset(rotation)));
				map.add(new PanelBorder(tile, Direction.RIGHT.offset(rotation)));
				map.add(new PanelBorder(tile, Direction.TOP.offset(rotation)));
				map.add(new PanelBorder(tile, Direction.BOTTOM.offset(rotation)));
			} else if (orientation.equals("w")) {
				map.add(new PanelBorder(tile, Direction.LEFT.offset(rotation)));
				map.add(new WhiteBorder(tile, Direction.RIGHT.offset(rotation)));
				map.add(new PanelBorder(tile, Direction.TOP.offset(rotation)));
				map.add(new PanelBorder(tile, Direction.BOTTOM.offset(rotation)));
			}
			return map;
		}
	}, CROSS {
		@Override
		public List<Border> getBorders(String orientation, Tile tile, double rotation) {
			List<Border> map = new ArrayList<Border>();
			map.add(new WhiteBorder(tile, Direction.LEFT.offset(rotation)));
			map.add(new WhiteBorder(tile, Direction.RIGHT.offset(rotation)));
			map.add(new WhiteBorder(tile, Direction.TOP.offset(rotation)));
			map.add(new WhiteBorder(tile, Direction.BOTTOM.offset(rotation)));
			return map;
		}
	}, CLOSED {
		@Override
		public List<Border> getBorders(String orientation, Tile tile, double rotation) {
			List<Border> map = new ArrayList<Border>();
			map.add(new PanelBorder(tile, Direction.LEFT.offset(rotation)));
			map.add(new PanelBorder(tile, Direction.RIGHT.offset(rotation)));
			map.add(new PanelBorder(tile, Direction.TOP.offset(rotation)));
			map.add(new PanelBorder(tile, Direction.BOTTOM.offset(rotation)));
			return map;
		}
	}, SEESAW {
		@Override
		public List<Border> getBorders(String orientation, Tile tile, double rotation) {
			orientation = orientation.toLowerCase();
			List<Border> map = new ArrayList<Border>();
			if (orientation.equals("n")) {
				map.add(new PanelBorder(tile, Direction.LEFT.offset(rotation)));
				map.add(new PanelBorder(tile, Direction.RIGHT.offset(rotation)));
				map.add(new SeesawBorder(tile, Direction.TOP.offset(rotation)));
				map.add(new WhiteBorder(tile, Direction.BOTTOM.offset(rotation)));
			} else if (orientation.equals("s")) {
				map.add(new PanelBorder(tile, Direction.LEFT.offset(rotation)));
				map.add(new PanelBorder(tile, Direction.RIGHT.offset(rotation)));
				map.add(new WhiteBorder(tile, Direction.TOP.offset(rotation)));
				map.add(new SeesawBorder(tile, Direction.BOTTOM.offset(rotation)));
			} else if (orientation.equals("e")) {
				map.add(new WhiteBorder(tile, Direction.LEFT.offset(rotation)));
				map.add(new SeesawBorder(tile, Direction.RIGHT.offset(rotation)));
				map.add(new PanelBorder(tile, Direction.TOP.offset(rotation)));
				map.add(new PanelBorder(tile, Direction.BOTTOM.offset(rotation)));
			} else if (orientation.equals("w")) {
				map.add(new SeesawBorder(tile, Direction.LEFT.offset(rotation)));
				map.add(new WhiteBorder(tile, Direction.RIGHT.offset(rotation)));
				map.add(new PanelBorder(tile, Direction.TOP.offset(rotation)));
				map.add(new PanelBorder(tile, Direction.BOTTOM.offset(rotation)));
			}
			return map;
		}
	}, UNKNOWN {

		@Override
		public List<Border> getBorders(String orientation, Tile tile, double rotation) {
			return new ArrayList<Border>();
		}
		
	};

	public abstract List< Border> getBorders(String orientation, Tile tile, double rotation);
	
	public List< Border> getBorders(String orientation, Tile tile) {
		return getBorders(orientation, tile, 0);
	}
	
	public static MazePart getPartFromString(String string) {
		//System.out.println("converting: " + string);
		return valueOf(string.toUpperCase());
	}
	
	public static String getToken(Map<Direction, Border> borders, Tile tile) {
		if (borders.size() != 4)
			throw new IllegalArgumentException();
		String retString = "";
		
		int wcount = 0, bcount = 0, scount = 0, gcount = 0;
		for (Border border : borders.values()) {
			if (border instanceof WhiteBorder)
				wcount++;
			if (border instanceof PanelBorder)
				bcount++;
			if (border instanceof UnsureBorder)
				gcount++;
			if (border instanceof SeesawBorder)
				scount++;
		}
		if (bcount == 4)
			retString = "closed";
		if (wcount == 4)
			retString = "cross";
		if (wcount == 3){
			if (borders.get(Direction.TOP) instanceof PanelBorder) {
				retString = "T.N";
			} else if (borders.get(Direction.RIGHT) instanceof PanelBorder) {
				retString = "T.E";
			} else if (borders.get(Direction.BOTTOM) instanceof PanelBorder) {
				retString = "T.S";
			} else if (borders.get(Direction.LEFT) instanceof PanelBorder) {
				retString = "T.W";
			}
		}
		if (wcount == 2){
			if (borders.get(Direction.TOP) instanceof WhiteBorder &&
					borders.get(Direction.BOTTOM) instanceof WhiteBorder) {
				retString = "Straight.N";
			} else if (borders.get(Direction.RIGHT) instanceof WhiteBorder &&
					borders.get(Direction.LEFT) instanceof WhiteBorder) {
				retString = "Straight.E";
			} else if (borders.get(Direction.BOTTOM) instanceof WhiteBorder &&
					borders.get(Direction.TOP) instanceof WhiteBorder) {
				retString = "Straight.S";
			} else if (borders.get(Direction.LEFT) instanceof WhiteBorder &&
					borders.get(Direction.RIGHT) instanceof WhiteBorder) {
				retString = "Straight.W";
			} else if (borders.get(Direction.BOTTOM) instanceof WhiteBorder &&
					borders.get(Direction.RIGHT) instanceof WhiteBorder) {
				retString = "Corner.N";
			} else if (borders.get(Direction.LEFT) instanceof WhiteBorder &&
					borders.get(Direction.BOTTOM) instanceof WhiteBorder) {
				retString = "Corner.E";
			} else if (borders.get(Direction.TOP) instanceof WhiteBorder &&
					borders.get(Direction.LEFT) instanceof WhiteBorder) {
				retString = "Corner.S";
			} else if (borders.get(Direction.RIGHT) instanceof WhiteBorder &&
					borders.get(Direction.TOP) instanceof WhiteBorder) {
				retString = "Corner.W";
			}
		}
		if (bcount == 3){
			if (borders.get(Direction.TOP) instanceof WhiteBorder) {
				retString = "Deadend.S";
			} else if (borders.get(Direction.RIGHT) instanceof WhiteBorder) {
				retString = "Deadend.W";
			} else if (borders.get(Direction.BOTTOM) instanceof WhiteBorder) {
				retString = "Deadend.N";
			} else if (borders.get(Direction.LEFT) instanceof WhiteBorder) {
				retString = "Deadend.E";
			}
		}
		if (scount == 1){
			if (borders.get(Direction.TOP) instanceof SeesawBorder &&
					borders.get(Direction.BOTTOM) instanceof WhiteBorder) {
				retString = "Seesaw.N";
			} else if (borders.get(Direction.RIGHT) instanceof SeesawBorder &&
					borders.get(Direction.LEFT) instanceof WhiteBorder) {
				retString = "Seesaw.E";
			} else if (borders.get(Direction.BOTTOM) instanceof SeesawBorder &&
					borders.get(Direction.TOP) instanceof WhiteBorder) {
				retString = "Seesaw.S";
			} else if (borders.get(Direction.LEFT) instanceof SeesawBorder &&
					borders.get(Direction.RIGHT) instanceof WhiteBorder) {
				retString = "Seesaw.W";
			}
		}
		
		if (tile.getBarcode() != null) {
			retString += "." + tile.getBarcode().getDecimal();
		}
		
		return retString;
	}
}
