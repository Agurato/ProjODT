package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;

public interface TextFile extends Serializable {
	/**
	 * Tells if the {@link TextFile} contains the {@link String} search
	 * @param search the {@link String} to be searched
	 * @return An {@link ArrayList} of {@link Result}
	 */
	public ArrayList<Result> contains(String search);

	/**
	 * List the titles in the {@link TextFile}
	 * @return An {@link ArrayList} of {@link Result}
	 */
	public ArrayList<Result> listTitles();

	/**
	 * Return the infos about the {@link TextFile}
	 * @return An {@link HashMap} of the infos
	 */
	public HashMap<String, String> getInfos();

	/**
	 * Return the path to the file
	 * @return a {@link String} of the path
	 */
	public String getFilename();

	/**
	 * Return the thumbnail of the {@link TextFile}
	 * @return The thumbnail as an {@link ImageIcon}
	 */
	public ImageIcon getThumbnail();
}
