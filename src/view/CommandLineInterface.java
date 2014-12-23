package view;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import controller.Controller;
import model.Result;

public class CommandLineInterface implements UserInterface{

	private enum Action{
		NONE, SEARCH, LIST, DISPLAY
	}
	public CommandLineInterface(String[] args) {
		Controller controller = new Controller();
		int index = 0;
		Action action = Action.NONE;
		String param = "";
		
		if(args[index].equals("-f") || args[index].equals("-d")) {
			index ++;
			action = Action.LIST;
		}
		else if(args[index].equals("-w")) {
			index ++;
			action = Action.SEARCH;
			while(index<args.length) {
				if(!args[index].startsWith("-")) {
					// We add a space to separate the words
					param += " "+args[index];
					index ++;
				}
			}
			// We delete the first character (' ') if we added another keyword
			if(index > 1) {
				param.substring(1);
			}
		}
		
		switch(action) {
		case LIST :
			controller = new Controller(args[index]);
			
			break;
		case SEARCH :
			for(Result res : controller.search(param)) {
				System.out.println(res);
			}
			
			break;
		default :
			
			break;
		}
		
//		if(args[index].equals("search")){
//			index++;
//			while (!args[index].startsWith("--") && index<args.length){
//				param += args[index];
//				index++;
//			}
//			action = Action.SEARCH;
//		}else if(args[index].equals("list")){
//			index++;
//			action = Action.LIST;
//		}else if(args[index].equals("display")){
//			action = Action.DISPLAY;
//			index++;
//		}else if(args[index].startsWith("--")){
//			
//		}
//		
//		//Execute action
//		switch(action){
//		case SEARCH:
//			controller.search(param);
//		break;
//		case DISPLAY:
//			controller.search(param);
//		break;
//		}
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
