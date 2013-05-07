package gui.tools;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ConcurrentModificationException;

import field.Ball;
import field.Border;
import field.Direction;
import field.Field;
import field.ObjectMap;
import field.Tile;
import field.TilePosition;
import field.UnsureBorder;
import field.WhiteBorder;

public class FieldDrawer {

	public void drawTiles(Graphics g, Field field, FieldCanvas fieldCanvas) {
		synchronized(field.getTileMap()){
			try {
		for (Tile currentTile : field.getTileMap()){
			int x = currentTile.getPosition().getX();
			int y = currentTile.getPosition().getY();
			int pixelX = (fieldCanvas.getStartX() - fieldCanvas.getHalfTileSize())  + (x * (fieldCanvas.getTileSize()));
			int pixelY = (fieldCanvas.getStartY() - fieldCanvas.getHalfTileSize()) - (y * (fieldCanvas.getTileSize()));
			g.setColor(Color.BLACK);
			g.drawRect((fieldCanvas.getStartX() - fieldCanvas.getHalfTileSize())  + (x * (fieldCanvas.getTileSize())),(fieldCanvas.getStartY() - fieldCanvas.getHalfTileSize()) - (y * (fieldCanvas.getTileSize())), fieldCanvas.getTileSize(), fieldCanvas.getTileSize());
			if (!(currentTile.getBarcode() == null)){
				int[] code = currentTile.getBarcode().getCode();
				g.drawString("B", fieldCanvas.getStartX() + (x * (fieldCanvas.getTileSize())), fieldCanvas.getStartY() - (y * (fieldCanvas.getTileSize())));
				Direction dir2 = null;
				for (Direction dir : Direction.values()){
					try {
						Border bord = field.getBorderInDirection(currentTile, dir);
						if (bord instanceof WhiteBorder) {
							dir2 = dir;
							break;
						}
					} catch (IllegalArgumentException top) {
						
					}
				}
				// check ori�ntatie barcode
				if (dir2 == Direction.TOP || dir2 == Direction.BOTTOM){
					// platte barcode ---
					g.setColor(Color.BLACK);
					g.fillRect(pixelX, (int) (pixelY + (fieldCanvas.getBarStart())), fieldCanvas.getTileSize(), fieldCanvas.getBar());
					g.fillRect(pixelX, (int) (pixelY + (fieldCanvas.getBarEnd())), fieldCanvas.getTileSize(), fieldCanvas.getBar());
					for (int i = 0; i < 6; i++){
						if (code[i] == 0){
							g.setColor(Color.BLACK);
						} else {
							g.setColor(Color.WHITE);
						}
						g.fillRect(pixelX, pixelY + fieldCanvas.getBarStart() + ((i + 1) * fieldCanvas.getBar()), fieldCanvas.getTileSize(), fieldCanvas.getBar());
						g.setColor(Color.RED);
						g.drawString(currentTile.getBarcode().getDecimal()+"", fieldCanvas.getStartX() + (x * (fieldCanvas.getTileSize())), fieldCanvas.getStartY() - (y * (fieldCanvas.getTileSize())));
						g.setColor(Color.BLACK);
					}
					
				} else {
					if (dir2 == Direction.LEFT || dir2 == Direction.RIGHT){
					// rechte barcode |||
						g.setColor(Color.BLACK);
						g.fillRect((int) (pixelX + (fieldCanvas.getBarStart())), pixelY , fieldCanvas.getBar(), fieldCanvas.getTileSize());
						g.fillRect((int) (pixelX + (fieldCanvas.getBarEnd())), pixelY , fieldCanvas.getBar(), fieldCanvas.getTileSize());
						for (int i = 0; i < 6; i++){
							if (code[i] == 0){
								g.setColor(Color.BLACK);
							} else {
								g.setColor(Color.WHITE);
							}
							g.fillRect(pixelX + fieldCanvas.getBarStart() + ((i + 1) * fieldCanvas.getBar()), pixelY, fieldCanvas.getBar(), fieldCanvas.getTileSize());
							g.setColor(Color.RED);
							g.drawString(currentTile.getBarcode().getDecimal()+"", fieldCanvas.getStartX() + (x * (fieldCanvas.getTileSize())), fieldCanvas.getStartY() - (y * (fieldCanvas.getTileSize())));
							g.setColor(Color.BLACK);
						}
					}
				}
				// vakje bevat object
				/**if (currentTile.hasObject()){ // code = object code
					int xBall = fieldCanvas.getStartX() + (x * (fieldCanvas.getTileSize()));
					int yBall = fieldCanvas.getStartY() - (y * (fieldCanvas.getTileSize()));
					if (true){// code = eigen object code
						g.setColor(Color.YELLOW);
					} else {
						g.setColor(Color.RED);
					}
					switch(dir2){
						case LEFT: xBall = xBall + fieldCanvas.getHalfTileSize() - fieldCanvas.getBorderWidth() ;
						case RIGHT: xBall = xBall - fieldCanvas.getHalfTileSize() + fieldCanvas.getBorderWidth();
						case TOP: yBall = yBall + fieldCanvas.getHalfTileSize() - fieldCanvas.getBorderWidth();
						case BOTTOM: yBall = yBall - fieldCanvas.getHalfTileSize() + fieldCanvas.getBorderWidth();
					}
					g.fillOval(xBall, yBall, fieldCanvas.getBorderWidth(), fieldCanvas.getBorderWidth());
					g.setColor(Color.BLACK);
				}**/
				// vakje onderdeel van wip
				/**if (currentTile.isWip()){
				   	int[] Xpnts = new int[6];
				   	int[] Ypnts = new int[6];
				   	// Alle 6 punten van de pijl in het midden van het vakje.
				   	for (int i = 0; i < 6; i++){
				   	 	Xpnts[i] = fieldCanvas.getStartX() + (x * (fieldCanvas.getTileSize()));
				   		Ypnts[i] = fieldCanvas.getStartY() - (y * (fieldCanvas.getTileSize()));
				   	}
				   	// Pas de nodige punten aan afhankelijk van de richting.
					field.Direction dir = currentTile.getDirection();
					switch (dir) {
						// onder boven links boven rechts boven
						case TOP:		Ypnts[0] = Ypnts[0] - halfArrow;
										Ypnts[1] = Ypnts[1] + halfArrow;
										Xpnts[2] = Xpnts[2] - halfArrow;
										Ypnts[3] = Ypnts[3] + halfArrow;
										Xpnts[4] = Xpnts[4] + halfArrow;
										Ypnts[5] = Ypnts[5] + halfArrow;
										break;
						// boven onder links onder rechts onder
						case BOTTOM:	Ypnts[0] = Ypnts[0] + halfArrow;
										Ypnts[1] = Ypnts[1] - halfArrow;
										Xpnts[2] = Xpnts[2] - halfArrow;
										Ypnts[3] = Ypnts[3] - halfArrow;
										Xpnts[4] = Xpnts[4] + halfArrow;
										Ypnts[5] = Ypnts[5] - halfArrow;
										break;
						// rechts links onder links boven links
						case LEFT:		Xpnts[0] = Xpnts[0] + halfArrow;
										Xpnts[1] = Xpnts[1] - halfArrow;
										Ypnts[2] = Ypnts[2] - halfArrow;
										Xpnts[3] = Xpnts[3] - halfArrow;
										Ypnts[4] = Ypnts[4] + halfArrow;
										Xpnts[5] = Xpnts[5] - halfArrow;
										break;
						// links rechts onder rechts boven rechts
						case RIGHT: 	Xpnts[0] = Xpnts[0] - halfArrow;
										Xpnts[1] = Xpnts[1] + halfArrow;
										Ypnts[2] = Ypnts[2] - halfArrow;
										Xpnts[3] = Xpnts[3] + halfArrow;
										Ypnts[4] = Ypnts[4] + halfArrow;
										Xpnts[5] = Xpnts[5] + halfArrow;
										break;
						}
					// draw arrow
					 * g.setColor(Color.BLACK);
					g.drawPolygon(Xpnts,Ypnts,6);
				}**/
			}
		}
			} catch (ConcurrentModificationException e) {
				
			}
		}
	}
	
