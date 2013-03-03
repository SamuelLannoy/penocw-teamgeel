package simulator;

/**
 * @author   Samuel
 */
public enum TurningMode {
	/**
	 * @uml.property  name="left"
	 * @uml.associationEnd  
	 */
	Left {
		@Override
		public int sign() {
			return -1;
		}
	}, /**
	 * @uml.property  name="right"
	 * @uml.associationEnd  
	 */
	Right {
		@Override
		public int sign() {
			return 1;
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
