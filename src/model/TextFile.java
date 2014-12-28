package model;

import java.util.ArrayList;
import java.util.HashMap;

public interface TextFile {
	public ArrayList<Result> contains(String search);
	public ArrayList<Result> listTitles();
	public HashMap<String, String> getInfos();
}
