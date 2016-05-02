package src.mode;

/*
   Code to read an object file and a viewing parameter file

   There is also code provided to print an object's information
   and the viewing parameter information to the console.

   For use in program 2, CS325 Computer Graphics
   Spring 2016
   Skidmore College
   Instructor: Michael Eckmann

   example usage:
     java ReadObjectAndViewingFiles house.object view1.view

     OR in Eclipse:
      enter the object file name followed by the view file name separated 
      by a space in the 
      Run, Run Configurations, Arguments tab, Program arguments text area

   Revised on 04/04/2016
      - to have polygon information (Point indices, color) stored in a
        Polygon class
      - put all the data variables that need to be used in other classes
        as public static global variables
      - allocated space for the Point array and Polygon array only after
        we read the size from the object file.

 */
/*
 * file formats for the object and view files modelled after
 * that used in csc313 - Computer Graphics, Lehigh University
 * taught by G. Drew Kessler
 */
import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.util.*;

import src.models.Light;
import src.models.Matrix;
import src.models.Point;
import src.models.Polygon;
import src.models.RGBPixel;
import src.models.Ray;
import src.models.Sphere;
import src.models.Surface;
import src.models.SurfaceProperties;
import src.models.Vector;
import src.models.Point;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;

public class ReadObjectAndViewingFiles 
{


	public static Point v[];
	public static Surface surface[];
	public static Light light[];

	/* Viewing parameters */
	public static Point prp;
	public static double umin=0, umax=0, vmin=0, vmax=0;

	static double ambientRed;
	static double ambientGreen;
	static double ambientBlue;

	/* Pixel Array */
	public static RGBPixel[][] image;

	public static int maxDepth = 5;

	public static boolean debug = false;

