package robot.brain;

import robot.Robot;

public abstract class EndingCondition {

	public EndingCondition() {
		super();
	}
	
	public abstract boolean isLastTile(Robot robot);
}
