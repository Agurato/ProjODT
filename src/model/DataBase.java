package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Database of TextFiles
 * @author Vincent Monot
 *
 */
public class DataBase {
	ArrayList<TextFile> files;
	File rootFolder;

	public DataBase() {
		files = new ArrayList<TextFile>();
	}

	public DataBase(String rootFolderPath) {
		rootFolder = new File(rootFolderPath);
		files = new ArrayList<TextFile>();
		files = getTextFiles(rootFolder.getAbsolutePath());
	}

	/**
	 * return the root Folder
	 * @return The root Folder
	 */
	public File getRoot() {
		return rootFolder;
	}
	
	/**
	 * Return the path to the root Folder
	 * @return a String of the path to the root Folder
	 */
	public String getRootPath() {
		return getRoot().getAbsolutePath();
	}

	/**
	 * Return an Array of files in the database
	 * @return and ArrayList<TextFile> of files in the database
	 */
	public ArrayList<TextFile> getTextFile() {
		return files;
	}

	/**
	 * change root to a new folder
	 * @param rootFolderPath The path to the new Folder
	 */
	public void setRoot(String rootFolderPath) {
		rootFolder = new File(rootFolderPath);
	}

	/**
	 * add An file to the database
	 * @param odt The TextFile to be added
	 * @deprecated
	 */
	public void addOdt(TextFile odt) {
		files.add(odt);
	}

	/**
	 * add An file to the database
	 * @param path the path to the TextFile to be added
	 * @deprecated
	 */
	public void addOdt(String path) {
		addOdt(new ODTFile(path));
	}

	/**
	 * Sync database with files from the hard drive
	 */
	public void sync() {
		// We delete the extract folder and the arrayList
		files.clear();

		// We call the function to extract every odt
		files = getTextFiles(rootFolder.getAbsolutePath());
	}

	/**
	 * Search if the database contains an expression 
	 * @param search The expression to be searched
	 * @return An ArrayList<Result> of results
	 */
	public ArrayList<Result> contains(String search) {
		ArrayList<Result> results = new ArrayList<Result>(); // Return
		ArrayList<Result> exam = null; // Stocks what we searched in a file

		for (TextFile odt : files) {
			exam = odt.contains(search);
			results.addAll(exam);
		}
		return results;
	}

	/**
	 * Return the union of Two ArrayList
	 * @param list1 The first list
	 * @param list2 The second list
	 * @return The union of the two lists
	 */
	private ArrayList<Result> union(ArrayList<Result> list1,
			ArrayList<Result> list2) {
		Set<Result> set = new HashSet<Result>();

		set.addAll(list1);
		set.addAll(list2);

		return new ArrayList<Result>(set);
	}

	/**
	 * Return the intersection of Two ArrayList
	 * @param list1 The first list
	 * @param list2 The second list
	 * @return The intersection of the two lists
	 */
	private ArrayList<Result> intersection(ArrayList<Result> list1,
			ArrayList<Result> list2) {
		ArrayList<Result> list = new ArrayList<Result>();

		for (Result t : list1) {
			if (list2.contains(t)) {
				list.add(t);
			}
		}

		return list;
	}

	/**
	 * Search the database if an expression has results
	 * @param search the expression to be searched
	 * @return An ArrayList<Result> of results
	 */
	public ArrayList<Result> search(String search) {
		ArrayList<Result> results = new ArrayList<Result>();

		if (!search.isEmpty()) {
			// Separate OR Statements
			String[] orSplits = search.split("OU");
			ArrayList<Result> orResults = new ArrayList<Result>();
			for (String orSplit : orSplits) {
				orSplit = orSplit.trim();
				ArrayList<Result> andResults = new ArrayList<Result>();
				// Separate AND Statements
				Iterator<String> andIt = Arrays.asList(orSplit.split("ET"))
						.iterator();

				// Iterate
				String andSplit;
				// Special treatment for the first one
				// because intersection(empty,something)=empty
				if (andIt.hasNext()) {
					andSplit = andIt.next().trim();
					andResults = contains(andSplit);
				}
				while (andIt.hasNext()) {
					andSplit = andIt.next().trim();
					// Intersection of results
					andResults = intersection(andResults, contains(andSplit));
				}

				// Union of results
				orResults = union(orResults, andResults);
			}

			// Check if multiple results for one file
			for (int i = 0; i < orResults.size(); i++) {
				Result tempResult = orResults.get(i);
				for (int j = i + 1; j < orResults.size();) {

					// If there already is a result with the same filename
					if (tempResult.getFilename().equals(
							orResults.get(j).getFilename())) {
						int freq = tempResult.getFrequency()
								+ orResults.get(j).getFrequency();

						// If second result has a upper title level
						if (tempResult.getLevel() >= orResults.get(j)
								.getLevel()) {
							tempResult = orResults.get(j);
						}

						tempResult.setFrequency(freq);
						orResults.remove(j);
					} else {// We go forward
						j++;
					}
				}
				results.add(tempResult);
			}
		}
		return results;
	}

	/**
	 * Return a list of TextFiles within a directory
	 * @param pathname The pathname to the directory to be inspected
	 * @return an ArrayList<TextFile> within that directory
	 */
	public ArrayList<TextFile> getTextFiles(String pathname) {
		File repertory = new File(pathname);

		if (repertory.isDirectory()) {
			for (File file : repertory.listFiles()) {
				files = getTextFiles(file.getAbsolutePath());
			}
		} else if (repertory.getAbsolutePath().endsWith(".odt")) {
			files.add(new ODTFile(repertory.getAbsolutePath()));
		}

		return files;
	}

	/**
	 * list the files from the database
	 * @return
	 */
	public ArrayList<Result> listFiles() {
		ArrayList<Result> result = new ArrayList<Result>();
		for(TextFile file: files){
			result.add(new Result(-1, -1, file.getFilename(), "", null));
		}
		return result;
	}
}
