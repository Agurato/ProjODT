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
	private static final long serialVersionUID = 1L;
	private int level;
	private int frequency;
	private String filename;
	private String quote;

	/**
	 * @param level The level of the title
	 * @param frequency The frequency of the title
	 * @param filename The filename of the original TextFile
	 * @param quote The title itself
	 */
	public Result(int level, int frequency, String filename, String quote) {
		this.level = level;
		this.frequency = frequency;
		this.filename = filename;
		this.quote = quote;
	}

	/**
	 * Return the level of the title
	 * @return the level of the title/quote
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Return the frequency of the title
	 * @return the frequency of the quote in the text
	 */
	public int getFrequency() {
		return frequency;
	}

	/**
	 * Return the name of the original odt
	 * @return the name of the odt (= path)
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * return the title itself
	 * @return the quote/title
	 */
	public String getQuote() {
		return quote;
	}

	/**
	 * Used to modify the frequency
	 * 
	 * @param freq The new Frequency
	 */
	public void setFrequency(int freq) {
		this.frequency = freq;
	}

	/**
	 * Test if the level, the quote and the file are the same
	 * 
	 * @param obj The object to be tested
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
	 * return the {@link String} with the main attributes of Result
	 */
	public String toString() {
		return "File : " + filename + "\n\tQuote : " + quote + "\n\tLevel : "
				+ level + "\n\tFrequency : " + frequency;
	}

	/**
	 * Compare two {@link Result}
	 */
	@Override
	public int compareTo(Result comparedResult) {
		return comparedResult.getLevel() * comparedResult.getFrequency()
				- level * frequency;
	}
}