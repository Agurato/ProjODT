package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;

public interface TextFile extends Serializable {
	public ArrayList<Result> contains(String search);

	public ArrayList<Result> listTitles();

	public HashMap<String, String> getInfos();

	public String getFilename();

	public ImageIcon getThumbnail();
}
