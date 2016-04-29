package src.models;

public class Sphere extends Surface{
	private SurfaceProperties props;
	private double radius;
	private Point center;
	
	public Sphere()
	{
		this.props = new SurfaceProperties();
	}
	
	public Sphere(SurfaceProperties props) {
		this.props = props;
	}

	public Sphere(SurfaceProperties props, double radius, Point center) {
		this.props = props;
		this.radius = radius;
		this.center = center;
	}

	public Sphere(double radius, Point center)
	{
		this.radius = radius;
		this.center = center;
		this.props = new SurfaceProperties();	
	}
	
	public void setRed(double r)
	{
		if (r >= 0)
		{
			if (r <= 1)
			{
				props.setRed(r);
			}
			else
			{
				props.setRed(1.0);
			}

		}
		else
		{
			props.setRed(0.0);
		}
	}

	public void setGreen(double g)
	{
		if (g >= 0)
		{
			if (g <= 1)
			{
				props.setGreen(g);
			}
			else
			{
				props.setGreen(1.0);
			}

		}
		else
		{
			props.setGreen(0.0);
		}
	}

	public void setBlue(double b)
	{
		if (b >= 0)
		{
			if (b <= 1)
			{
				props.setBlue(b);
			}
			else
			{
				props.setBlue(1.0);
			}

		}
		else
		{
			props.setBlue(0.0);
		}
	}
	
	public double getRed()
	{
		return props.getRed();
	}

	public double getGreen()
	{
		return props.getGreen();
	}

	public double getBlue()
	{
		return props.getBlue();
	}

	
	@Override
	public Point intersect(Ray x) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return "Sphere [props=" + props + ", radius=" + radius + ", center="
				+ center + "]";
	}

	
}
