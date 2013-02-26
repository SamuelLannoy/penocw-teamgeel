package field;

public class Ball {

	public Ball(int id) {
		super();
		Id = id;
	}
	
	boolean pickedUp;
	
	int Id;

	public boolean isPickedUp() {
		return pickedUp;
	}

	public void setPickedUp(boolean pickedUp) {
		this.pickedUp = pickedUp;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

}
