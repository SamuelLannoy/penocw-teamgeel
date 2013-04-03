package field.fieldmerge;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import field.Barcode;
import field.Border;
import field.Direction;
import field.Field;
import field.TilePosition;
import field.Tile;
import field.fromfile.MazePart;

public class TileConverter {
	
	public static peno.htttp.Tile convertToTileMsg(Tile tile, Field field) {
		Map<Direction, Border> borders = new HashMap<Direction, Border>();
		
		try {
		borders.put(Direction.TOP, field.getTopBorderOfTile(tile));
		borders.put(Direction.BOTTOM, field.getBottomBorderOfTile(tile));
		borders.put(Direction.LEFT, field.getLeftBorderOfTile(tile));
		borders.put(Direction.RIGHT, field.getRightBorderOfTile(tile));
		
		String sentToken = MazePart.getToken(borders, tile);
		System.out.println("sent: " + sentToken);
		if (sentToken.equals(""))
			return null;
		
		return new peno.htttp.Tile(tile.getPosition().getX(),
				tile.getPosition().getY(),
				sentToken);
		
		} catch (IllegalArgumentException e) {

			
			return new peno.htttp.Tile(tile.getPosition().getX(),
					tile.getPosition().getY(),
					"unknown");
		}
	}
	
	

}
