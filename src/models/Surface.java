package src.models;

public abstract class Surface {
	private SurfaceProperties props;
	
	public abstract Point intersect(Ray x);
}
