package view;

import java.util.ArrayList;

import controller.Controller;
import model.Result;

public class CommandLineInterface implements UserInterface{

	public CommandLineInterface(String[] args) {
		Controller controller = new Controller();
		if(args[0].equals("search")){
			controller.search(args[1]);
		}else if(args[0].equals("list")){
			controller.listFiles();
		}else if(args[0].equals("display")){
			
		}
	}

	@Override
	public void displayResults(ArrayList<Result> results) {
		// TODO Auto-generated method stub
		
	}

	public void chooseRoot() {
		// TODO Auto-generated method stub
		
	}

	public void sync() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void confirmSync() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void confirmChangeRoot(String rootPath) {
		// TODO Auto-generated method stub
		
	}

}
