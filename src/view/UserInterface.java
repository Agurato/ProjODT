package view;

import java.util.ArrayList;

import model.Result;

/**
 * 
 * @author Louis Desportes
 *
 */

public interface UserInterface {
	public void displayResults(ArrayList<Result> results);
	public void confirmSync();
	public void confirmChangeRoot(String rootPath);
	public void displayHelp();
	public void ListTitles(ArrayList<Result> titles);
	public void confirmOpening(String filename);
	public void ListFiles(ArrayList<Result> files);
}