	public void drawBorders(Graphics g, Field field, FieldCanvas fieldCanvas) {
		g.setColor(Color.WHITE);
		for (Border currentBorder : field.getBorderMap()){
			int x1 = currentBorder.getBorderPos().getPosition1().getX();
			int y1 = currentBorder.getBorderPos().getPosition1().getY();
			int x2 = currentBorder.getBorderPos().getPosition2().getX();
			int y2 = currentBorder.getBorderPos().getPosition2().getY();
			if (currentBorder.isPassable() && !(currentBorder instanceof UnsureBorder)){	
				if (x1 == x2){
					//platte border
					if (y1 < y2){
						g.fillRect((fieldCanvas.getStartX() - fieldCanvas.getHalfTileSize()) + (x1 * (fieldCanvas.getTileSize())), (fieldCanvas.getStartY() - fieldCanvas.getHalfTileSize() - fieldCanvas.getHalfBorderWidth()) - (y1 * (fieldCanvas.getTileSize())), fieldCanvas.getTileSize(), fieldCanvas.getBorderWidth());
					} else {
						g.fillRect((fieldCanvas.getStartX() - fieldCanvas.getHalfTileSize()) + (x1 * (fieldCanvas.getTileSize())), (fieldCanvas.getStartY() - fieldCanvas.getHalfTileSize() - fieldCanvas.getHalfBorderWidth()) - (y2 * (fieldCanvas.getTileSize())), fieldCanvas.getTileSize(), fieldCanvas.getBorderWidth());
					}	
				} else {
					//rechte border
					if (x1 < x2){
						g.fillRect((fieldCanvas.getStartX() + fieldCanvas.getHalfTileSize() - fieldCanvas.getHalfBorderWidth()) + (x1 * (fieldCanvas.getTileSize())), (fieldCanvas.getStartY() - fieldCanvas.getHalfTileSize()) - (y1 * (fieldCanvas.getTileSize())), fieldCanvas.getBorderWidth(), fieldCanvas.getTileSize());
					} else {
						g.fillRect((fieldCanvas.getStartX() + fieldCanvas.getHalfTileSize() - fieldCanvas.getHalfBorderWidth()) + (x2 * (fieldCanvas.getTileSize())), (fieldCanvas.getStartY() - fieldCanvas.getHalfTileSize()) - (y1 * (fieldCanvas.getTileSize())), fieldCanvas.getBorderWidth(), fieldCanvas.getTileSize());
					}
				}
			}
			
		}
		g.setColor(Color.BLACK);
		try{
			for (Border currentBorder : field.getBorderMap()){
				int x1 = currentBorder.getBorderPos().getPosition1().getX();
				int y1 = currentBorder.getBorderPos().getPosition1().getY();
				int x2 = currentBorder.getBorderPos().getPosition2().getX();
				int y2 = currentBorder.getBorderPos().getPosition2().getY();
				if (!currentBorder.isPassable() && !(currentBorder instanceof UnsureBorder)){
					if (x1 == x2){
						//platte border
						if (y1 < y2){
							g.fillRect((fieldCanvas.getStartX() - fieldCanvas.getHalfTileSize()) + (x1 * (fieldCanvas.getTileSize())), (fieldCanvas.getStartY() - fieldCanvas.getHalfTileSize() - fieldCanvas.getHalfBorderWidth()) - (y1 * (fieldCanvas.getTileSize())), fieldCanvas.getTileSize(), fieldCanvas.getBorderWidth());
						} else {
							g.fillRect((fieldCanvas.getStartX() - fieldCanvas.getHalfTileSize()) + (x1 * (fieldCanvas.getTileSize())), (fieldCanvas.getStartY() - fieldCanvas.getHalfTileSize() - fieldCanvas.getHalfBorderWidth()) - (y2 * (fieldCanvas.getTileSize())), fieldCanvas.getTileSize(), fieldCanvas.getBorderWidth());
						}	
					} else {
						//rechte border
						if (x1 < x2){
							g.fillRect((fieldCanvas.getStartX() + fieldCanvas.getHalfTileSize() - fieldCanvas.getHalfBorderWidth()) + (x1 * (fieldCanvas.getTileSize())), (fieldCanvas.getStartY() - fieldCanvas.getHalfTileSize()) - (y1 * (fieldCanvas.getTileSize())), fieldCanvas.getBorderWidth(), fieldCanvas.getTileSize());
						} else {
							g.fillRect((fieldCanvas.getStartX() + fieldCanvas.getHalfTileSize() - fieldCanvas.getHalfBorderWidth()) + (x2 * (fieldCanvas.getTileSize())), (fieldCanvas.getStartY() - fieldCanvas.getHalfTileSize()) - (y1 * (fieldCanvas.getTileSize())), fieldCanvas.getBorderWidth(), fieldCanvas.getTileSize());
						}
					}
				}
			}
			g.setColor(Color.GRAY);
			for (Border currentBorder : field.getBorderMap()){
				int x1 = currentBorder.getBorderPos().getPosition1().getX();
				int y1 = currentBorder.getBorderPos().getPosition1().getY();
				int x2 = currentBorder.getBorderPos().getPosition2().getX();
				int y2 = currentBorder.getBorderPos().getPosition2().getY();
				if (currentBorder instanceof UnsureBorder){
					if (x1 == x2){
						//platte border
						if (y1 < y2){
							g.fillRect((fieldCanvas.getStartX() - fieldCanvas.getHalfTileSize()) + (x1 * (fieldCanvas.getTileSize())), (fieldCanvas.getStartY() - fieldCanvas.getHalfTileSize() - fieldCanvas.getHalfBorderWidth()) - (y1 * (fieldCanvas.getTileSize())), fieldCanvas.getTileSize(), fieldCanvas.getBorderWidth());
						} else {
							g.fillRect((fieldCanvas.getStartX() - fieldCanvas.getHalfTileSize()) + (x1 * (fieldCanvas.getTileSize())), (fieldCanvas.getStartY() - fieldCanvas.getHalfTileSize() - fieldCanvas.getHalfBorderWidth()) - (y2 * (fieldCanvas.getTileSize())), fieldCanvas.getTileSize(), fieldCanvas.getBorderWidth());
						}	
					} else {
						//rechte border
						if (x1 < x2){
							g.fillRect((fieldCanvas.getStartX() + fieldCanvas.getHalfTileSize() - fieldCanvas.getHalfBorderWidth()) + (x1 * (fieldCanvas.getTileSize())), (fieldCanvas.getStartY() - fieldCanvas.getHalfTileSize()) - (y1 * (fieldCanvas.getTileSize())), fieldCanvas.getBorderWidth(), fieldCanvas.getTileSize());
						} else {
							g.fillRect((fieldCanvas.getStartX() + fieldCanvas.getHalfTileSize() - fieldCanvas.getHalfBorderWidth()) + (x2 * (fieldCanvas.getTileSize())), (fieldCanvas.getStartY() - fieldCanvas.getHalfTileSize()) - (y1 * (fieldCanvas.getTileSize())), fieldCanvas.getBorderWidth(), fieldCanvas.getTileSize());
						}
					}
				}
			}
		} catch (ConcurrentModificationException e) {
			
		}
	}
	
