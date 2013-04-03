package field.fieldmerge;

import java.util.List;

import robot.Robot;

import field.Border;
import field.Field;
import field.TilePosition;
import field.Tile;
import field.representation.FieldRepresentation;

public class FieldMerger {

	/*public static FieldRepresentation mergeFields(Robot robot, FieldRepresentation field2) {
		FieldRepresentation retF = new FieldRepresentation();
		FieldRepresentation field1 = robot.getField();
		
		List<BarcodeNode> bc1 = field1.getBarcodes();
		List<BarcodeNode> bc2 = field2.getBarcodes();
		
		bc1.retainAll(bc2);
		bc2.retainAll(bc1);
		
		if (bc1.size() >= 2) {
			BarcodeNode barcodeNode1 = bc1.get(0);
			System.out.println("first " + barcodeNode1);
			
			for (BarcodeNode barcodeNode2 : bc2) {
				if (barcodeNode1.equals(barcodeNode2)) {
					System.out.println("first " + barcodeNode2);
					int diffX = barcodeNode1.getPosition().getX() - barcodeNode2.getPosition().getX();
					int diffY = barcodeNode1.getPosition().getY() - barcodeNode2.getPosition().getY();
					field2 = field2.moveX(diffX);
					field2 = field2.moveY(diffY);
					System.out.println("x " + diffX + " y " + diffY);
					robot.setTranslX(diffX);
					robot.setTranslY(diffY);
				}
			}
			
			bc2 = field2.getBarcodes();
			bc2.retainAll(bc1);
			
			BarcodeNode barcodeNode1_2 = bc1.get(1);
			System.out.println("second " + barcodeNode1_2);

			for (BarcodeNode barcodeNode2 : bc2) {
				if (barcodeNode1_2.equals(barcodeNode2)) {
					System.out.println("second " + barcodeNode2);
					double P12 = TilePosition.euclDistance(barcodeNode1.getPosition(), barcodeNode1_2.getPosition());
					System.out.println("rot " + P12);
					double P13 = TilePosition.euclDistance(barcodeNode1.getPosition(), barcodeNode2.getPosition());
					System.out.println("rot " + P13);
					double P23 = TilePosition.euclDistance(barcodeNode1_2.getPosition(), barcodeNode2.getPosition());
					System.out.println("rot " + P23 + " " + barcodeNode1_2.getPosition() + " " + barcodeNode2.getPosition());
					int rotation = (int)(Math.acos((Math.pow(P12, 2) + Math.pow(P13, 2) - Math.pow(P23, 2)) / (2 * P12 * P13)) / Math.PI * 180 + .5);
					System.out.println("rot " + rotation);
					field2 = field2.rotate(rotation, barcodeNode1.getPosition());
					robot.setRotation(rotation);
				}
			}
			

			
			for (Tile tile : field1.getTileMap()) {
				if (retF.canHaveAsTile(tile.getPosition())) {
					retF.addTile(tile);
				}
			}
			for (Border border : field1.getBorderMap()) {
				if (retF.canHaveAsBorder(border.getBorderPos())) {
					retF.addBorder(border);
				}
			}
			
			
			for (Tile tile : field2.getTileMap()) {
				if (retF.canHaveAsTile(tile.getPosition())) {
					retF.addTile(tile);
				}
			}
			for (Border border : field2.getBorderMap()) {
				if (retF.canHaveAsBorder(border.getBorderPos())) {
					retF.addBorder(border);
				} else {
					if (retF.getBorderMap().getObjectAtId(border.getBorderPos()).getClass() != border.getClass()) {
						retF.makeUnsure(border.getBorderPos());
					}
				}
			}
			
			
		} else {
			// explore more
			throw new IllegalStateException();
		}
		return retF;
	}*/
}
