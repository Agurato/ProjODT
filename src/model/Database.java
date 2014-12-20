package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Database {
	ArrayList<ODTFile> files;
	File rootFolder;
	
<<<<<<< HEAD:src/model/DataBase.java
	public DataBase(String rootFolderPath) {
=======
	public Database(String rootFolderPath) {
		files = new ArrayList<ODTFile>();
>>>>>>> 07c5efef2f41de8855188e28a4fa2140d8e89520:src/model/Database.java
		rootFolder = new File(rootFolderPath);
		files = new ArrayList<ODTFile>();
		files = getOdtFiles(rootFolder.getAbsolutePath());
	}
	
	public File getRoot() {
		return rootFolder;
	}

<<<<<<< HEAD:src/model/DataBase.java
	public ArrayList<Result> search(String search) {
		ArrayList<Result> results =  new ArrayList<Result>();
		
		for(ODTFile odt : files) {
			for(Result tempResult : odt.examination(search)) {
				results.add(tempResult);
			}
=======
	public ArrayList<Result> search(String search) throws NoSuchElementException{
		// TODO Auto-generated method stub
		ArrayList<Result> results =  new ArrayList<Result>();
		if(!search.equals("")){
			results.add(new Result(1, "YolODT.odt", "You Only Live Once"));
			results.add(new Result(0, "bible.odt", "Genensis"));
			results.add(new Result(3, "RDJ.odt", "Rêves De Jeux"));
>>>>>>> 07c5efef2f41de8855188e28a4fa2140d8e89520:src/model/Database.java
		}
		
		return results;
	}
	
<<<<<<< HEAD:src/model/DataBase.java
	public void changeRoot(String pathName) {
		rootFolder = new File(pathName);
	}
	
	public void sync() {
		this.delete();
		
		// We call the function to extract everything and parse it
		files = getOdtFiles(rootFolder.getAbsolutePath());
	}
	
	public void delete() {
		//We delete the extract folder and the arrayList
		for(ODTFile odt : files) {
			odt.getExtract().delete();
		}
		files.clear();
=======
	public void sync() throws FileNotFoundException{
		// TODO Auto-generated method stub
>>>>>>> 07c5efef2f41de8855188e28a4fa2140d8e89520:src/model/Database.java
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
