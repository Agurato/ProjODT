package model;

import java.util.ArrayList;

public class DataBase {

	public ArrayList<Result> search(String search) {
		// TODO Auto-generated method stub
		ArrayList<Result> results =  new ArrayList<Result>();
		if(!search.equals("")){
			results.add(new Result(156364, "YolODT.odt", "... C'est le Cancer! ..."));
			results.add(new Result(264962, "bible.odt", "... Et dieu dit: que ..."));
			results.add(new Result(260864, "RDJ.odt", "... Jeu de rôle ..."));
		}
		return results;
	}

	public ArrayList<Result> listFiles() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean add(String fileName) {
		// TODO Auto-generated method stub
		return true;
	}
	
	public boolean delete(String fileName) {
		// TODO Auto-generated method stub
		return true;
	}
	
	public boolean sync() {
		// TODO Auto-generated method stub
		return true;
	}
}
