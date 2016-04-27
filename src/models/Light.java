package src.models;

public class Light {
	Point position;
	double red;
	double green;
	double blue;
	
	public Light() {
	}

	public Point getPosition() {
		return position;
	}
	
	

	public Light(Point position, double red, double green, double blue) {
		this.position = position;
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public double getRed() {
		return red;
	}

	public void setRed(double red) {
		this.red = red;
	}

	public double getGreen() {
		return green;
	}

	public void setGreen(double green) {
		this.green = green;
	}

	public double getBlue() {
		return blue;
	}

	public void setBlue(double blue) {
		this.blue = blue;
	}

	@Override
	public String toString() {
		return "Light [position=" + position + ", red=" + red + ", green="
				+ green + ", blue=" + blue + "]";
	}

	
		
	
}
