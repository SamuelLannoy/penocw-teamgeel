package field.fieldmerge;

import java.util.Collection;

import field.Border;
import field.Field;
import field.Position;
import field.Tile;
import field.fromfile.MazePart;

public class TileConverter {
	
	
	public static void convertToFieldTile(peno.htttp.Tile tileMsg, Field field) {
		String[] split = tileMsg.getToken().split("\\.");
		MazePart part = MazePart.getPartFromString(split[0]);
		
		Position tilePos = new Position((int)tileMsg.getX(), (int)tileMsg.getY());
		Tile added = null;
		if (field.canHaveAsTile(tilePos)){
			added = new Tile(tilePos);
			field.addTile(added);
		} else {
			added = field.getTileMap().getObjectAtId(tilePos);
		}
		
		Collection<Border> borders = part.getBorders(split.length >= 2 ? split[1] : "", added);
		
		field.addBorders(borders);
	}
	
	

}
