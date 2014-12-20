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
		ArrayList<Result> results =  new ArrayList<Result>();
		ArrayList<Result> exam = null;
		ArrayList<String> notFoundYet = new ArrayList<String>();	
		
		for(String str : search) {
			notFoundYet.add(str);
		}
			
		switch(operator) {
		case "and" :
			
			break;
		case "or" :
			for(String str : search) {
				System.out.println("str = \""+str+"\"");
				for(ODTFile odt : files) {
					exam = odt.examination(str);
					
					System.out.println("\tFile = "+odt.getFile().getAbsolutePath()+" :");
					
					if(exam.size() == 0) {
						System.out.println("\t\t\""+str+"\" not found !");
					}
					else {
						notFoundYet.remove(str);
						System.out.println("\t\t\""+str+"\" found and removed frome notFoundYet !");
					}
					for(Result tempResult : exam) {
						results.add(tempResult);
					}
				}
			}
			
			System.out.println("\nNot Found : ");
			for(String str : notFoundYet) {
				System.out.println("\""+str+"\"");
			}
			
			System.out.println("\nFinal results :");
			for(Result result : results) {
				System.out.println(result.getQuote());
			}
			System.out.println();
			
			break;
		case "null" :
			String keyword = search.get(0);
			
			for(ODTFile odt : files) {
				exam = odt.examination(keyword);
				System.out.println("File = "+odt.getFile().getAbsolutePath()+" :");
				
				if(exam.size() == 0) {
					System.out.println("\t\""+keyword+"\" not found !");
				}
				else {
					System.out.println("\t\""+keyword+"\" found !");
				}
				
				for(Result tempResult : exam) {
					results.add(tempResult);
				}
			}
			
			System.out.println("\nFinal results :");
			for(Result result : results) {
				System.out.println(result.getQuote());
			}
			System.out.println();
			
			break;
		}
		
		return results;
	}
	
	public void sync() {
		//We delete the extract folder and the arrayList
		files.clear();
		
		// We call the function to extract everything and parse it
		files = getOdtFiles(rootFolder.getAbsolutePath());
	}
	
	public void deleteFolders() {
		for(ODTFile odt : files) {
			odt.getExtract().delete();
		}
	}
	
	public ArrayList<ODTFile> getOdtFiles(String pathname) {
		File repertory = new File(pathname);
		
		if(repertory.isDirectory()) {
			for(File file : repertory.listFiles()) {
				files = getOdtFiles(file.getAbsolutePath());
			}
		}
		else if(repertory.getAbsolutePath().endsWith(".odt")){
			files.add(new ODTFile(repertory.getAbsolutePath()));
		}
		
		return files;
	}
}