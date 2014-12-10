package model;

public class Result {
	private int Frequence;
	private String filename;
	private String excerpt;
	
	public Result(int frequence, String filename, String excerpt) {
		super();
		Frequence = frequence;
		this.filename = filename;
		this.excerpt = excerpt;
	}
	
	public int getFrequence() {
		return Frequence;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public String getExcerpt() {
		return excerpt;
	}
}
