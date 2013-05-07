package gui.tools;

import robot.*;
import robot.brain.Explorer;
import field.*;
import field.simulation.FieldSimulation;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.text.AttributedCharacterIterator;
import java.util.ConcurrentModificationException;
import java.util.List;

import peno.htttp.PlayerClient;


/*
 * Custom canvas klasse om de map van het doolhof
 * weer te geven.
 */
public class DrawCanvas extends FieldCanvas{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RobotPool robotPool;
	private FieldSimulation field;
	
	

	public DrawCanvas(RobotPool robotPool, String title){
		setRobotPool(robotPool);
		setTitle(title);
		this.setVisible(true);
	}
	
	public void setRobotPool(RobotPool robotPool) {
		if (robotPool != null) {
			this.robotPool = robotPool;
			//setField(robotPool.getMainRobot().getField());
		}
	}
	
	public void setField(FieldSimulation field) {
		this.field = field;
	}

	/*protected void setField(Field field) {
		this.field = field;
	}*/
	
	// Tekent de map van het doolhof zoals ze op dit moment bekend is.
	@Override
	public void paint(Graphics g){ 
		createBufferStrategy(2);
		if (robotPool != null && robotPool.getMainRobot().isConnectedToGame()) {
			if (robotPool.getMainRobot().getField().isMerged()) {
				setTitle("Player's View - Merged");
			}
			try{
				//if (robotPool.getMainRobot().getClient().isPlaying()) {
				paintTitle(g);
				rescale(robotPool.getMainRobot().getField());
				paintTiles(g);
				paintBorders(g);
				paintPos(g);
				paintObjects(g);
				shortestPath(g);
			} catch (ConcurrentModificationException e) {

			}
		}
	}
	
	// TODO add this somewhere else (new canvas?)
	/*private void drawLobby(Graphics g) {
		PlayerClient client = robotPool.getMainRobot().getClient();
		
		if (client.isPaused()) {
			g.drawString("PAUSED", 80, 160);
		}
		
		g.drawString("players in lobby:", 20, 20);
		int i = 1;
		for (String player : client.getPlayers()) {
			String drawString = "Player " + i + ": " + player;
			g.drawString(drawString, 20, 40 + 20 * i);
			i++;
		}
	}*/
	
	

	
	
