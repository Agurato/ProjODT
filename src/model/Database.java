package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Database {
	ArrayList<ODTFile> files;
	File rootFolder;
	
	public Database(String rootFolderPath) {
		files = new ArrayList<ODTFile>();
		rootFolder = new File(rootFolderPath);
	}
	
	public File getRoot() {
		return rootFolder;
	}

	public ArrayList<Result> search(String search) throws NoSuchElementException{
		// TODO Auto-generated method stub
		ArrayList<Result> results =  new ArrayList<Result>();
		if(!search.equals("")){
			results.add(new Result(1, "YolODT.odt", "You Only Live Once"));
			results.add(new Result(0, "bible.odt", "Genensis"));
			results.add(new Result(3, "RDJ.odt", "Rêves De Jeux"));
		}
		return results;
	}

	public ArrayList<Result> listFiles() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void sync() throws FileNotFoundException{
		// TODO Auto-generated method stub
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
