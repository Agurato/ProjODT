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

	public ArrayList<Result> search(String search) {
		ArrayList<Result> results =  new ArrayList<Result>();
		
		for(ODTFile odt : files) {
			for(Result tempResult : odt.examination(search)) {
				results.add(tempResult);
			}
		}
		
		return results;
	}
	
	public void changeRoot(String pathName) {
		rootFolder = new File(pathName);
	}
	
	public void sync() {
		//We delete the extract folder and the arrayList
		for(ODTFile odt : files) {
			odt.getExtract().delete();
		}
		files.clear();
		
		// We call the function to extract everything and parse it
		files = getOdtFiles(rootFolder.getAbsolutePath());
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

	public ArrayList<Result> listFiles() {
		// TODO Auto-generated method stub
		ArrayList<Result> results = new ArrayList<Result>();
		results.add(new Result(1, "YolODT.odt", "C'est le Cancer!"));
		results.add(new Result(0, "bible.odt", "Et dieu dit: que"));
		results.add(new Result(2, "RDJ.odt", "Jeu de rôle"));
		return results;
	}
}