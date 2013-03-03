package simulator;

/**
 * @author   Samuel
 */
public enum MovingMode {
	/**
	 * @uml.property  name="forward"
	 * @uml.associationEnd  
	 */
	Forward {
		@Override
		public int sign() {
			return 1;
		}
	}, /**
	 * @uml.property  name="backward"
	 * @uml.associationEnd  
	 */
	Backward {
		@Override
		public int sign() {
			return -1;
		}
	}, /**
	 * @uml.property  name="none"
	 * @uml.associationEnd  
	 */
	None {
		@Override
		public int sign() {
			return 0;
		}
	};
	
	public abstract int sign();
}
