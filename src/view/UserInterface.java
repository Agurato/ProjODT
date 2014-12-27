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
	public void displayResults(ArrayList<Result> results);
	public void confirmSync();
	public void confirmChangeRoot(String rootPath);
	public void displayHelp();
	public void listTitles(ArrayList<Result> titles);
	public void confirmOpening(String filename);
	public void listFiles(ArrayList<Result> files);
	public void displayInfos(HashMap<String, String> infos);
}
