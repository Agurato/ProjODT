package model;

import java.io.File;
import java.util.ArrayList;

public class DataBase {
	ArrayList<ODTFile> files;
	File rootFolder;

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

	public ArrayList<Result> search(ArrayList<String> search, String operator) {
		ArrayList<Result> results = new ArrayList<Result>(); // Return
		ArrayList<Result> exam = null; // Stocks what we searched in a file
		ArrayList<String> notFoundYet = new ArrayList<String>(); // Helps to find which String we didn't found yet

		// Remove duplicates
		for (int i = 0; i < search.size(); i++) {
			for (int j = i + 1; j < search.size(); j++) {
				if (search.get(i).equals(search.get(j))) {
					search.remove(j);
				}
			}
		}

		notFoundYet.addAll(search);
		
		switch (operator) {

		case "and":
			
			for (ODTFile odt : files) {
				System.out.println("File = "+ odt.getFile().getAbsolutePath() + " :");
				
				ArrayList<Result> res = new ArrayList<Result>(); // Used to add every result for 1 file
																 // exam is used for 1 file and 1 String
				
				for (String str : search) {
					exam = odt.examination(str);
					res.addAll(exam);

					if (exam.size() == 0) { // If there wasn't that word in the file
						System.out.println("\t\"" +str + "\" not found !");
					}
					else {	// Else, we remove it from notFoundYet
						notFoundYet.remove(str);
						System.out.println("\t\"" +str+ "\" found and removed from notFoundYet !");
					}
				}
				// If we found all the Strings in the same file, we add them to results
				if(notFoundYet.size() == 0) {
					for (Result tempResult : res) {
						results.add(tempResult);
					}
				}
				// Else, we clear the list and re-add every terms in search
				else {
					notFoundYet.clear();
					for (String str : search) {
						notFoundYet.add(str);
					}
				}
				res.clear(); // We clear res to go on with the next file
			}
			
			break;

		case "or":

			for (ODTFile odt : files) {
				System.out.println("File = "+ odt.getFile().getAbsolutePath() + " :");

				for (String str : search) {
					exam = odt.examination(str);

					if (exam.size() == 0) {
						System.out.println("\t\"" + str + "\" not found !");
					}
					else {
						notFoundYet.remove(str);
						System.out.println("\t\"" + str + "\" found "+ exam.size()+ " time(s) and removed from notFoundYet !");
					}
					
					for (Result tempResult : exam) {
						results.add(tempResult);
					}
				}
			}

			System.out.println("\nNot Found : ");
			for (String str : notFoundYet) {
				System.out.println("\"" + str + "\"");
			}

			break;

		case "null":
			String keyword = search.get(0);

			for (ODTFile odt : files) {
				exam = odt.examination(keyword);
				System.out.println("File = " + odt.getFile().getAbsolutePath()+ " :");

				if (exam.size() == 0) {
					System.out.println("\t\"" + keyword + "\" not found !");
				}
				else {
					System.out.println("\t\"" + keyword + "\" found !");
				}

				for (Result tempResult : exam) {
					results.add(tempResult);
				}
			}

			break;
		default:
			break;
		}

		// If there is duplicates, remove them and increment the frequency
		for (int i = 0; i < results.size(); i++) {
			Result res = results.get(i);
			for (int j = i + 1; j < results.size(); j++) {
				if (res.equals(results.get(j))) {
					res.setFrequency(res.getFrequency() + 1);
					results.remove(j);
				}
			}
		}
		
		System.out.println("\nFinal results :");
		for (Result result : results) {
			System.out.println(result.toString());
		}
		System.out.println();

		return results;
	}

	public void sync() {
		// We delete the extract folder and the arrayList
		this.deleteFolders();
		files.clear();

		// We call the function to extract everything and parse it
		files = getOdtFiles(rootFolder.getAbsolutePath());
	}

	public void deleteFolders() {
		for (ODTFile odt : files) {
			odt.getExtract().delete();
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