package model;

import java.io.File;
import java.util.ArrayList;

public class DataBase {
	ArrayList<ODTFile> files;
	File rootFolder;
	
	public DataBase(String rootFolderPath) {
		files = new ArrayList<ODTFile>();
		rootFolder = new File(rootFolderPath);
	}
	
	public File getRoot() {
		return rootFolder;
	}

	public ArrayList<Result> search(String search) {
		// TODO Auto-generated method stub
		ArrayList<Result> results =  new ArrayList<Result>();
		if(!search.equals("")){
			results.add(new Result(156364, "YolODT.odt"));
			results.add(new Result(264962, "bible.odt"));
			results.add(new Result(260864, "RDJ.odt"));
		}
		return results;
	}

	public ArrayList<Result> listFiles() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void changeRoot(String pathName) {
		// TODO Auto-generated method stub
		
	}
	
	public void sync() {
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
