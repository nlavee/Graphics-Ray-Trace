package src.models;

public class Ray {
	private Point startingPoint;
	private Vector direction;
	
	public Ray(Point startingPoint, Vector direction) {
		this.startingPoint = startingPoint;
		this.direction = direction;
	}

	public Ray() {
		
	}

	public Point getStartingPoint() {
		return startingPoint;
	}

	public void setStartingPoint(Point startingPoint) {
		this.startingPoint = startingPoint;
	}

	public Vector getDirection() {
		return direction;
	}

	public void setDirection(Vector direction) {
		this.direction = direction;
	}

	@Override
	public String toString() {
		return "Ray [startingPoint=" + startingPoint + ", direction="
				+ direction + "]";
	}
	
	
}
