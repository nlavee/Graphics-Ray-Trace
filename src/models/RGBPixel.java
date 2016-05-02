package src.models;

/*
   RGBPixel 

   Instantiable class that stores a Red, Green and Blue value each
   in the range 0-255.

   Author: Michael Eckmann
   Skidmore College
   Spring 2016

*/

public class RGBPixel
{
	private int r;
	private int g;
	private int b;

	public RGBPixel(int r, int g, int b)
	{
		if (r >= 0 && r <= 255)
			this.r = r;
		else
			this.r = 0;
		if (g >= 0 && g <= 255)
			this.g = g;
		else
			this.g = 0;
		if (b >= 0 && b <= 255)
			this.b = b;
		else
			this.b = 0;
	}

	public int getRed()
	{
		return r;
	}

	public int getGreen()
	{
		return g;
	}

	public int getBlue()
	{
		return b;
	}

	public void setRed(int r)
	{
		if (r >= 0 && r <= 255)
			this.r = r;
	}
	public void setGreen(int g)
	{
		if (g >= 0 && g <= 255)
			this.g = g;
	}
	public void setBlue(int b)
	{
		if (b >= 0 && b <= 255)
			this.b = b;
	}

}