	public static void main(String args[]) 
	{
		boolean io = false;

		if(!io) {
			// NOTE: this program as written expects the 2 command line arguments
			// the first is the object file name 
			// the second is the viewing paramater file name
			// in Eclipse, enter these separated by a space in the 
			// Run..., Java Application name, Arguments tab, Program arguments text area

			/* this is Java code to read in an object file and
		 a viewing parameter file */

			BufferedReader objFileBR;
			String line, tempstr;
			StringTokenizer st;

			String objfName = args[0]; // get the object file name from first command line parameter

			int numVs = 0, numSurface = 0, numPolys = 0, numLight = 0;

			try {
				/**
				 * Reading the house object
				 */
				objFileBR = new BufferedReader(new FileReader(objfName));

				/**
				 * Getting an idea of how many vertices there are
				 */
				line = objFileBR.readLine(); // should be the VERTICES line
				st = new StringTokenizer(line, " ");
				tempstr = st.nextToken();
				if (tempstr.equals("VERTICES")) {
					tempstr = st.nextToken();
					numVs = Integer.parseInt(tempstr);
					v = new Point[numVs];
				} else {
					numVs = 0;
					System.out.println("Expecting VERTICES line in file "
							+ objfName);
					System.exit(1);
				}

				/**
				 * Getting an idea of how many surfaces there are
				 */
				line = objFileBR.readLine(); // should be the POLYGONS line
				st = new StringTokenizer(line, " ");
				tempstr = st.nextToken();
				if (tempstr.equals("SURFACE")) {
					tempstr = st.nextToken();
					numSurface = Integer.parseInt(tempstr);
					surface = new Surface[numSurface];
				} else {
					System.out.println("Expecting SURFACE line in file "
							+ objfName);
					System.exit(1);
				}

				/**
				 * Getting an idea of how many polygons there are
				 */
				line = objFileBR.readLine(); // should be the POLYGONS line
				st = new StringTokenizer(line, " ");
				tempstr = st.nextToken();
				if (tempstr.equals("POLYGON")) {
					tempstr = st.nextToken();
					numPolys = Integer.parseInt(tempstr);
				} else {
					System.out.println("Expecting POLYGON line in file "
							+ objfName);
					System.exit(1);
				}

				/**
				 * Getting an idea of how many light there are
				 */
				line = objFileBR.readLine(); // should be the POLYGONS line
				st = new StringTokenizer(line, " ");
				tempstr = st.nextToken();
				if (tempstr.equals("LIGHT")) {
					tempstr = st.nextToken();
					numLight = Integer.parseInt(tempstr);
					light = new Light[numLight];
				} else {
					System.out.println("Expecting LIGHT line in file "
							+ objfName);
					System.exit(1);
				}

				/**
				 * Go through vertices list
				 */
				line = objFileBR.readLine(); // should be the Point LIST line
				st = new StringTokenizer(line, " ");
				tempstr = st.nextToken();
				if (tempstr.equals("POINT")) {
					tempstr = st.nextToken();
					if (!tempstr.equals("LIST")) {
						System.out.println("Expecting POINT LIST line in file "
								+ objfName);
						System.exit(1);
					}
				} else {
					System.out.println("Expecting POINT LIST line in file "
							+ objfName);
					System.exit(1);
				}
				// if we get here we successfully processed the Point LIST line

				// reads each of the Point coordinates and creates a Point object for each one 
				for (int i = 0; i < numVs; i++) {
					line = objFileBR.readLine();
					st = new StringTokenizer(line, " ");
					double x1=0, y1=0, z1=0;				
					tempstr = st.nextToken();
					x1 = Double.parseDouble(tempstr);
					tempstr = st.nextToken();
					y1 = Double.parseDouble(tempstr);
					tempstr = st.nextToken();
					z1 = Double.parseDouble(tempstr);
					v[i] = new Point(x1,y1,z1,1.0);
				}

				/**
				 * Go through polygon list
				 */
				line = objFileBR.readLine(); // should be the POLYGON LIST line
				st = new StringTokenizer(line, " ");
				tempstr = st.nextToken();
				if (tempstr.equals("POLYGON")) {
					tempstr = st.nextToken();
					if (!tempstr.equals("LIST")) {
						System.out.println("Expecting POLYGON LIST line in file "
								+ objfName);
						System.exit(1);
					}
				} else {
					System.out.println("Expecting POLYGON LIST line in file "
							+ objfName);
					System.exit(1);
				}
				// if we get here we successfully processed the POLYGON LIST line

				for (int i = 0; i < numPolys; i++) {
					line = objFileBR.readLine();
					st = new StringTokenizer(line, " ");
					st.nextToken(); // ignore the string COUNT 
					tempstr = st.nextToken(); // this is the value of count (number of vertices for this poly)
					int numVsForThisPoly = Integer.parseInt(tempstr);
					Polygon newPolygon = new Polygon();
					st.nextToken(); // ignore the string VERTICES 

					//example line: COUNT 5 VERTICES 5 4 3 2 1 COLOR .4 .2 .4

					for (int j = 1; j <=numVsForThisPoly; j++) {
						tempstr = st.nextToken();
						newPolygon.addPoint(v[Integer.parseInt(tempstr) - 1]);
					}

					st.nextToken(); // ignore the string COLOR

					tempstr = st.nextToken();
					newPolygon.setRed(Double.parseDouble(tempstr));
					tempstr = st.nextToken();
					newPolygon.setGreen(Double.parseDouble(tempstr));
					tempstr = st.nextToken();
					newPolygon.setBlue(Double.parseDouble(tempstr));

					st.nextToken(); // ignore the string OTHER

					tempstr = st.nextToken();
					double ambientReflectionCoeff = Double.parseDouble(tempstr);
					tempstr = st.nextToken();
					double diffuseReflectionCoeff = Double.parseDouble(tempstr);
					tempstr = st.nextToken();
					double specularReflectionCoeff = Double.parseDouble(tempstr);
					tempstr = st.nextToken();
					double specularReflectionExp = Double.parseDouble(tempstr);
					newPolygon.getSurfaceProperties().setAmbientReflectionCoefficient(ambientReflectionCoeff);
					newPolygon.getSurfaceProperties().setDiffuseReflectionCoefficient(diffuseReflectionCoeff);
					newPolygon.getSurfaceProperties().setSpecularReflectionCoefficient(specularReflectionCoeff);
					newPolygon.getSurfaceProperties().setSpecularReflectionExponent(specularReflectionExp);
					if(debug) System.out.println(newPolygon);

					surface[i] = newPolygon;
				}

				/**
				 * Go through sphere list
				 */
				line = objFileBR.readLine(); // should be the POLYGON LIST line
				st = new StringTokenizer(line, " ");
				tempstr = st.nextToken();
				if (tempstr.equals("SPHERE")) {
					tempstr = st.nextToken();
					if (!tempstr.equals("LIST")) {
						System.out.println("Expecting SPHERE LIST line in file "
								+ objfName);
						System.exit(1);
					}
				} else {
					System.out.println("Expecting SPHERE LIST line in file "
							+ objfName);
					System.exit(1);
				}
				// if we get here we successfully processed the SPHERE LIST line

				for (int i = 0; i < numSurface - numPolys; i++) {
					line = objFileBR.readLine();
					st = new StringTokenizer(line, " ");
					st.nextToken(); // ignore the string RADIUS 
					tempstr = st.nextToken(); // this is the radius
					int radius = Integer.parseInt(tempstr);
					st.nextToken(); // ignore the string CENTER 
					tempstr = st.nextToken();
					int centerIndex = Integer.parseInt(tempstr);
					Point center = v[centerIndex];
					Sphere newSphere = new Sphere(radius, center);
					st.nextToken(); // ignore the string COLOR

					tempstr = st.nextToken();
					newSphere.setRed(Double.parseDouble(tempstr));
					tempstr = st.nextToken();
					newSphere.setGreen(Double.parseDouble(tempstr));
					tempstr = st.nextToken();
					newSphere.setBlue(Double.parseDouble(tempstr));

					st.nextToken(); // ignore the string OTHER

					tempstr = st.nextToken();
					double ambientReflectionCoeff = Double.parseDouble(tempstr);
					tempstr = st.nextToken();
					double diffuseReflectionCoeff = Double.parseDouble(tempstr);
					tempstr = st.nextToken();
					double specularReflectionCoeff = Double.parseDouble(tempstr);
					tempstr = st.nextToken();
					double specularReflectionExp = Double.parseDouble(tempstr);
					newSphere.getSurfaceProperties().setAmbientReflectionCoefficient(ambientReflectionCoeff);
					newSphere.getSurfaceProperties().setDiffuseReflectionCoefficient(diffuseReflectionCoeff);
					newSphere.getSurfaceProperties().setSpecularReflectionCoefficient(specularReflectionCoeff);
					newSphere.getSurfaceProperties().setSpecularReflectionExponent(specularReflectionExp);
					surface[numPolys + i] = newSphere;
				}

				/**
				 * Go through sphere list
				 */
				line = objFileBR.readLine(); // should be the POLYGON LIST line
				st = new StringTokenizer(line, " ");
				tempstr = st.nextToken();
				if (tempstr.equals("LIGHT")) {
					tempstr = st.nextToken();
					if (!tempstr.equals("LIST")) {
						System.out.println("Expecting LIGHT LIST line in file "
								+ objfName);
						System.exit(1);
					}
				} else {
					System.out.println("Expecting LIGHT LIST line in file "
							+ objfName);
					System.exit(1);
				}
				// if we get here we successfully processed the SPHERE LIST line

				for (int i = 0; i < numLight; i++) {
					line = objFileBR.readLine();
					st = new StringTokenizer(line, " ");
					st.nextToken(); // ignore the string POSITION 
					tempstr = st.nextToken(); // this is the position
					int positionIndex = Integer.parseInt(tempstr);
					Point position = v[positionIndex];
					st.nextToken(); // ignore the string COLOR

					tempstr = st.nextToken();
					double r = Double.parseDouble(tempstr);
					tempstr = st.nextToken();
					double g = Double.parseDouble(tempstr);
					tempstr = st.nextToken();
					double b = Double.parseDouble(tempstr);

					Light newLight = new Light(position, r,g,b);
					light[i] = newLight;
				}

				objFileBR.close();

			} catch (FileNotFoundException fnfe) {
				System.out.println("File not found");
			} catch (IOException ioe) {
				System.out.println("couldn't read from file");
			}


			//			for(Point Point : v)
			//			{
			//				System.out.println(Point);
			//			}
			//			
			//			for (int i = 0; i < numPolys; i++) {
			//				System.out.println("Polygon number " + i + ":");
			//				for (int j = 0; j < polygon[i].getNumVs(); j++)
			//					System.out.println(" " + v[polygon[i].PointIndices[j]].toString());
			//				System.out.println();
			//				System.out.println(" with color: " + polygon[i].getColorInfo() + "\n");
			//			}

			// ================================================================
			// ------READ VIEWING PARAMETER FILE  
			// ================================================================

			String viewfName = args[1]; // second command line arg
			BufferedReader viewFileBR;

			try {
				viewFileBR = new BufferedReader(new FileReader(viewfName));

				line = viewFileBR.readLine(); // should be the PRP line
				st = new StringTokenizer(line, " ");
				tempstr = st.nextToken();
				if (tempstr.equals("PRP")) {
					double x1=0, y1=0, z1=0;				
					tempstr = st.nextToken();
					x1 = Double.parseDouble(tempstr);
					tempstr = st.nextToken();
					y1 = Double.parseDouble(tempstr);
					tempstr = st.nextToken();
					z1 = Double.parseDouble(tempstr);
					prp = new Point(x1,y1,z1,1.0);
				} else {
					System.out.println("Expecting PRP line in file "
							+ viewfName);
					System.exit(1);
				}


				line = viewFileBR.readLine(); // should be the WINDOW line
				st = new StringTokenizer(line, " ");
				tempstr = st.nextToken();
				if (tempstr.equals("WINDOW")) {
					tempstr = st.nextToken();
					umin = Double.parseDouble(tempstr);
					tempstr = st.nextToken();
					umax = Double.parseDouble(tempstr);
					tempstr = st.nextToken();
					vmin = Double.parseDouble(tempstr);
					tempstr = st.nextToken();
					vmax = Double.parseDouble(tempstr);

				} else {
					System.out.println("Expecting WINDOW line in file "
							+ viewfName);
					System.exit(1);
				}

				line = viewFileBR.readLine(); // should be the AMBIENT line
				st = new StringTokenizer(line, " ");
				tempstr = st.nextToken();
				if (tempstr.equals("AMBIENT")) {
					tempstr = st.nextToken();
					ambientRed = Double.parseDouble(tempstr);
					tempstr = st.nextToken();
					ambientGreen = Double.parseDouble(tempstr);
					tempstr = st.nextToken();
					ambientBlue = Double.parseDouble(tempstr);
				} else {
					System.out.println("Expecting AMBIENT line in file "
							+ viewfName);
					System.exit(1);
				}

				viewFileBR.close();
				System.out.print("...\n");
			} catch (FileNotFoundException fnfe) {
				System.out.println("File not found");
			} catch (IOException ioe) {
				System.out.println("couldn't read from file");
			}

			//			System.out.println("VRP = " + vrp);
			//			System.out.println("PRP = " + prp);
			//			System.out.println("VUP = " + vup);
			//			System.out.println("VPN = " + vpn);
			//			
			//			System.out.print("WINDOW =");
			//			System.out.println(" " + umin + " " + umax + " " + vmin + " " + vmax);
			//
			//			System.out.print("FRONT =");
			//			System.out.println(" " + frontClip);
			//
			//			System.out.print("BACK =");
			//			System.out.println(" " + backClip);
			if (debug) System.out.println(ambientRed);
			if (debug) System.out.println(ambientGreen);
			if (debug) System.out.println(ambientBlue);

			io = true;
		}

		//setTempParam();
		//System.out.print("...");
		//System.out.println();
		/**
		 * Done with IO, move on to drawing.
		 */
		//mainDrawing();
		mainImageGenerating();
	} // end of main		

