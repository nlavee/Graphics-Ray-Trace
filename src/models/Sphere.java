package src.models;

import src.mode.ReadObjectAndViewingFiles;

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
		// ray intersect sphere algorithm
		Point intersection = null;
		Vector rayDirection = null;
		Vector CenterToOrign = center.subtractVertices(x.getStartingPoint());
		//System.out.println(CenterToOrign);
		boolean intersect = true;
		Double L = null;
		Double E = null;

		// (1) check inside sphere
		boolean insideSphere = isInsideSphere(x);
		if (ReadObjectAndViewingFiles.debug) System.out.println("Inside Sphere: " + insideSphere);
		
		// (2) check intersection
//		if(!insideSphere) // if implemented like this, we would get null for those that are inside sphere.
//		{
			rayDirection = new Vector(x.getDirection().getX1(), 
					x.getDirection().getX2(),
					x.getDirection().getX3());
			rayDirection.normalize(); // get unit 
			//System.out.println("ray Direction: " + rayDirection);
			//System.out.println(CenterToOrign);
			L = CenterToOrign.dotProduct(rayDirection);
			
			// check L to see whether it's < 0
			if(L < 0)
			{
				intersect = false;
			}

			// (3) 
			if(intersect)
			{
				double centerToOriginDist = CenterToOrign.magnitude();
				double eSquared = radius * radius - centerToOriginDist * centerToOriginDist + L * L;
				E = Math.sqrt(eSquared);
				if( eSquared < 0)
				{
					intersect = false;
				}
			}
//		}

		// (4) Find s
		if(intersect)
		{
			System.out.println("Intersect");
			System.out.println("L: " + L);
			System.out.println("E: " + E);
			Double s = null;
			if(!insideSphere)
			{
				s = L - E;
			}
			else
			{
				s = L + E;
			}
			System.out.println(s);
			intersection = new Point(x.getStartingPoint().getX1() + rayDirection.getX1() * s, 
					x.getStartingPoint().getX2() + rayDirection.getX2() * s,
					x.getStartingPoint().getX3() + rayDirection.getX3() * s);
		}
		
		return intersection;
	}

	private boolean isInsideSphere(Ray ray)
	{
		boolean insideSphere = false;
		Vector CenterToOrign = center.subtractVertices(ray.getStartingPoint());
		// check inside sphere by checking whether length is smaller than radius
		if( CenterToOrign.magnitude() < radius )
		{
			insideSphere = true;
		}
		return insideSphere;
	}

	@Override
	public String toString() {
		return "Sphere [props=" + props + ", radius=" + radius + ", center="
				+ center + "]";
	}

	@Override
	public Vector calculateNormalAtIntersection(Point intersection, Ray ray) {

		boolean isInsideSphere = isInsideSphere(ray);
		int coeff = isInsideSphere ? -1 : 1;
		
		Vector normal = new Vector(
				coeff * (intersection.getX1() - center.getX1()) / radius,
				coeff * (intersection.getX2() - center.getX2()) / radius,
				coeff * (intersection.getX3() - center.getX3()) / radius
				);

		return normal;
	}

	@Override
	public SurfaceProperties getSurfaceProperties() {
		return props;
	}


}
