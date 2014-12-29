package model;

import java.io.Serializable;

/**
 * Used for the researches in the files
 * 
 * @author Louis Desportes
 * @author Vincent Monot
 *
 */

public class Result implements Comparable<Result>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int level;
	private int frequency;
	private String filename;
	private String quote;

	/**
	 * 
	 * @param level
	 * @param frequency
	 * @param filename
	 * @param quote
	 * @param thumbnail
	 */

	public Result(int level, int frequency, String filename, String quote) {
		this.level = level;
		this.frequency = frequency;
		this.filename = filename;
		this.quote = quote;
	}

	/**
	 * 
	 * @return the level of the title/quote
	 */

	public int getLevel() {
		return level;
	}

	/**
	 * 
	 * @return the frequency of the quote in the text
	 */

	public int getFrequency() {
		return frequency;
	}

	/**
	 * 
	 * @return the name of the odt (= path)
	 */

	public String getFilename() {
		return filename;
	}

	/**
	 * 
	 * @return the quote/title
	 */

	public String getQuote() {
		return quote;
	}

	/**
	 * Used to modify the frequence
	 * 
	 * @param freq
	 */

	public void setFrequency(int freq) {
		this.frequency = freq;
	}

	/**
	 * Test if the level, the quote and the file are the same
	 * 
	 * @param obj
	 */

	public boolean equals(Object obj) {
		if (!(obj instanceof Result)) {
			return false;
		}
		Result res = (Result) obj;

		if (res.getLevel() == this.level && res.getQuote().equals(this.quote)
				&& res.getFilename().equals(this.filename)) {
			return true;
		}

		return false;
	}

	/**
	 * return the String with the main attributes of Result
	 */

	public String toString() {
		return "File : " + filename + "\n\tQuote : " + quote + "\n\tLevel : "
				+ level + "\n\tFrequency : " + frequency;
	}

	/**
	 * 
	 */

	@Override
	public int compareTo(Result comparedResult) {
		return comparedResult.getLevel() * comparedResult.getFrequency()
				- level * frequency;
	}
}