	private static void mainImageGenerating() {
		if (debug) {
			for(Point point : v)
			{
				System.out.println(point);
			}
			for(Surface surf : surface)
			{
				System.out.println(surf);
			}
			for(Light l : light)
			{
				System.out.println(l);
			}
		}
		
		double height = umax - umin;
		double width = vmax - vmin;
		//System.out.println("Height: " + (height) + " - Width: " + (width));

		image  = new RGBPixel[(int) height][(int) width];

		// for each scan line
		for(int i = 0; i < height; i++)
		{
			// for each pixel in scan line
			for(int j = 0; j < width; j++)
			{
				System.out.println("\t\tStarting on pixel: (" + i + "," +j + ")");
				double xValuePixel = (2/height) * (i + 0.5)-1;
				double yValuePixel = (2/width) * (j + 0.5) - 1;
				Point pixelPoint = new Point(xValuePixel, yValuePixel, -1); // plane sits on z = -1
				if (debug) System.out.println(pixelPoint);

				Vector rayFromPRP = pixelPoint.subtractVertices(prp);
				Ray ray = new Ray(prp, rayFromPRP);
				
				// start tracing ray
				SurfaceProperties surfaceProp = RT_trace(ray, 1);
				if (debug) System.out.println(surfaceProp.getRed() * 255 + " - " + surfaceProp.getGreen() * 255 + " - " + surfaceProp.getBlue() * 255);
				
				// get the color and put it in the array of RGB Pixel
				image[i][j] = new RGBPixel((int) (surfaceProp.getRed() * 255), 
						(int) (surfaceProp.getGreen() * 255), 
						(int) (surfaceProp.getBlue() * 255));
				
				//if (debug) 
			}
		}

		// export image
		try {
			ReadWritePPM.writeImage(image, "../../img/test.ppm");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static SurfaceProperties RT_trace(Ray ray, int i) {
		SurfaceProperties prop = new SurfaceProperties();

		// determine nearest intersection point
		int nearestSurface = -1;
		Point nearestPoint = null;
		for(int j = 0; j < surface.length; j++)
		{
			Surface surf = surface[j];
			if (debug) System.out.println(surf);

			// calculate intersection point
			Point intersectionPoint = surf.intersect(ray);
			if(intersectionPoint != null)
			{
				//if (debug) 
				//	System.out.println(intersectionPoint);
				if(nearestPoint == null)
				{
					nearestPoint = intersectionPoint;
					nearestSurface = j;
				}
				else
				{
					if(intersectionPoint.getX3() < nearestPoint.getX3()) // going in negative z so smaller is better
					{
						nearestPoint = intersectionPoint;
						nearestSurface = j;
					}
				}
			}
		}

		// if we can find a nearest point, we calculate normal at intersection
		// and then compute other ray from this
		if(nearestPoint != null)
		{
			//if (debug) 
			//System.out.println(nearestPoint);
			Vector normal = computerNormalAtIntersection(surface[nearestSurface], nearestPoint, ray);
			//System.out.println(normal);
			prop = RT_Shade(surface[nearestSurface], ray, nearestPoint, normal, i);
		}
		// if no point, meaning that it just shots to infinity, set that pixel to have ambient color
		else
		{
			setColor(prop, ambientRed, ambientGreen, ambientBlue);
		}

		// return surface properties so that we can get color out
		return prop;
	}

	private static SurfaceProperties RT_Shade(Surface surface2, Ray ray,
			Point nearestPoint, Vector normal, int i) {
		SurfaceProperties prop = new SurfaceProperties();
		
		Ray reflected = new Ray();
		Ray refracted = new Ray();
		Ray shadow = new Ray();

		for(Light l : light)
		{
			Vector pointToLight = l.getPosition().subtractVertices(nearestPoint);
			shadow = new Ray(nearestPoint, pointToLight);
			if( normal.dotProduct(pointToLight) > 0)
			{
				// compute how much light is blocked by opaque and transparent surfaces
				


			}
		}

		if( i < maxDepth )
		{
			// if object is reflective
			if(surface2.getSurfaceProperties().getSpecularReflectionCoefficient() > 0.0)
			{
				Vector reflectedVector = calculateReflectedVector(ray, normal, nearestPoint);
				reflected = new Ray(nearestPoint, reflectedVector);
				//System.out.println(reflected);
				SurfaceProperties surfPropRRay = RT_trace(reflected, i+1);

				// scale color by specular coefficient and add to color
				double redSpecular = surfPropRRay.getRed() * surface2.getSurfaceProperties().getSpecularReflectionCoefficient();
				double greenSpecular = surfPropRRay.getGreen() * surface2.getSurfaceProperties().getSpecularReflectionCoefficient();
				double blueSpecular = surfPropRRay.getBlue() * surface2.getSurfaceProperties().getSpecularReflectionCoefficient();
				//				System.out.println(redSpecular + " - " + greenSpecular + " - " + blueSpecular);
				setColor(prop, prop.getRed() + redSpecular, prop.getGreen() + greenSpecular, prop.getBlue() + blueSpecular);

			}

			// if object is transparent, not taking this into account right now
			//			if(true)
			//			{
			//				Ray tRay = new Ray();
			//
			//				// if total internal reflection does not occur
			//				if(true)
			//				{
			//					SurfaceProperties surfProptRay = RT_trace(tRay, i+1);
			//
			//					// scale color by transmission coefficient and add to color
			//					
			//				}
			//			}
		}

		return prop;
	}

	/**
	 * Calculate e reflected ray based on the entering ray and normal vector.
	 * @param ray
	 * @param normal
	 * @param nearestPoint
	 * @return
	 */
	private static Vector calculateReflectedVector(Ray ray, Vector normal,
			Point nearestPoint) {
		Vector lightIn = nearestPoint.subtractVertices(ray.getStartingPoint());
		lightIn.normalize();
		normal.normalize();

		double doubleDotProductInAndNormal = lightIn.dotProduct(normal) * 2;
		Vector newN = new Vector(doubleDotProductInAndNormal * normal.getX1(),
				doubleDotProductInAndNormal * normal.getX2(), 
				doubleDotProductInAndNormal * normal.getX3());
		
		Vector res = new Vector(newN.getX1() - lightIn.getX1(), 
				newN.getX2() - lightIn.getX2(),
				newN.getX3() - lightIn.getX3());
		return res;
	}

	private static void setColor(SurfaceProperties prop, double ambientRed2,
			double ambientGreen2, double ambientBlue2) {
		prop.setRed(ambientRed2);
		prop.setGreen(ambientGreen2);
		prop.setBlue(ambientBlue2);
	}

	private static Vector computerNormalAtIntersection(Surface surface2,
			Point nearestPoint, Ray ray) {
		return surface2.calculateNormalAtIntersection(nearestPoint, ray);
	}


} // end of class

