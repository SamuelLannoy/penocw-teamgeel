package field.fromfile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import field.*;

public enum MazePart {
	STRAIGHT {
		@Override
		public List<Border> getBorders(String orientation, Tile tile) {
			orientation = orientation.toLowerCase();
			List<Border> map = new ArrayList<Border>();
			if (orientation.equals("s") || orientation.equals("n")) {
				map.add(new PanelBorder(tile, Direction.LEFT));
				map.add(new PanelBorder(tile, Direction.RIGHT));
				map.add(new WhiteBorder(tile, Direction.TOP));
				map.add(new WhiteBorder(tile, Direction.BOTTOM));
			} else {
				map.add(new WhiteBorder(tile, Direction.LEFT));
				map.add(new WhiteBorder(tile, Direction.RIGHT));
				map.add(new PanelBorder(tile, Direction.TOP));
				map.add(new PanelBorder(tile, Direction.BOTTOM));
			}
			return map;
		}
	}, CORNER {
		@Override
		public List<Border> getBorders(String orientation, Tile tile) {
			orientation = orientation.toLowerCase();
			List<Border> map = new ArrayList<Border>();
			if (orientation.equals("n")) {
				map.add(new PanelBorder(tile, Direction.LEFT));
				map.add(new WhiteBorder(tile, Direction.RIGHT));
				map.add(new PanelBorder(tile, Direction.TOP));
				map.add(new WhiteBorder(tile, Direction.BOTTOM));
			} else if (orientation.equals("s")) {
				map.add(new WhiteBorder(tile, Direction.LEFT));
				map.add(new PanelBorder(tile, Direction.RIGHT));
				map.add(new WhiteBorder(tile, Direction.TOP));
				map.add(new PanelBorder(tile, Direction.BOTTOM));
			} else if (orientation.equals("e")) {
				map.add(new WhiteBorder(tile, Direction.LEFT));
				map.add(new PanelBorder(tile, Direction.RIGHT));
				map.add(new PanelBorder(tile, Direction.TOP));
				map.add(new WhiteBorder(tile, Direction.BOTTOM));
			} else if (orientation.equals("w")) {
				map.add(new PanelBorder(tile, Direction.LEFT));
				map.add(new WhiteBorder(tile, Direction.RIGHT));
				map.add(new WhiteBorder(tile, Direction.TOP));
				map.add(new PanelBorder(tile, Direction.BOTTOM));
			}
			return map;
		}
	}, T {
		@Override
		public List<Border> getBorders(String orientation, Tile tile) {
			orientation = orientation.toLowerCase();
			List<Border> map = new ArrayList<Border>();
			if (orientation.equals("n")) {
				map.add(new WhiteBorder(tile, Direction.LEFT));
				map.add(new WhiteBorder(tile, Direction.RIGHT));
				map.add(new PanelBorder(tile, Direction.TOP));
				map.add(new WhiteBorder(tile, Direction.BOTTOM));
			} else if (orientation.equals("s")) {
				map.add(new WhiteBorder(tile, Direction.LEFT));
				map.add(new WhiteBorder(tile, Direction.RIGHT));
				map.add(new WhiteBorder(tile, Direction.TOP));
				map.add(new PanelBorder(tile, Direction.BOTTOM));
			} else if (orientation.equals("e")) {
				map.add(new WhiteBorder(tile, Direction.LEFT));
				map.add(new PanelBorder(tile, Direction.RIGHT));
				map.add(new WhiteBorder(tile, Direction.TOP));
				map.add(new WhiteBorder(tile, Direction.BOTTOM));
			} else if (orientation.equals("w")) {
				map.add(new PanelBorder(tile, Direction.LEFT));
				map.add(new WhiteBorder(tile, Direction.RIGHT));
				map.add(new WhiteBorder(tile, Direction.TOP));
				map.add(new WhiteBorder(tile, Direction.BOTTOM));
			}
			return map;
		}
	}, DEADEND {
		@Override
		public List<Border> getBorders(String orientation, Tile tile) {
			orientation = orientation.toLowerCase();
			List<Border> map = new ArrayList<Border>();
			if (orientation.equals("n")) {
				map.add(new PanelBorder(tile, Direction.LEFT));
				map.add(new PanelBorder(tile, Direction.RIGHT));
				map.add(new PanelBorder(tile, Direction.TOP));
				map.add(new WhiteBorder(tile, Direction.BOTTOM));
			} else if (orientation.equals("s")) {
				map.add(new PanelBorder(tile, Direction.LEFT));
				map.add(new PanelBorder(tile, Direction.RIGHT));
				map.add(new WhiteBorder(tile, Direction.TOP));
				map.add(new PanelBorder(tile, Direction.BOTTOM));
			} else if (orientation.equals("e")) {
				map.add(new WhiteBorder(tile, Direction.LEFT));
				map.add(new PanelBorder(tile, Direction.RIGHT));
				map.add(new PanelBorder(tile, Direction.TOP));
				map.add(new PanelBorder(tile, Direction.BOTTOM));
			} else if (orientation.equals("w")) {
				map.add(new PanelBorder(tile, Direction.LEFT));
				map.add(new WhiteBorder(tile, Direction.RIGHT));
				map.add(new PanelBorder(tile, Direction.TOP));
				map.add(new PanelBorder(tile, Direction.BOTTOM));
			}
			return map;
		}
	}, CROSS {
		@Override
		public List<Border> getBorders(String orientation, Tile tile) {
			List<Border> map = new ArrayList<Border>();
			map.add(new WhiteBorder(tile, Direction.LEFT));
			map.add(new WhiteBorder(tile, Direction.RIGHT));
			map.add(new WhiteBorder(tile, Direction.TOP));
			map.add(new WhiteBorder(tile, Direction.BOTTOM));
			return map;
		}
	}, CLOSED {
		@Override
		public List<Border> getBorders(String orientation, Tile tile) {
			List<Border> map = new ArrayList<Border>();
			map.add(new PanelBorder(tile, Direction.LEFT));
			map.add(new PanelBorder(tile, Direction.RIGHT));
			map.add(new PanelBorder(tile, Direction.TOP));
			map.add(new PanelBorder(tile, Direction.BOTTOM));
			return map;
		}
	}, SEESAW {
		@Override
		public List<Border> getBorders(String orientation, Tile tile) {
			orientation = orientation.toLowerCase();
			List<Border> map = new ArrayList<Border>();
			if (orientation.equals("n")) {
				map.add(new PanelBorder(tile, Direction.LEFT));
				map.add(new PanelBorder(tile, Direction.RIGHT));
				map.add(new SeesawBorder(tile, Direction.TOP));
				map.add(new WhiteBorder(tile, Direction.BOTTOM));
			} else if (orientation.equals("s")) {
				map.add(new PanelBorder(tile, Direction.LEFT));
				map.add(new PanelBorder(tile, Direction.RIGHT));
				map.add(new WhiteBorder(tile, Direction.TOP));
				map.add(new SeesawBorder(tile, Direction.BOTTOM));
			} else if (orientation.equals("e")) {
				map.add(new WhiteBorder(tile, Direction.LEFT));
				map.add(new SeesawBorder(tile, Direction.RIGHT));
				map.add(new PanelBorder(tile, Direction.TOP));
				map.add(new PanelBorder(tile, Direction.BOTTOM));
			} else if (orientation.equals("w")) {
				map.add(new SeesawBorder(tile, Direction.LEFT));
				map.add(new WhiteBorder(tile, Direction.RIGHT));
				map.add(new PanelBorder(tile, Direction.TOP));
				map.add(new PanelBorder(tile, Direction.BOTTOM));
			}
			return map;
		}
	};
	
