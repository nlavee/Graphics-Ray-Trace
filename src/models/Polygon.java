package src.models;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class for a polygon
 * @author AnhVuNguyen
 *
 */
public class Polygon extends Surface
{

	public ArrayList<Point> pointList; // an array of point
	private SurfaceProperties props;

	public Polygon(ArrayList<Point> pointList,
			SurfaceProperties props) {
		this.pointList = pointList;
		this.props = props;
	}

	public Polygon()
	{
		pointList = new ArrayList<Point>();
		props =  new SurfaceProperties();
	}

	public void addPoint(Point point)
	{
		pointList.add(point);
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

	public int getNumVs()
	{
		return pointList.size();
	}

	public String getColorInfo()
	{
		return "red: " + props.getRed() + ", green: " + props.getGreen() + ", blue: " + props.getBlue();
	}

	@Override
	public String toString() {
		return "Polygon [pointList=" + pointList + ", props=" + props + "]";
	}

	@Override
	public Point intersect(Ray x) {

		Point intersectionPoint = null;

		int numCross = 0;
		int signHold1 = 0;
		int signHold2 = 0;

		if(pointList.get(0).getX2() > 0) signHold1 += 1;
		else signHold1 -= 1;

		for(int i = 0 ; i < pointList.size() -1 ; i++)
		{
			for(int j = i+1; j < pointList.size(); j++)
			{
				Point start = pointList.get(i);
				Point end = pointList.get(j);

				if(end.getX2() > 0) signHold2 += 1;
				else signHold2 -= 1;

				if(signHold1 != signHold2)
				{
					if(start.getX1() > 0 && end.getX1() > 0) numCross++;
					else if(start.getX1() > 0 || end.getX1() > 0)
					{
						if(crossPosUAxis(start,end)) numCross++;
					}
				}
				signHold1 = signHold2;
			}
		}

		boolean intersect = false;
		if(numCross % 2 == 1) intersect = true;

		if(intersect)
		{
			intersectionPoint = calculatePlane(x);
		}
		return intersectionPoint;
	}

	private Point calculatePlane(Ray x) 
	{
		Vector normal = calculateNormal();
		Point point1 = pointList.get(0);
		
		double D = 0 - ( normal.getX1() * point1.getX1() + normal.getX2() * point1.getX2() + normal.getX3() * point1.getX3()); 

		double s = - ( 
				normal.dotProduct(new Vector(x.getStartingPoint().getX1(), 
						x.getStartingPoint().getX2(), 
						x.getStartingPoint().getX3())) + D) / 
						(normal.dotProduct(x.getDirection()));

		return new Point(x.getStartingPoint().getX1() + x.getDirection().getX1() * s,
				x.getStartingPoint().getX2() + x.getDirection().getX2() * s,
				x.getStartingPoint().getX3() + x.getDirection().getX3() * s);
	}

	private Vector calculateNormal() 
	{
		Point point1 = pointList.get(0);
		Point point2 = pointList.get(1);
		Point point3 = pointList.get(2);

		Vector first = point2.subtractVertices(point1);
		Vector second = point3.subtractVertices(point1);

		Vector normal = first.crossProduct(second);
		normal.normalize();
		return normal;
	}

	private boolean crossPosUAxis(Point start, Point end) {
		double ub = end.getX1();
		double ua = start.getX1();
		double vb = end.getX2();
		double va = end.getX1();

		if( ( ub + ( ua - ub ) * ( vb / (vb - va))) > 0 )
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public Vector calculateNormalAtIntersection(Point intersection, Ray ray) {
		Vector normal = calculateNormal();
		
		if(normal.dotProduct(ray.getDirection()) > 0)
		{
			normal = new Vector(-1 * normal.getX1(), 
					-1 * normal.getX2(),
					-1 * normal.getX3());
		}
		
		return normal;
	}

	@Override
	public SurfaceProperties getSurfaceProperties() {
		return props;
	}


}
