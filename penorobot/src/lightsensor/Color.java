package lightsensor;

public enum Color {
	BROWN {
		@Override
		public String getCodeForBarcode() {
			return null;
		}
	},
	WHITE {
		@Override
		public String getCodeForBarcode() {
			return "1";
		}
	},
	BLACK {
		@Override
		public String getCodeForBarcode() {
			return "0";
		}
	};
	
	public static Color getColor(int val) {
		if (val <= 35) {
			return BLACK;
		} else if (val > 35 && val <= 80) {
			return BROWN;
		} else {
			return WHITE;
		}
	}
	
	public abstract String getCodeForBarcode();
}
