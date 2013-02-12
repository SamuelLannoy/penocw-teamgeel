package gui.tools;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

/*
 * Plot de gegeven data in een grafiek.
 */
public class PlotCanvas extends JPanel{
	private List<Integer> data;
	private final int PAD = 20;
	
	public PlotCanvas(List<Integer> data){
		this.data = data;
		this.setVisible(true);
	}
	
	public void setData(List<Integer> data){
		this.data = data;
	}
	
	// Tekent de plot
	protected void paintComponent(Graphics g) {  
        super.paintComponent(g);  
        Graphics2D g2 = (Graphics2D)g;  
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  
                            RenderingHints.VALUE_ANTIALIAS_ON);  
        int w = getWidth();  
        int h = getHeight();  
        g2.setPaint(Color.green);  
        g2.drawLine(PAD, PAD, PAD, h-PAD);  
        g2.drawLine(PAD, h-PAD, w-PAD, h-PAD);  
        double xScale = (w - 2*PAD)/(data.size() + 1);  
        double maxValue = 100.0;  
        double yScale = (h - 2*PAD)/maxValue;
        for (int i = 0; i < 6; i++){
        	g2.drawString("" + (i*20), 5, (int) (h-PAD-(i*20*yScale)));
        }
        // The origin location.  
        int x0 = PAD;  
        int y0 = h-PAD;  
        for(int j = 0; j < data.size(); j++) {  
            int x = x0 + (int)(xScale * (j+1));  
            int y = y0 - (int)(yScale * data.get(j));  
            g2.fillOval(x-2, y-2, 4, 4);  
        }  
    }  
	
	@Override
	public void update(Graphics g) {
		super.update(g);
	}
}
