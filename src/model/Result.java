package model;

public class Result {
	private int level;
	private String filename;
	
	public Result(int level, String filename) {
		super();
		this.level = level;
		this.filename = filename;
	}
	
	public int getLevel() {
		return level;
	}
	
	public String getFilename() {
		return filename;
	}
}