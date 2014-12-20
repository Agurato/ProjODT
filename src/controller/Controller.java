package controller;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import model.DataBase;
import model.Result;
import view.CommandLineInterface;
import view.UserInterface;
import view.GraphicalUserInterface;

public class Controller {
	static UserInterface ui;
	static DataBase database;

	public static void main(String[] Args) {
		if(Args.length == 0){
			ui = (UserInterface) new GraphicalUserInterface();
		}else{
			ui = (UserInterface) new CommandLineInterface(Args);
		}
	}

	public Controller(){
		database = new DataBase(
				System.getProperty("user.dir"));
	}
	
	public Controller(String path){
		database = new DataBase(path);
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

	public ArrayList<Result> listFiles() {
		return database.listFiles();
		
	}
}
