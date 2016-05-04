package src.models;

public abstract class Surface {
	private SurfaceProperties props;
	
	public abstract Point intersect(Ray x);
	
	public abstract Vector calculateNormalAtIntersection(Point intersection, Ray ray);
	
	public abstract SurfaceProperties getSurfaceProperties();
	
}
