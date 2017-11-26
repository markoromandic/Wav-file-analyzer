package model;

public class ResultDFT {
	private double[] real, img;

	public ResultDFT(double[] real, double[] img) {
		super();
		this.real = real;
		this.img = img;
	}

	public double[] getReal() {
		return real;
	}

	public double[] getImg() {
		return img;
	}
	
	
}
