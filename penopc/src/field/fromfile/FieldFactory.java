package field.fromfile;

import java.io.*;
import java.util.List;

import field.*;

public class FieldFactory {


	public static Field fieldFromFile(String path) throws IOException {
		Field field = new Field();
		FileInputStream fstream = new FileInputStream(path);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		int lineNr = 0;
		int dimX = 0;
		int dimY = 0;	
		int currY = 0;		
		while ((strLine = br.readLine()) != null)   {
			//System.out.println(""+lineNr);
			if (lineNr == 0) {
				String[] dim = strLine.split("( |\t)+");
				dimX = Integer.parseInt(dim[0]);
				dimY = Integer.parseInt(dim[1]);	
				currY = dimY-1;
			} else {
				if (!strLine.isEmpty() && !strLine.startsWith("#")) {
					String[] sections = strLine.split("( |\t)+");
					for (int i = 0; i < dimX; i++) {
						//System.out.println("line: "+sections[i]);
						String[] parts = sections[i].split("\\.");
						//System.out.println("section: " + sections[i] + " parts " + parts.length);
						MazePart part = MazePart.getPartFromString(parts[0]);
						Tile tile = new Tile(i, currY);
						//System.out.println("x: " + i + " y: " + currY);
						field.addTile(tile);
						if (parts.length >= 3 && !parts[2].isEmpty()) {
							if (parts[2].equals("V")) {
								field.addBall(new Ball(1), tile.getPosition());
							} else if (parts[2].startsWith("S")) {
								String start = parts[2];
								int id = Integer.parseInt(start.substring(1, 2));
								field.setStartPos(id, tile.getPosition());
								String dir = start.substring(2, 3);
								field.setStartDir(id, Direction.fromString(dir));
							} else {
								tile.setBarcode(new Barcode(Integer.parseInt(parts[2])));
							}
						}
						if (parts.length == 2) {
							if (parts[1].startsWith("S") && parts[1].length() > 1) {
								String start = parts[1];
								//System.out.println("tt " + start);
								int id = Integer.parseInt(start.substring(1, 2));
								field.setStartPos(id, tile.getPosition());
								String dir = start.substring(2, 3);
								field.setStartDir(id, Direction.fromString(dir));
							}
						}
						String param = parts.length >= 2 ? parts[1] : "";
						List<Border> borders = part.getBorders(param, tile);
						//System.out.println("" + borders + " param: " + param);
						field.addBorders(borders);
					}
					currY--;
				}
			}
			lineNr++;
			
		}
		in.close();
		//field.initSeesaw();
		return field;
	}
}
