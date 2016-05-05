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

		Vector normal = calculateNormal();
		
		Point planeIntersection = calculatePlane(x, normal);

		// check conditions
		if(normal.dotProduct(x.getDirection()) > 0)
		{
			//				System.out.println("Negate normal");
			//				System.out.println(normal);
			normal.setX1(normal.getX1() * -1);
			normal.setX2(normal.getX2() * -1);
			normal.setX3(normal.getX3() * -1);
			//				System.out.println(normal);
		}

		//System.out.println(planeIntersection);

		if(planeIntersection != null)
		{

			// calculate which plane to project to
			int planeToProject = calculateProjection(normal);
			//			System.out.println(normal);
			//			System.out.println(planeToProject);

			
			// project to the plane it needs 
			ArrayList<Point> projectionPoint = new ArrayList<Point>();
			for(Point p : pointList)
			{
				Point pProjected = new Point(p.getX1(), p.getX2(), p.getX3(), p.getX4());
				if(planeToProject == 1) pProjected.setX1(0);
				else if(planeToProject == 2) pProjected.setX2(0);
				else pProjected.setX3(0);
				projectionPoint.add(pProjected);
			}
			//			System.out.println(projectionPoint);

			// project the intersection point
			Point projectedIntersection = new Point(
					planeIntersection.getX1(), 
					planeIntersection.getX2(), 
					planeIntersection.getX3(), 
					planeIntersection.getX4()
					);
			if(planeToProject == 1) projectedIntersection.setX1(0);
			else if(planeToProject == 2) projectedIntersection.setX2(0);
			else projectedIntersection.setX3(0);
			
			// calculating matrix to translate to origin based on the projected intersection point
			double[][] data = {
					{1,0,0, -1 * projectedIntersection.getX1()},
					{0,1,0, -1 * projectedIntersection.getX2()},
					{0,0,1, -1 * projectedIntersection.getX3()},
					{0,0,0,1}};
			Matrix translateMatrix = new Matrix(data);

			// translate all the points
			for(int i = 0 ; i < projectionPoint.size(); i ++ )
			{
				Point p = projectionPoint.get(i);
				// translate every p
				projectionPoint.set(i, translateMatrix.multiply(p));
			}
			
			boolean intersect = originInsidePolygon(projectionPoint, planeToProject);
			if(intersect)
			{
				//				System.out.println("Intersect: " + intersect);
				return planeIntersection;
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
	}

	private boolean originInsidePolygon(ArrayList<Point> projectionPoint, int planeToProject) {
		int numCross = 0;
		int signHold1 = 0;
		int signHold2 = 0;

		double v0 = 0;
		if(planeToProject == 1) v0 = projectionPoint.get(0).getX3();
		else if(planeToProject == 2) v0 = projectionPoint.get(0).getX3();
		else v0 = projectionPoint.get(0).getX2();
		
		if(v0 > 0) signHold1 += 1;
		else signHold1 -= 1;

		for(int i = 0 ; i < projectionPoint.size() ; i++)
		{
			for(int j = i+1; j < projectionPoint.size(); j++)
			{
				Point start = projectionPoint.get(i);
				Point end = projectionPoint.get(j);


				double ub = 0;
				double ua = 0;
				double vb = 0;
				double va = 0;

				if(planeToProject == 1)
				{
					ub = end.getX2();
					ua = start.getX2();
					vb = end.getX3();
					va = start.getX3();
				}
				else if(planeToProject == 2)
				{
					ub = end.getX1();
					ua = start.getX1();
					vb = end.getX3();
					va = start.getX3();
				}
				else if(planeToProject == 3)
				{
					ub = end.getX1();
					ua = start.getX1();
					vb = end.getX2();
					va = start.getX2();
				}


				if(vb > 0) signHold2 = 1;
				else signHold2 = -1;

				if(signHold1 != signHold2)
				{
					if(ua > 0 && ub > 0) numCross++;
					else if(ua > 0 || ub > 0)
					{
						if(crossPosUAxis(ua, va, ub, vb)) numCross++;
					}
				}
				signHold1 = signHold2;
			}
		}

		boolean intersect = false;
		if(numCross % 2 == 1) intersect = true;
		return intersect;
	}

	private int calculateProjection(Vector normal) {
		double xCoeff = Math.abs(normal.getX1());
		double yCoeff = Math.abs(normal.getX2());
		double zCoeff = Math.abs(normal.getX3());

		double largest = Math.max(xCoeff, Math.max(yCoeff, zCoeff));

		if(largest == xCoeff) return 1;
		else if(largest == yCoeff) return 2;
		else return 3;
	}

	private Point calculatePlane(Ray x, Vector normal) 
	{
		Point point1 = pointList.get(0);

		double D = 0 - ( 
				normal.getX1() * point1.getX1() + 
				normal.getX2() * point1.getX2() + 
				normal.getX3() * point1.getX3()); 
		//System.out.println(D);

		x.getDirection().normalize();
		
		double s = - ( 
				normal.getX1() * x.getStartingPoint().getX1() + 
				normal.getX2() * x.getStartingPoint().getX2() + 
				normal.getX3() * x.getStartingPoint().getX3() + D) / 
				(normal.dotProduct(x.getDirection()));
		//System.out.println(s);
		
		if(s < 0) return null;
		else return new Point(x.getStartingPoint().getX1() + x.getDirection().getX1() * s,
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

	private boolean crossPosUAxis(double ua, double va, double ub, double vb) {
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
