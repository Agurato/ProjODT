package controller;

import java.awt.Desktop;
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

/**
 * Controller for ProjODT
 * @author Vincent Monot
 * @author Louis Desportes
 *
 */
public class Controller {
	static UserInterface ui;
	static DataBase database;

	private enum Action {
		NONE, SEARCH, LIST, DISPLAY, SYNC, OPEN
	}

	/**
	 * Start of the program
	 * @param args The arguments passed by the command line
	 */
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
				ui.displayInfos(textFile.getInfos());
				ui.listTitles(textFile.contains(""));
			}
			break;
		case LIST:
			if (!help) {
				ui = (UserInterface) new CommandLineInterface();
				ui.listFiles(database.listFiles());
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
			database = new DataBase(path);
	}

	/**
	 * Ask database to search an expression
	 * @param search The expression to search
	 * @return A list of results
	 * @throws NoSuchElementException No results in the database
	 */
	public ArrayList<Result> search(String search)
			throws NoSuchElementException {
		return database.search(search);
	}

	/**
	 * Change the root of the database
	 * @param pathName The path to the new root
	 */
	public void changeRoot(String pathName) {
		database = new DataBase(pathName);
	}

	/**
	 * List files in the database
	 * @return An ArrayList<Result> with an Arbitrary Quote
	 */
	public ArrayList<TextFile> listFiles() {
		return database.listFiles();
	}

	/**
	 * Sync the dataBase with the HardDrive
	 */
	public void sync() throws FileNotFoundException {
		database.sync();
	}
	
	/**
	 * Get the Root Path
	 * @return An String of the absolute root path
	 */
	public String getRootPath(){
		return database.getRootPath();
	}

	/**
	 * Open a File with the default application
	 * @param path The path of the file to open
	 * @throws IOException The is no default application
	 */
	public static void openFile(String path) throws IOException {
		openFile(new File(path));
	}

	/**
	 * Open a File with the default application
	 * @param path The file to open
	 * @throws IOException The is no default application
	 */
	public static void openFile(File file) throws IOException, IllegalArgumentException {
		Desktop.getDesktop().open(file);
	}
}
