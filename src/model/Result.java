package model;

public class Result {
	private int level;
	private String filename;
	private String quote;
	
<<<<<<< HEAD
	public Result(int level, String filename) {
=======
	public Result(int level, String filename, String quote) {
>>>>>>> 07c5efef2f41de8855188e28a4fa2140d8e89520
		super();
		this.level = level;
		this.filename = filename;
		this.quote = quote;
	}
	
	public int getLevel() {
		return level;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public String getQuote() {
		return quote;
	}

	@Override
	public String toString() {
		return "Result [level=" + level + ", filename=" + filename
				+ ", quote=" + quote + "]";
	}
}
