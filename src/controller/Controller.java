package controller;

import java.awt.Desktop;
import java.awt.TextField;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import model.DataBase;
import model.ODTFile;
import model.Result;
import model.TextFile;
import view.CommandLineInterface;
import view.UserInterface;
import view.GraphicalUserInterface;

public class Controller {
	static UserInterface ui;
	static DataBase database;

	private enum Action {
		NONE, SEARCH, LIST, DISPLAY, SYNC, OPEN
	}

	public static void main(String[] args) {
		boolean help = false;
		Action action = Action.NONE;
		String param = "", root = "";

		// Decide actions
		for (int i = 0; i < args.length;) {
			switch (args[i]) {
			case "-d":
			case "--database":
				root = args[i + 1];
				i+=2;
				break;
			case "-h":
			case "--help":
				help = true;
				i++;
				break;
			case "display":
				action = Action.DISPLAY;
				param = args[i + 1];
				i+=2;

				break;
			case "search":
				action = Action.SEARCH;
				i++;
				while (i < args.length && !args[i].startsWith("-")) {
					param = param + " " + args[i];
					i++;
				}
				param = param.substring(1);

				break;
			case "open":
				action = Action.OPEN;
				i++;
				while (i < args.length && !args[i].startsWith("-")) {
					param = param + " " + args[i];
					i++;
				}
				param = param.substring(1);

				break;
			case "list":
				action = Action.LIST;
				i++;

				break;
			case "sync":
				action = Action.SYNC;
				i++;

				break;
			default:
				i++;
			}
		}

		// Set root and start database
		if (root.isEmpty()) {
			database = new DataBase();
		} else {
			database = new DataBase(root);
		}

		// Exec Action
		switch (action) {
		case DISPLAY:
			if (!help) {
				TextFile textFile = (TextFile) new ODTFile(param);
				ui = (UserInterface) new CommandLineInterface();
				ui.ListTitles(textFile.examination(""));
			}
			break;
		case LIST:
			if (!help) {
				ui = (UserInterface) new CommandLineInterface();
				ui.ListFiles(database.listFiles());
			}
			break;
		case SEARCH:
			if (!help) {
				ui = (UserInterface) new CommandLineInterface();
				ui.displayResults(database.search(param));
			}
			break;
		case OPEN:
			if (!help) {
				ui = (UserInterface) new CommandLineInterface();
				Result result = database.search(param).get(0);
				ui.confirmOpening(result.getFilename());
				try {
					Controller.openFile(result.getFilename());
				} catch (IOException e) {
					System.out.println("Fichier non trouvé");
					e.printStackTrace();
				}
			}
			break;
		case SYNC:
			if (!help) {
				ui = (UserInterface) new CommandLineInterface();
				database.sync();
				ui.confirmSync();
			}
			break;
		default:
			if (!help) {
				ui = (UserInterface) new GraphicalUserInterface();
			} else {
				ui = (UserInterface) new CommandLineInterface();
				ui.displayHelp();
			}
			break;
		}
	}

	public Controller() {
		database = new DataBase(System.getProperty("user.dir"));
	}

	public Controller(String path) {
		// If the given path is a repertory, we call the DataBase constructor
		// with a path of a directory
		if (new File(path).isDirectory()) {
			database = new DataBase(path);
		}
		// Else, we call the DataBase constructor without parameters and we add
		// the file
		else {
			database = new DataBase();
			database.addOdt(path);
		}
	}

	public ArrayList<Result> search(String search)
			throws NoSuchElementException {
		return database.search(search);
	}

	public void changeRoot(String pathName) {
		database = new DataBase(pathName);
	}

	public void addOdt(String pathName) {
		database.addOdt(pathName);
	}

	public ArrayList<Result> listFiles() {
		return database.listFiles();
	}

	public void sync() throws FileNotFoundException {
		database.sync();
	}

	public static void openFile(String path) throws IOException {
		Desktop.getDesktop().open(new File(path));
	}

	public static void openFile(File file) throws IOException {
		Desktop.getDesktop().open(file);
	}
}