	public abstract List< Border> getBorders(String orientation, Tile tile);
	
	public static MazePart getPartFromString(String string) {
		return valueOf(string.toUpperCase());
	}
	
	public static String getToken(Map<Direction, Border> borders, Tile tile) {
		if (borders.size() != 4)
			throw new IllegalArgumentException();
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
			return "closed";
		if (wcount == 4)
			return "cross";
		if (wcount == 3){
			if (borders.get(Direction.TOP) instanceof PanelBorder) {
				return "T.N";
			} else if (borders.get(Direction.RIGHT) instanceof PanelBorder) {
				return "T.E";
			} else if (borders.get(Direction.BOTTOM) instanceof PanelBorder) {
				return "T.S";
			} else if (borders.get(Direction.LEFT) instanceof PanelBorder) {
				return "T.W";
			}
		}
		if (wcount == 2){
			if (borders.get(Direction.TOP) instanceof WhiteBorder &&
					borders.get(Direction.BOTTOM) instanceof WhiteBorder) {
				return "Straight.N";
			} else if (borders.get(Direction.RIGHT) instanceof WhiteBorder &&
					borders.get(Direction.LEFT) instanceof WhiteBorder) {
				return "Straight.E";
			} else if (borders.get(Direction.BOTTOM) instanceof WhiteBorder &&
					borders.get(Direction.TOP) instanceof WhiteBorder) {
				return "Straight.S";
			} else if (borders.get(Direction.LEFT) instanceof WhiteBorder &&
					borders.get(Direction.RIGHT) instanceof WhiteBorder) {
				return "Straight.W";
			} else if (borders.get(Direction.TOP) instanceof WhiteBorder &&
					borders.get(Direction.LEFT) instanceof WhiteBorder) {
				return "Corner.N";
			} else if (borders.get(Direction.RIGHT) instanceof WhiteBorder &&
					borders.get(Direction.TOP) instanceof WhiteBorder) {
				return "Corner.E";
			} else if (borders.get(Direction.BOTTOM) instanceof WhiteBorder &&
					borders.get(Direction.RIGHT) instanceof WhiteBorder) {
				return "Corner.S";
			} else if (borders.get(Direction.LEFT) instanceof WhiteBorder &&
					borders.get(Direction.TOP) instanceof WhiteBorder) {
				return "Corner.W";
			}
		}
		if (bcount == 3){
			if (borders.get(Direction.TOP) instanceof WhiteBorder) {
				return "Deadend.N";
			} else if (borders.get(Direction.RIGHT) instanceof WhiteBorder) {
				return "Deadend.E";
			} else if (borders.get(Direction.BOTTOM) instanceof WhiteBorder) {
				return "Deadend.S";
			} else if (borders.get(Direction.LEFT) instanceof WhiteBorder) {
				return "Deadend.W";
			}
		}
		if (scount == 1){
			if (borders.get(Direction.TOP) instanceof SeesawBorder &&
					borders.get(Direction.BOTTOM) instanceof WhiteBorder) {
				return "Seesaw.N";
			} else if (borders.get(Direction.RIGHT) instanceof SeesawBorder &&
					borders.get(Direction.LEFT) instanceof WhiteBorder) {
				return "Seesaw.E";
			} else if (borders.get(Direction.BOTTOM) instanceof SeesawBorder &&
					borders.get(Direction.TOP) instanceof WhiteBorder) {
				return "Seesaw.S";
			} else if (borders.get(Direction.LEFT) instanceof SeesawBorder &&
					borders.get(Direction.RIGHT) instanceof WhiteBorder) {
				return "Seesaw.W";
			}
		}
		
		return "";
	}
}
