package field.representation;

import java.util.Collection;

import field.Barcode;
import field.Border;
import field.SeesawBorder;
import field.Tile;
import field.TilePosition;
import field.fromfile.MazePart;

public class PenoHtttpFieldRepresentation extends FieldRepresentation {
	
	public PenoHtttpFieldRepresentation(Collection<peno.htttp.Tile> tileList) {
		super();
		addFromComm(tileList);
	}
	
	public void addTilesFromTeammate(Collection<peno.htttp.Tile> tileList) {
		addFromComm(tileList);
	}
	
	private void addFromComm(Collection<peno.htttp.Tile> tileList) {
		for (peno.htttp.Tile tileMsg : tileList) {
			String[] split = tileMsg.getToken().split("\\.");
			MazePart part = MazePart.getPartFromString(split[0]);
			
			TilePosition tilePos = new TilePosition((int)tileMsg.getX(), (int)tileMsg.getY());
			Tile added = null;
			if (!hasTileAt(tilePos)){
				added = new Tile(tilePos);
				addTile(added);
			} else {
				added = getTileAt(tilePos);
			}
			
			if (split.length >= 3) {
				int nr = Integer.parseInt(split[2]);
				added.setBarcode(new Barcode(nr));
			}
			
			Collection<Border> borders = part.getBorders(split.length >= 2 ? split[1] : "", added);

			for (Border border : borders) {
				if (border instanceof SeesawBorder) {
					overWriteBorder(border);
				} else {
					addBorder(border);
				}
			}
		}
	}
	
}
