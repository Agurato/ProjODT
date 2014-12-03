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
	public void confirmAdd(String fileName);
	public void confirmDelelte(String fileName);
	public void confirmSync(String fileName);
}
