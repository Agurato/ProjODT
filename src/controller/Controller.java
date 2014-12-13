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
		database = new DataBase("/home/vincent/Documents/odt/exemple_traitement_de_texte_libre_office.odt");
	}

	public ArrayList<Result> search(String search){
		return database.search(search);
		
	}
	
	public void changeRoot(String pathName) {
		database.changeRoot(pathName);
	}
	
	public void sync() {
		database.sync();
	}
}
