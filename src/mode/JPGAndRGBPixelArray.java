package src.mode;

/*
   JPGAndRGBPixelArray contains methods that 

    readImage - read a jpg image file into a 2d array of RGBPixel
    writeImage - write a jpg image from a 2d array of RGBPixel
    
   Author: Michael Eckmann
   Skidmore College
   Spring 2016

*/
import java.io.*;

import javax.imageio.*;

import java.awt.*;
import java.awt.image.*;
import java.util.*;

import javax.imageio.stream.*;

import src.models.RGBPixel;

public class JPGAndRGBPixelArray
{

	public static RGBPixel[][] readImage(String jpgFileName)
	{
 
		BufferedImage img = null;
		try {
    			img = ImageIO.read(new File(jpgFileName));
		} catch (IOException e) {
			System.out.println("Could not read " + jpgFileName);
		}
		int w = img.getWidth();
		int h = img.getHeight();

		int allPixels[] = img.getRGB(0, 0, w, h, null, 0, w); 
		
		int rows = h, cols = w;

		RGBPixel[][] outImage = new RGBPixel[rows][cols];
				
		for (int i = 0; i < allPixels.length; i++)
		{
			Color c = new Color(allPixels[i]);
/*			System.out.println("i = " + i + "R: = " + c.getRed());
			System.out.println("i = " + i + "G: = " + c.getGreen());
			System.out.println("i = " + i + "B: = " + c.getBlue());
*/
			int row = i / w;
			int col = i % w;
			outImage[row][col] = new RGBPixel(c.getRed(), c.getGreen(), c.getBlue());
		}

		return outImage;
	}

	public static void writeImage(RGBPixel inImage[][], String fName) throws IOException
	{
		int i = 0;
		int pixelArray[] = new int[inImage.length*inImage[0].length];
		BufferedImage img = new BufferedImage(inImage[0].length, inImage.length, BufferedImage.TYPE_INT_RGB);

		for (int r = 0; r < inImage.length; r++)
		{
			for (int c = 0; c < inImage[r].length; c++)
			{
				Color col = new Color(inImage[r][c].getRed(), inImage[r][c].getGreen(), inImage[r][c].getBlue());
				img.setRGB(c,r,col.getRGB());
			}
		}

		Iterator iter = ImageIO.getImageWritersByFormatName("jpeg");
		ImageWriter writer = (ImageWriter)iter.next();
		ImageWriteParam iwp = writer.getDefaultWriteParam();
		iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		iwp.setCompressionQuality(1);
		File outputFile = new File(fName);
		FileImageOutputStream output = new FileImageOutputStream(outputFile);
		writer.setOutput(output);
		IIOImage image = new IIOImage(img, null, null);
		writer.write(null, image, iwp);
		writer.dispose();
		
	}

	public static void main(String args[]) throws IOException
	{
		RGBPixel[][] inImg = readImage("house.jpg");
		writeImage(inImg, "house1out.jpg");
	}

}
