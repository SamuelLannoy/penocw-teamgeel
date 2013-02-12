package simulator;

public enum CommandType {
	STARTFORWARD{
		public void execute(VirtualRobotConnector conn, Command comm) {
			conn.setMovingMode(MovingMode.Forward);
		}
	},
	STARTBACKWARD{
		public void execute(VirtualRobotConnector conn, Command comm) {
			conn.setMovingMode(MovingMode.Backward);
		}
	},
	FORWARD{
		public void execute(VirtualRobotConnector conn, Command comm) {
			conn.setMovingMode(MovingMode.Forward);
			conn.setTicksMoving(comm.getParam());
		}
	},
	BACKWARD{
		public void execute(VirtualRobotConnector conn, Command comm) {
			conn.setMovingMode(MovingMode.Backward);
			conn.setTicksMoving(comm.getParam());
		}
	},
	STARTLEFT{
		public void execute(VirtualRobotConnector conn, Command comm) {
			conn.setTurningMode(TurningMode.Left);
		}
	},
	STARTRIGHT{
		public void execute(VirtualRobotConnector conn, Command comm) {
			conn.setTurningMode(TurningMode.Right);
		}
	},
	LEFT{
		public void execute(VirtualRobotConnector conn, Command comm) {
			conn.setTurningMode(TurningMode.Left);
			conn.setTicksTurning(comm.getParam());
		}
	},
	RIGHT{
		public void execute(VirtualRobotConnector conn, Command comm) {
			conn.setTurningMode(TurningMode.Right);
			conn.setTicksTurning(comm.getParam());
		}
	},
	ORIENT{
		public void execute(VirtualRobotConnector conn, Command comm) {
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
	
	public abstract void execute(VirtualRobotConnector conn, Command comm);
}
