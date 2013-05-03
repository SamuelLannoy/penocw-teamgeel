package robot.brain;

import robot.Robot;

public abstract class EndingCondition {

	public final static EndingCondition NULL_CONDITION = new EndingCondition() {
		
		@Override
		public boolean isLastTile(Robot robot) {
			return false;
		}
		
		@Override
		public boolean checkEveryTile() {
			return false;
		}
	};
	
	public EndingCondition() {
		super();
	}
	
	public abstract boolean isLastTile(Robot robot);
	
	public abstract boolean checkEveryTile();
}
