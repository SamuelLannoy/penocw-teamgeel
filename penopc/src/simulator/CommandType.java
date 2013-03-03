package simulator;

public enum CommandType {
	STARTFORWARD{
		@Override
		public void execute(IMovementManager conn, Command comm) {
			conn.setMovingMode(MovingMode.Forward);
		}
	},
	STARTBACKWARD{
		@Override
		public void execute(IMovementManager conn, Command comm) {
			conn.setMovingMode(MovingMode.Backward);
		}
	},
	FORWARD{
		@Override
		public void execute(IMovementManager conn, Command comm) {
			conn.setMovingMode(MovingMode.Forward);
			conn.setTicksMoving(comm.getParam());
		}
	},
	BACKWARD{
		@Override
		public void execute(IMovementManager conn, Command comm) {
			conn.setMovingMode(MovingMode.Backward);
			conn.setTicksMoving(comm.getParam());
		}
	},
	STARTLEFT{
		@Override
		public void execute(IMovementManager conn, Command comm) {
			conn.setTurningMode(TurningMode.Left);
		}
	},
	STARTRIGHT{
		@Override
		public void execute(IMovementManager conn, Command comm) {
			conn.setTurningMode(TurningMode.Right);
		}
	},
	LEFT{
		@Override
		public void execute(IMovementManager conn, Command comm) {
			conn.setTurningMode(TurningMode.Left);
			conn.setTicksTurning(comm.getParam());
		}
	},
	RIGHT{
		@Override
		public void execute(IMovementManager conn, Command comm) {
			conn.setTurningMode(TurningMode.Right);
			conn.setTicksTurning(comm.getParam());
		}
	},
	ORIENT{
		@Override
		public void execute(IMovementManager conn, Command comm) {
			conn.orientOnWhiteLineExec(false);
		}
		
	},
	/*STOP{
		public void execute(VirtualRobotConnector conn, Command comm) {
			conn.setMovingMode(MovingMode.None);
			conn.setTurningMode(TurningMode.None);
			conn.resetCurrCmd();
		}
	}*/;
	
	public abstract void execute(IMovementManager conn, Command comm);
}
