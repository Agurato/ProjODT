package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public interface TextFile extends Serializable{
	public ArrayList<Result> contains(String search);
	public ArrayList<Result> listTitles();
	public HashMap<String, String> getInfos();
	public String getFilename();
}
