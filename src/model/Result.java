package model;

import java.awt.image.BufferedImage;

public class Result {
	private int level;
	private int frequency;
	private String filename;
	private String quote;
	private BufferedImage thumbnail;
	
	public Result(int level, int frequency, String filename, String quote, BufferedImage thumbnail) {
		this.level = level;
		this.frequency = frequency;
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
	
	public BufferedImage getThumbnail(){
		return thumbnail;
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
		return "File : "+filename+"\n\tQuote : "+quote+"\n\tLevel : "+level+"\n\tFrequency : "+frequency;
	}
}