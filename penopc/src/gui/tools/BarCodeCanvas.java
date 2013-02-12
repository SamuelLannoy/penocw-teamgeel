package gui.tools;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

import robot.Robot;
import field.Barcode;

//Tekent de barcode van de huidige tegel.
public class BarCodeCanvas extends Canvas{
	
	private Robot robot;
	private Barcode barcode;
	private int height;
	private int width;
	private int barWidth;
	
	
	public BarCodeCanvas(Robot robot){
		setRobot(robot);
		this.setVisible(true);
	}
	
	public void setRobot(Robot robot){
		if (robot != null) {
			this.robot = robot;
		}
	}
	
	public void paint(Graphics g){
		if (robot != null) {
			paintBarCode(g);
		}
	}
	
	public void paintBarCode(Graphics g){
		if (!(robot.getCurrTile().getBarcode() == null)){
			height = getHeight();
			width = getWidth();
			barWidth = width / 8;
			//System.out.println("painting");
			//System.out.println("" + height + " " + width + " " + barWidth);
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, barWidth, height);
			g.fillRect(7 * barWidth, 0, barWidth, height);
			barcode = robot.getCurrTile().getBarcode();
			int[] code = barcode.getCode();
			for (int i = 0; i < 6; i++){
				if (code[i] == 0){
					g.setColor(Color.BLACK);
					g.fillRect((i + 1) * barWidth, 0, barWidth, height);
				}
			}
		} else {
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, width, height);
			g.setColor(Color.BLACK);
		}
	}
	
	@Override
	public void update(Graphics g) {
		super.update(g);
	}
}	
