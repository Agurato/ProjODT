package model;

public class Result {
	private int level;
	private String filename;
	private String quote;
	
	public Result(int level, String filename, String quote) {
//		super();
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
}