	public void drawObjects(Graphics g, Field field, FieldCanvas fieldCanvas) {
		ObjectMap<TilePosition, Ball> ballMap = field.getBallMap();
		ObjectMap<TilePosition, Tile> TileMap = field.getTileMap();
		for (TilePosition pos : ballMap.getKeys()){
			Tile currentTile = TileMap.getObjectAtId(pos);
			int xBall = fieldCanvas.getStartX() + (pos.getX() * (fieldCanvas.getTileSize()));
			int yBall = fieldCanvas.getStartY() - (pos.getY() * (fieldCanvas.getTileSize()));
			Direction dir2 = null;
			for (Direction dir : Direction.values()){
				try {
					Border bord = field.getBorderInDirection(currentTile, dir);
					if (bord instanceof WhiteBorder) {
						dir2 = dir;
						break;
					}
				} catch (IllegalArgumentException top) {
					
				}
			}
			switch(dir2){
			case LEFT: xBall = xBall + fieldCanvas.getHalfTileSize() - fieldCanvas.getBorderWidth() ;
			case RIGHT: xBall = xBall - fieldCanvas.getHalfTileSize() + fieldCanvas.getBorderWidth();
			case TOP: yBall = yBall + fieldCanvas.getHalfTileSize() - fieldCanvas.getBorderWidth();
			case BOTTOM: yBall = yBall - fieldCanvas.getHalfTileSize() + fieldCanvas.getBorderWidth();
		}
		g.setColor(Color.YELLOW);
		g.fillOval(xBall, yBall, fieldCanvas.getBorderWidth(), fieldCanvas.getBorderWidth());
		g.setColor(Color.BLACK);
		}
	}
	
}
