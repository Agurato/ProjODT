package model;

public class Result {
	private int frequency;
	private String filename;
	
	public Result(int frequency, String filename) {
		super();
		this.frequency = frequency;
		this.filename = filename;
	}
	
	public int getFrequency() {
		return frequency;
	}
	
	public String getFilename() {
		return filename;
	}
}
