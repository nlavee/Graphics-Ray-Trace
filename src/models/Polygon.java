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
		return "Polygon [numberOfVs=" + pointList.size() + ", vertex="
				+ pointList
				+ ", red=" + props.getRed() + ", green=" + props.getGreen() + ", blue=" + props.getBlue() + "]";
	}

	@Override
	public Point intersect(Ray x) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
