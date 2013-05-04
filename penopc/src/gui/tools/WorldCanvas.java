package gui.tools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ConcurrentModificationException;

import robot.Robot;
import robot.RobotModel;
import robot.RobotPool;

import field.simulation.FieldSimulation;

public class WorldCanvas extends FieldCanvas {
	private static final long serialVersionUID = 8007195457007033525L;
	
	private FieldSimulation world;
	private RobotPool robotPool;
	
	public WorldCanvas() {
		setTitle("World View");
	}
	
	public void setWorld(FieldSimulation world) {
		this.world = world;
		rescale(world);
	}
	
	public void setRobotPool(RobotPool robotPool) {
		this.robotPool = robotPool;
	}

	@Override
	public void paint(Graphics g) {
		createBufferStrategy(2);
		if (world != null) {
			try{
				if (world.getWinFlag() == -1) {
					if (!robotPool.getMainRobot().receivedTeamTiles()) {
						rescale(world);
						//if (robotPool.getMainRobot().getClient().isPlaying()) {
						paintTitle(g, "World View");
						fieldDrawer.drawTiles(g, world, this);
						fieldDrawer.drawBorders(g, world, this);
						if (robotPool.getMainRobot().getLobbyViewer().isPlaying()) {
							paintRobots(g);
						} else {
							paintStartPositions(g);
						}
						fieldDrawer.drawObjects(g, world, this);
					} else {
						paintTitle(g, "Teammate Field");
						rescale(robotPool.getMainRobot().getTeamMateField());
						fieldDrawer.drawTiles(g, robotPool.getMainRobot().getTeamMateField(), this);
						fieldDrawer.drawBorders(g, robotPool.getMainRobot().getTeamMateField(), this);
						fieldDrawer.drawObjects(g, robotPool.getMainRobot().getTeamMateField(), this);
					}
				} else {
					paintWin(g);
				}
			} catch (ConcurrentModificationException e) {

			}
		}
	}
	
	public void paintWin(Graphics g) {
		if (world.getWinFlag() == robotPool.getMainRobot().getTeamNr()) {
			g.setColor(Color.GREEN);
		} else {
			g.setColor(Color.RED);
		}
		g.drawString("VICTORY FOR TEAM " + world.getWinFlag(), 50, 50);	
	}
	
	public Color[] pcolors = new Color[] { Color.CYAN, Color.RED, Color.GREEN, Color.YELLOW };
	
	public void paintStartPositions(Graphics g) {
		for (int i = 1; i <= world.getPlayerNo(); i++) {
			int x = world.getStartPos(i).getX() * 40;
			int y = world.getStartPos(i).getY() * 40;
			double r = (world.getStartDir(i).toAngle() * Math.PI / 180) + (Math.PI/2);
			g.setColor(pcolors[i-1]);
			g.drawLine((int) ((x * getScale()) + getStartX()), (int) (getStartY() - (y * getScale())), (int) ((getScale() * x) + getStartX() - (getBorderWidth() * Math.cos(r))), (int) (getStartY() - (getScale() * y) - (getBorderWidth() * Math.sin(r))));
			g.fillOval((int) ((x * getScale()) + (getStartX() - getHalfBorderWidth())), (int) ((getStartY() - getHalfBorderWidth()) - (y * getScale())), getBorderWidth(), getBorderWidth());
			/*x+=10;
			y+=10;*/
			//System.out.println("x " + (getStartX()  + x * getTileSize()) + 
				//	" y " + (getStartY() - y * getTileSize()));
			//g.drawString("P"+i, (getStartX())  + (x * (getTileSize())),(getStartY()) - (y * (getTileSize())));
			g.drawString("P"+i, 100+20*i,25);
		}
	}
	
