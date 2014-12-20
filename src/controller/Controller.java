package controller;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import model.DataBase;
import model.Result;
import view.UserInterface;
import view.GraphicalUserInterface;

public class Controller {
	static UserInterface ui;
	static DataBase database;

	public static void main(String[] Args) {
		ui = (UserInterface) new GraphicalUserInterface();
		database = new DataBase(
				"/home/vincent/Documents/odt/exemple_traitement_de_texte_libre_office.odt");
	}

	public ArrayList<Result> search(String search) throws NoSuchElementException{
		return database.search(search);
	}

	public void changeRoot(String pathName) {
		database = new DataBase(pathName);
	}

	public void sync() throws FileNotFoundException {
		database.sync();
	}
}
