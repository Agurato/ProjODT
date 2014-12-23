package model;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class DataBase {
	ArrayList<ODTFile> files;
	File rootFolder;

	public DataBase() {
		files = new ArrayList<ODTFile>();
	}

	public DataBase(String rootFolderPath) {
		rootFolder = new File(rootFolderPath);
		files = new ArrayList<ODTFile>();
		files = getOdtFiles(rootFolder.getAbsolutePath());
	}

	public File getRoot() {
		return rootFolder;
	}

	public ArrayList<ODTFile> getOdt() {
		return files;
	}

	public void setRoot(String rootFolderPath) {
		rootFolder = new File(rootFolderPath);
	}

	public void addOdt(ODTFile odt) {
		files.add(odt);
	}
	
	public void addOdt(String path) {
		files.add(new ODTFile(path));
	}

	public void sync() {
		// We delete the extract folder and the arrayList
		this.deleteFolders();
		files.clear();

		// We call the function to extract every odt
		files = getOdtFiles(rootFolder.getAbsolutePath());
		this.parse();
	}

	public void parse() {
		for (ODTFile odt : files) {
			odt.parseContentXML();
		}
	}

	public ArrayList<Result> contains(String search) {
		ArrayList<Result> results = new ArrayList<Result>(); // Return
		ArrayList<Result> exam = null; // Stocks what we searched in a file

		for (ODTFile odt : files) {
			exam = odt.examination(search);
			System.out.println("File = " + odt.getFile().getAbsolutePath()
					+ " :");

			if (exam.size() == 0) {
				System.out.println("\t\"" + search + "\" not found !\n");
			} else {
				System.out.println("\t\"" + search + "\" found !\n");
			}

			results.addAll(exam);
		}
		return results;
	}

	public ArrayList<Result> union(ArrayList<Result> list1, ArrayList<Result> list2) {
		Set<Result> set = new HashSet<Result>();

		set.addAll(list1);
		set.addAll(list2);

		return new ArrayList<Result>(set);
	}

	public ArrayList<Result> intersection(ArrayList<Result> list1, ArrayList<Result> list2) {
		ArrayList<Result> list = new ArrayList<Result>();

		for (Result t : list1) {
			if (list2.contains(t)) {
				list.add(t);
			}
		}

		return list;
	}

	public ArrayList<Result> search(String search) {
		ArrayList<Result> results = new ArrayList<Result>();

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
		for (int i = 0; i < orResults.size(); i++){
			Result tempResult = orResults.get(i);
			System.out.println(">" + tempResult.getQuote() + ": " + tempResult.getLevel());
			for(int j = i+1; j < orResults.size();){
				
				// If there already is a result with the same filename
				if (tempResult.getFilename().equals(orResults.get(j).getFilename())){
					int freq = tempResult.getFrequency() + orResults.get(j).getFrequency();
					System.out.println("+->" + orResults.get(j).getQuote() + ": " + orResults.get(j).getLevel());
					
					// If second result has a upper title level
					System.out.println(" +->" + tempResult.getQuote() + ": " + tempResult.getLevel() + ", "
							+orResults.get(j).getQuote() + ": " + orResults.get(j).getLevel());
					if(tempResult.getLevel() >= orResults.get(j).getLevel()){
						System.out.println("upper");
						tempResult = orResults.get(j);
					}
					
					tempResult.setFrequency(freq);
					orResults.remove(j);
				}else{// We go forward
					j++;
				}
			}
			results.add(tempResult);
		}

		return results;
	}

	public void deleteFolders() {
		for (ODTFile odt : files) {
			odt.suppExtract(odt.getExtract());
		}
	}

	public ArrayList<ODTFile> getOdtFiles(String pathname) {
		File repertory = new File(pathname);

		if (repertory.isDirectory()) {
			for (File file : repertory.listFiles()) {
				files = getOdtFiles(file.getAbsolutePath());
			}
		} else if (repertory.getAbsolutePath().endsWith(".odt")) {
			files.add(new ODTFile(repertory.getAbsolutePath()));
		}

		return files;
	}
}
