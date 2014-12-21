package model;

public class Result {
	private int level;
	private int frequency; // This represent how many different terms (among which we searched with) there are in this quote
	private String filename;
	private String quote;
	
	public Result(int level, int freq, String filename, String quote) {
//		super();
		this.level = level;
		this.frequency = freq;
		this.filename = filename;
		this.quote = quote;
	}
	
	public int getLevel() {
		return level;
	}
	
	public int getFrequency() {
		return frequency;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public String getQuote() {
		return quote;
	}
	
	public void setFrequency(int freq) {
		this.frequency = freq;
	}

	public boolean equals(Object obj) {
		if(!(obj instanceof Result)) {
			return false;
		}
		Result res = (Result) obj;
		
		if(res.getLevel() == this.level && res.getQuote().equals(this.quote) && res.getFilename().equals(this.filename)) {
			return true;
		}
		
		return false;
	}

	public String toString() {
		return "File : "+filename+" ; Quote : "+quote+" ; Level : "+level+" ; Frequency : "+frequency;
	}
}