package view;

import java.util.ArrayList;
import java.util.HashMap;

import model.Result;

/**
 * 
 * @author Louis Desportes
 *
 */

public interface UserInterface {
	/**
	 * Display results of search
	 * 
	 * @param results
	 *            the results to display
	 */
	public void displayResults(ArrayList<Result> results);
	/**
	 * Confirm that the database has been sync
	 */
	public void confirmSync();
	/**
	 * Confirm that root folder has been changed
	 * 
	 * @param rootPath
	 *            The new root folder
	 */
	public void confirmChangeRoot(String rootPath);
	/**
	 * Display help
	 */
	public void displayHelp();
	/**
	 * List the titles of a file
	 * @param titles The titles to display
	 */
	public void listTitles(ArrayList<Result> titles);
	/**
	 * confirm that a file is about to open
	 * @param filename The path of the file opening
	 */
	public void confirmOpening(String filename);
	/**
	 * Inform the user that the file to be opened can't be found
	 * @param filename The path to the file to be opened
	 */
	public void fileNotFound(String filename);
	/**
	 * Inform the user that there is no default application to open the file
	 * @param filename The path to the file to be opened
	 */
	public void noDefaultApp(String filename);
	/**
	 * List files
	 * @param files The files to list
	 */
	public void listFiles(ArrayList<Result> files);
	/**
	 * Display TextFile info
	 * @param infos The informations to display
	 */
	public void displayInfos(HashMap<String, String> infos);
}
