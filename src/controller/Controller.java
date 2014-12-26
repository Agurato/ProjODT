package controller;

import java.io.File;
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

	public static void main(String[] args) {
		if(args.length == 0){
			ui = (UserInterface) new GraphicalUserInterface();
		}else{
			ui = (UserInterface) new CommandLineInterface(args);
		}
	}

	public Controller(){
		database = new DataBase(
				System.getProperty("user.dir"));
	}
	
	public Controller(String path){
		// If the given path is a repertory, we call the DataBase constructor with a path of a directory
		if(new File(path).isDirectory()) {
			database = new DataBase(path);
		}
		// Else, we call the DataBase constructor without parameters and we add the file
		else {
			database = new DataBase();
			database.addOdt(path);
		}
	}
	
	public ArrayList<Result> search(String search) throws NoSuchElementException{
		return database.search(search);
	}

	public void changeRoot(String pathName) {
		database = new DataBase(pathName);
	}

	public void addOdt(String pathName) {
		database.addOdt(pathName);
	}
	
	public ArrayList<Result> listFiles(){
		return database.listFiles();
	}

	public void sync() throws FileNotFoundException {
		database.sync();
	}

}
