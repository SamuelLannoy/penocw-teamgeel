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
	
	private boolean rescaleteammate = false;

	@Override
	public void paint(Graphics g) {
		createBufferStrategy(2);
		if (world != null) {
			try{
				if (!robotPool.getMainRobot().getField().isMerged()) {
					//if (robotPool.getMainRobot().getClient().isPlaying()) {
					paintTitle(g);
					fieldDrawer.drawTiles(g, world, this);
					fieldDrawer.drawBorders(g, world, this);
					paintRobots(g);
					fieldDrawer.drawObjects(g, world, this);
				} else {
					if (!rescaleteammate){
						rescale(robotPool.getMainRobot().getTeamMateField());
						rescaleteammate = true;
					}
					fieldDrawer.drawTiles(g, robotPool.getMainRobot().getTeamMateField(), this);
					fieldDrawer.drawBorders(g, robotPool.getMainRobot().getTeamMateField(), this);
					fieldDrawer.drawObjects(g, robotPool.getMainRobot().getTeamMateField(), this);
				}
			} catch (ConcurrentModificationException e) {

			}
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
			g.fillPolygon(robotSurface);		if (currentRobot.hasBall()){
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
