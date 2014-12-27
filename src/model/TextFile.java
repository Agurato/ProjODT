package model;

import java.util.ArrayList;
import java.util.HashMap;

public interface TextFile {
	public ArrayList<Result> examination(String search);
	public ArrayList<Result> listTitles();
	public HashMap<String, String> parseMetaXML();
}
