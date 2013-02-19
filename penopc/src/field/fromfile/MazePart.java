package field.fromfile;

import java.util.ArrayList;
import java.util.List;

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
	};
	
	public abstract List< Border> getBorders(String orientation, Tile tile);
	
	public static MazePart getPartFromString(String string) {
		return valueOf(string.toUpperCase());
	}
}
