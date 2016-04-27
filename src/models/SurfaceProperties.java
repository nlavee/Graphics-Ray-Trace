package src.models;

public class SurfaceProperties {
	
	double red;
	double green;
	double blue;
	double ambientReflectionCoefficient;
	double diffuseReflectionCoefficient;
	double specularReflectionCoefficient;
	double specularReflectionExponent;
	
	public SurfaceProperties() {
	}

	public SurfaceProperties(double red, double green, double blue,
			double ambientReflectionCoefficient,
			double diffuseReflectionCoefficient,
			double specularReflectionCoefficient,
			double specularReflectionExponent) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.ambientReflectionCoefficient = ambientReflectionCoefficient;
		this.diffuseReflectionCoefficient = diffuseReflectionCoefficient;
		this.specularReflectionCoefficient = specularReflectionCoefficient;
		this.specularReflectionExponent = specularReflectionExponent;
	}

	public double getRed() {
		return red;
	}

	public void setRed(double red) {
		this.red = red;
	}

	public double getGreen() {
		return green;
	}

	public void setGreen(double green) {
		this.green = green;
	}

	public double getBlue() {
		return blue;
	}

	public void setBlue(double blue) {
		this.blue = blue;
	}

	public double getAmbientReflectionCoefficient() {
		return ambientReflectionCoefficient;
	}

	public void setAmbientReflectionCoefficient(double ambientReflectionCoefficient) {
		this.ambientReflectionCoefficient = ambientReflectionCoefficient;
	}

	public double getDiffuseReflectionCoefficient() {
		return diffuseReflectionCoefficient;
	}

	public void setDiffuseReflectionCoefficient(double diffuseReflectionCoefficient) {
		this.diffuseReflectionCoefficient = diffuseReflectionCoefficient;
	}

	public double getSpecularReflectionCoefficient() {
		return specularReflectionCoefficient;
	}

	public void setSpecularReflectionCoefficient(
			double specularReflectionCoefficient) {
		this.specularReflectionCoefficient = specularReflectionCoefficient;
	}

	public double getSpecularReflectionExponent() {
		return specularReflectionExponent;
	}

	public void setSpecularReflectionExponent(double specularReflectionExponent) {
		this.specularReflectionExponent = specularReflectionExponent;
	}

	@Override
	public String toString() {
		return "SurfaceProperties [red=" + red + ", green=" + green + ", blue="
				+ blue + ", ambientReflectionCoefficient="
				+ ambientReflectionCoefficient
				+ ", diffuseReflectionCoefficient="
				+ diffuseReflectionCoefficient
				+ ", specularReflectionCoefficient="
				+ specularReflectionCoefficient
				+ ", specularReflectionExponent=" + specularReflectionExponent
				+ "]";
	}
	
	
	
	
}
