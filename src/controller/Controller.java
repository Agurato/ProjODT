package controller;

import java.util.ArrayList;

import model.DataBase;
import model.Result;
import view.UserInterface;
import view.GraphicalUserInterface;

public class Controller {
	static UserInterface ui;
	static DataBase database;
	
	public static void main(String[] Args){
		ui = (UserInterface) new GraphicalUserInterface();
		database = new DataBase();
		
	}

	public ArrayList<Result> search(String search){
		return database.search(search);
		
	}
	
	public boolean ChangeRoot(String pathName) {
		return database.ChangeRoot(pathName);
	}
}
