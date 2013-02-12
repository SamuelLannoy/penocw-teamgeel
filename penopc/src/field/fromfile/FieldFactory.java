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
						String[] parts = sections[i].split("\\.");
						//System.out.println("section: " + sections[i] + " parts " + parts.length);
						MazePart part = MazePart.getPartFromString(parts[0]);
						Tile tile = new Tile(i, currY);
						//System.out.println("x: " + i + " y: " + currY);
						if (parts.length >= 3 && !parts[2].isEmpty()) {
							tile.setBarcode(new Barcode(Integer.parseInt(parts[2])));
						}
						field.addTile(tile);
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
		return field;
	}
}
