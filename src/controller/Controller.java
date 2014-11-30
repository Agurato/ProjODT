package controller;
import view.UserInterface;
import view.GraphicalUserInterface;

public class Controller {
	static UserInterface ui;
	public static void main(String[] Args){
		ui = (UserInterface) new GraphicalUserInterface();
	}
}