	// Tekent de huidige posities op de map. De robots als rechthoek.
		private void paintPos(Graphics g){
			for (RobotModel currentRobot : robotPool){
			//Graphics2D g2 = (Graphics2D)g;  
			//g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  
	               // RenderingHints.VALUE_ANTIALIAS_ON); 
			int x = (int)( currentRobot.getPosition().getPosX()
					+ currentRobot.getCurrTile().getPosition().getX() * 40);
			int y = (int)( currentRobot.getPosition().getPosY()
					+ currentRobot.getCurrTile().getPosition().getY() * 40);
			double r = currentRobot.getPosition().getRotationRadian() + (Math.PI/2);
			int[] drawXs;
			int[] drawYs;
			if (currentRobot != robotPool.getMainRobot()){
				/*x -= robotPool.getMainRobot().getStartPos().getPosX();
				y -= robotPool.getMainRobot().getStartPos().getPosY();*/

				TilePosition start = field.getStartPos(robotPool.getMainRobot().getPlayerNr());

				TilePosition other = new TilePosition(
							currentRobot.getCurrTile().getPosition().getX() - start.getX(),
							currentRobot.getCurrTile().getPosition().getY() - start.getY()
						);

				double rr = field.getStartDir(robotPool.getMainRobot().getPlayerNr()).toAngle();
				r -= rr * Math.PI / 180;
				int[] newpos = new int[] {(int)other.getX(),(int)other.getY()};
				//DebugBuffer.addInfo("pos: " + (r * 180 / Math.PI) + " " + rr);
				switch(field.getStartDir(robotPool.getMainRobot().getPlayerNr())) {
					case BOTTOM:
						newpos = new int[] {-(int)other.getX(),-(int)other.getY()};
						break;
					case LEFT:
						newpos = new int[] {(int)other.getY(),-(int)other.getX()};
						break;
					case RIGHT:
						newpos = new int[] {-(int)other.getY(),(int)other.getX()};
						break;
					case TOP:
						break;
					default:
						break;

				}
				x = newpos[0] * 40;
				y = newpos[1] * 40;
				//DebugBuffer.addInfo("pos: " + x + " " + y);
				double[] xs = currentRobot.getCornersX(field.getStartDir(robotPool.getMainRobot().getPlayerNr()).toAngle());
				double[] ys = currentRobot.getCornersY(field.getStartDir(robotPool.getMainRobot().getPlayerNr()).toAngle());
				drawXs = new int[4];
				drawYs = new int[4];
				for (int i = 0; i < 4; i++){
					drawXs[i] = (int)((getStartX() + (xs[i] * getScale()) + (x * getScale())));
					drawYs[i] = (int)((getStartY() - (ys[i] * getScale()) - (y * getScale())));
				}
			} else {
				double[] xs = currentRobot.getCornersX();
				double[] ys = currentRobot.getCornersY();
				drawXs = new int[4];
				drawYs = new int[4];
				for (int i = 0; i < 4; i++){
					drawXs[i] = (int)((getStartX() + (xs[i] * getScale()) + (x * getScale())));
					drawYs[i] = (int)((getStartY() - (ys[i] * getScale()) - (y * getScale())));
				}
			}
			/*System.out.println("paintpos " + x + ", " + y);
			System.out.println("startpos " + getStartX() + ", " + getStartY());
			System.out.println("getScale() " + getScale() );*/
			Polygon robotSurface = new Polygon(drawXs, drawYs, 4);
			if (currentRobot == robotPool.getMainRobot()){
				g.setColor(Color.GREEN);
			} else {
				g.setColor(Color.BLUE);
			}
			g.fillPolygon(robotSurface);
			// robot heeft object bij.
			if (currentRobot.hasBall()){
				g.setColor(Color.YELLOW);
				g.fillOval((int)(getStartX() + x * getScale()), (int)(getStartY() - y * getScale()), getBorderWidth(), getBorderWidth());
			}
			g.setColor(Color.BLACK);
			g.drawLine((int) ((x * getScale()) + getStartX()), (int) (getStartY() - (y * getScale())), (int) ((getScale() * x) + getStartX() - (getBorderWidth() * Math.cos(r))), (int) (getStartY() - (getScale() * y) - (getBorderWidth() * Math.sin(r))));
			}

			//current tile
			int x = robotPool.getMainRobot().getCurrTile().getPosition().getX();
			int y = robotPool.getMainRobot().getCurrTile().getPosition().getY();
			g.setColor(Color.ORANGE);
			g.drawRect((getStartX() - getHalfTileSize())  + (x * (getTileSize())),(getStartY() - getHalfTileSize()) - (y * (getTileSize())), getTileSize(), getTileSize());

			// teammate current tile
			if (robotPool.getMainRobot().hasTeamMate() && robotPool.getMainRobot().getField().isMerged()) {
				x = robotPool.getMainRobot().getTeamMate().getCurrTile().getPosition().getX();
				y = robotPool.getMainRobot().getTeamMate().getCurrTile().getPosition().getY();
				g.setColor(Color.GREEN);
				g.drawRect((getStartX() - getHalfTileSize())  + (x * (getTileSize())),(getStartY() - getHalfTileSize()) - (y * (getTileSize())), getTileSize(), getTileSize());
			}

			// draw explore tiles
			int i = 1;
			try {
			synchronized (Explorer.getToExplore()) {
			for (TilePosition tilePos : Explorer.getToExplore()) {
				x = tilePos.getX();
				y = tilePos.getY();
				g.setColor(Color.CYAN);
				g.drawRect((getStartX() - getHalfTileSize())  + (x * (getTileSize())),(getStartY() - getHalfTileSize()) - (y * (getTileSize())), getTileSize(), getTileSize());
				g.drawString(""+i, (getStartX())  + (x * (getTileSize())),(getStartY()) - (y * (getTileSize())));
				i++;
			}
			}
			} catch (ConcurrentModificationException e) {

			}

			// draw robot spotted tiles
			for (TilePosition tilePos : robotPool.getMainRobot().getRobotSpottedTiles()) {
				x = tilePos.getX();
				y = tilePos.getY();
				g.setColor(Color.RED);
				g.drawRect((getStartX() - getHalfTileSize())  + (x * (getTileSize())),(getStartY() - getHalfTileSize()) - (y * (getTileSize())), getTileSize(), getTileSize());
			}

			//ghost
			/*if (robotPool.getMainRobot().isSim()){
				x = (int) robotPool.getMainRobot().getSimX() - (int) robotPool.getMainRobot().getStartx();
				y = (int) robotPool.getMainRobot().getSimY() - (int) robotPool.getMainRobot().getStarty();
				double r = robotPool.getMainRobot().getSimAngle() + (Math.PI/2);
				g.setColor(Color.CYAN);
				g.drawLine((int) ((x * getScale()) + getStartX()), (int) (getStartY() - (y * getScale())), (int) ((getScale() * x) + getStartX() - (getBorderWidth() * Math.cos(r))), (int) (getStartY() - (getScale() * y) - (getBorderWidth() * Math.sin(r))));
				g.fillOval((int) ((x * getScale()) + (getStartX() - getHalfBorderWidth())), (int) ((getStartY() - getHalfBorderWidth()) - (y * getScale())), getBorderWidth(), getBorderWidth());
			}*/
		}

	
	// Tekent alle bekende tegels op de map.
	private void paintTiles(Graphics g){
		fieldDrawer.drawTiles(g, robotPool.getMainRobot().getField(), this);
	}
	
	// Tekent alle bekende muren op de map.
	private void paintBorders(Graphics g){
		fieldDrawer.drawBorders(g, robotPool.getMainRobot().getField(), this);
	}
	
	// tekent de balletjes in het doolhof
	private void paintObjects(Graphics g){
		fieldDrawer.drawObjects(g, robotPool.getMainRobot().getField(), this);
	}
	
	private void shortestPath(Graphics g){
		if (robotPool.getMainRobot().getAStarTileList() != null){
			g.setColor(Color.CYAN);
			List<Tile> list = robotPool.getMainRobot().getAStarTileList();
			for (int i = 0; i < list.size()-1; i++){
				TilePosition pos1 = list.get(i).getPosition();
				TilePosition pos2 = list.get(i+1).getPosition();
				int x1 = getStartX() + (pos1.getX() * (getTileSize()));
				int y1 = getStartY() - (pos1.getY() * (getTileSize()));
				int x2 = getStartX() + (pos2.getX() * (getTileSize()));
				int y2 = getStartY() - (pos2.getY() * (getTileSize()));
				g.drawLine(x1, y1, x2, y2);
			}
		}
		g.setColor(Color.BLACK);
	}
	
	@Override
	public void update(Graphics g) {
		super.update(g);
	}
}
