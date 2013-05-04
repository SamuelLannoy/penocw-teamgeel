package gui.tools;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

import field.Field;
import field.Tile;

public abstract class FieldCanvas extends Canvas  {
	
	public abstract void paint(Graphics g);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6251652537644441987L;
	
	private int tileSize;
	private int borderWidth;
	private int halfTileSize;
	private int halfArrow;
	private int boardSize;
	private int halfBoardSize;
	private int halfBorderWidth;
	private double scale;
	private int bar;
	private int startX;
	private int startY;
	private int barStart;
	private int barEnd;
	private String title;
	protected FieldDrawer fieldDrawer = new FieldDrawer();
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public String getTitle(){
		return title;
	}
	
	public int getTileSize() {
		return tileSize;
	}

	public void setTileSize(int tileSize) {
		this.tileSize = tileSize;
	}

	public int getBorderWidth() {
		return borderWidth;
	}

	public void setBorderWidth(int borderWidth) {
		this.borderWidth = borderWidth;
	}

	public int getHalfTileSize() {
		return halfTileSize;
	}

	public void setHalfTileSize(int halfTileSize) {
		this.halfTileSize = halfTileSize;
	}

	public int getHalfArrow() {
		return halfArrow;
	}

	public void setHalfArrow(int halfArrow) {
		this.halfArrow = halfArrow;
	}

	public int getBoardSize() {
		return boardSize;
	}

	public void setBoardSize(int boardSize) {
		this.boardSize = boardSize;
	}

	public int getHalfBoardSize() {
		return halfBoardSize;
	}

	public void setHalfBoardSize(int halfBoardSize) {
		this.halfBoardSize = halfBoardSize;
	}

	public int getHalfBorderWidth() {
		return halfBorderWidth;
	}

	public void setHalfBorderWidth(int halfBorderWidth) {
		this.halfBorderWidth = halfBorderWidth;
	}

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	public int getBar() {
		return bar;
	}

	public void setBar(int bar) {
		this.bar = bar;
	}

	public int getStartX() {
		return startX;
	}

	public void setStartX(int startX) {
		this.startX = startX;
	}

	public int getStartY() {
		return startY;
	}

	public void setStartY(int startY) {
		this.startY = startY;
	}

	public int getBarStart() {
		return barStart;
	}

	public void setBarStart(int barStart) {
		this.barStart = barStart;
	}

	public int getBarEnd() {
		return barEnd;
	}

	public void setBarEnd(int barEnd) {
		this.barEnd = barEnd;
	}
	
	// zoekt de uiterste afmetingen van het doolhof en herschaald de map hieraan.
	public void rescale(Field field){
		int maxX = 0;
		int maxY = 0;
		int minX = 0;
		int minY = 0;
		for (Tile currentTile : field.getTileMap()){
			int x = currentTile.getPosition().getX();
			int y = currentTile.getPosition().getY();
			if (x > maxX){
				maxX = x;
			}
			if (y > maxY){
				maxY = y;
			}
			if (x < minX){
				minX = x;
			}
			if (y < minY){
				minY = y;
			}
		}
		int xDiff = maxX + Math.abs(minX);
		int yDiff = maxY + Math.abs(minY);
		int size = Math.max(xDiff, yDiff) + 1;
		setBoardSize(Math.min(getWidth(),getHeight()) - 10);
		setHalfBoardSize((int) (boardSize /2));
		setTileSize((int) (boardSize / (size + 1)));
		setBorderWidth((int) (tileSize * (.1)));
		setHalfTileSize((int) (tileSize / 2));
		setHalfArrow((int) (getHalfTileSize() / 2));
		setHalfBorderWidth((int) (borderWidth / 2));
		setScale((double)tileSize / 40.0);
		setStartX(5 + (tileSize * (Math.abs(minX) +1)));
		setStartY((boardSize - 5) - (tileSize * (Math.abs(minY) + 1)));
		setBar((int) (getScale() * 3.0));
		setBarStart((int) (getScale() * 8.0));
		setBarEnd(getBarStart() + (7 * getBar()));
	}
	
	protected void paintTitle(Graphics g) {
		paintTitle(g, getTitle());
	}
	
	protected void paintTitle(Graphics g, String title) {
		g.setColor(Color.WHITE);
		g.drawString(title, 20, 20);	
	}
}