	public void paintRobots(Graphics g) {
		for (RobotModel currentRobot : robotPool.getOtherRobots()) {
			int x = (int)(currentRobot.getCurrTile().getPosition().getX() * 40);
			int y = (int)(currentRobot.getCurrTile().getPosition().getY() * 40);
			double r = currentRobot.getPosition().getRotationRadian() + (Math.PI/2);
			int[] drawXs = new int[4];
			int[] drawYs = new int[4];
			double[] xs = currentRobot.getCornersX();
			double[] ys = currentRobot.getCornersY();
			for (int i = 0; i < 4; i++){
				drawXs[i] = (int)((getStartX() + (xs[i] * getScale()) + (x * getScale())));
				drawYs[i] = (int)((getStartY() - (ys[i] * getScale()) - (y * getScale())));
			}
			Polygon robotSurface = new Polygon(drawXs, drawYs, 4);
			g.setColor(Color.BLUE);
			g.fillPolygon(robotSurface);
			if (currentRobot.hasBall()){
				g.setColor(Color.YELLOW);
				g.fillOval((int)(getStartX() + x * getScale()), (int)(getStartY() - y * getScale()), getBorderWidth(), getBorderWidth());
			}
			g.setColor(Color.BLACK);
			g.drawLine((int) ((x * getScale()) + getStartX()),
					(int) (getStartY() - (y * getScale())),
					(int) ((getScale() * x) + getStartX() - (getBorderWidth() * Math.cos(r))),
					(int) (getStartY() - (getScale() * y) - (getBorderWidth() * Math.sin(r))));
		}
		Robot currentRobot = robotPool.getMainRobot();
		int[] newpos = convertRelativeToAbsolutePosition(currentRobot.getCurrTile().getPosition().getX(),
				currentRobot.getCurrTile().getPosition().getY(),
				world,
				robotPool.getMainRobot().getPlayerNr());
		int x = (int)(newpos[0] * 40);
		int y = (int)(newpos[1] * 40);
		double r = currentRobot.getPosition().getRotationRadian() + (Math.PI/2)
				+ (world.getStartDir(robotPool.getMainRobot().getPlayerNr()).toAngle() * Math.PI / 180);
		int[] drawXs = new int[4];
		int[] drawYs = new int[4];
		double[] xs = currentRobot.getCornersX(world.getStartDir(robotPool.getMainRobot().getPlayerNr()).toAngle());
		double[] ys = currentRobot.getCornersY(world.getStartDir(robotPool.getMainRobot().getPlayerNr()).toAngle());
		for (int i = 0; i < 4; i++){
			drawXs[i] = (int)((getStartX() + (xs[i] * getScale()) + (x * getScale())));
			drawYs[i] = (int)((getStartY() - (ys[i] * getScale()) - (y * getScale())));
		}
		Polygon robotSurface = new Polygon(drawXs, drawYs, 4);
		g.setColor(Color.BLUE);
		g.fillPolygon(robotSurface);
		if (currentRobot.hasBall()){
			g.setColor(Color.YELLOW);
			g.fillOval((int)(getStartX() + x * getScale()), (int)(getStartY() - y * getScale()), getBorderWidth(), getBorderWidth());
		}
		g.setColor(Color.BLACK);
		g.drawLine((int) ((x * getScale()) + getStartX()),
				(int) (getStartY() - (y * getScale())),
				(int) ((getScale() * x) + getStartX() - (getBorderWidth() * Math.cos(r))),
				(int) (getStartY() - (getScale() * y) - (getBorderWidth() * Math.sin(r))));
	}
	
	private int[] convertRelativeToAbsolutePosition(int x, int y, FieldSimulation field, int playerNumber) {
		int[] newpos = new int[] {x,y};
		switch(field.getStartDir(playerNumber)) {
			case BOTTOM:
				newpos = new int[] {-(int)x,-(int)y};
				break;
			case LEFT:
				newpos = new int[] {-(int)y,(int)x};
				break;
			case RIGHT:
				newpos = new int[] {(int)y,-(int)x};
				break;
			case TOP:
				break;
			default:
				break;
			
		}
		newpos[0] += field.getStartPos(playerNumber).getX();
		newpos[1] += field.getStartPos(playerNumber).getY();
		return newpos;
	}

}
