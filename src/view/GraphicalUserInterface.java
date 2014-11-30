package view;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Window;

import model.Result;

public class GraphicalUserInterface extends JFrame implements UserInterface{
	private static final long serialVersionUID = 1L;
	JScrollPane scrollPanel;
	JPanel resultsPanel;
	JButton addButton;
	JTextField searchField;

	public GraphicalUserInterface() {
		super("ProjODT");
		getContentPane().setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Insets insets = getContentPane().getInsets();
		
		//TextField
		searchField = new JTextField(10);
		getContentPane().add(searchField);

		//Add Button
		addButton = new JButton("Add");
		getContentPane().add(addButton);

		//Results Scroll
		resultsPanel = new JPanel();
		resultsPanel.add(new JLabel("Tapez votre recherche et appuyez sur entrée."));
		scrollPanel = new JScrollPane(resultsPanel);
		getContentPane().add(this.scrollPanel);
		
		//Set size
		this.setSize(500 + insets.left + insets.right,
				300 + insets.top + insets.bottom);

		//Set Placement
		searchField.setBounds(
				insets.left,
				insets.top, 
				(int)(this.getWidth() - addButton.getPreferredSize().getWidth() - insets.left - insets.right),
				searchField.getPreferredSize().height);
		addButton.setBounds(
				insets.left + searchField.getWidth(),
				insets.top, 
				(int)(addButton.getPreferredSize().getWidth()),
				(int)(addButton.getPreferredSize().getHeight()));
		scrollPanel.setBounds(
				insets.left,
				(int)(insets.top + searchField.getPreferredSize().getHeight()),
				(int)(this.getWidth() - insets.left - insets.right),
				(int)(this.getHeight() - insets.top - insets.bottom - searchField.getPreferredSize().getHeight()));
		
		//Listen to actions
		this.addComponentListener(new resizeAction());
		searchField.addActionListener(new searchReact());
		
		setVisible(true);
	}

	//Resize Listening
	public class resizeAction implements ComponentListener{

		@Override
		public void componentResized(ComponentEvent e) {
			Insets insets = getContentPane().getInsets();
			//Set Placement
			searchField.setBounds(
					insets.left,
					insets.top, 
					(int)(getFrames()[0].getWidth() - addButton.getPreferredSize().getWidth() - insets.left - insets.right),
					searchField.getPreferredSize().height);
			addButton.setBounds(
					insets.left + searchField.getWidth(),
					insets.top, 
					(int)(addButton.getPreferredSize().getWidth()),
					(int)(addButton.getPreferredSize().getHeight()));
			scrollPanel.setBounds(
					insets.left,
					(int)(insets.top + searchField.getPreferredSize().getHeight()),
					(int)(getFrames()[0].getWidth() - insets.left - insets.right),
					(int)(getFrames()[0].getHeight() - insets.top - insets.bottom - searchField.getPreferredSize().getHeight()));
			
		}

		@Override
		public void componentMoved(ComponentEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void componentShown(ComponentEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void componentHidden(ComponentEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	//text entered listening
	public class searchReact implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Text: " + searchField.getText());
			
		}
		
	}
	@Override
	public void displayResults(ArrayList<Result> results) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void confirmAdd(String fileName) {
		JOptionPane confirmation = new JOptionPane();
		confirmation.showMessageDialog(null, fileName + " a bien été ajouté à la base de donnée.", "Fichier ajouté", JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	public void confirmDelelte(String fileName) {
		JOptionPane deletion = new JOptionPane();
		deletion.showMessageDialog(null, fileName + " a bien été supprimé de la base de donnée.", "Fichier supprimé", JOptionPane.INFORMATION_MESSAGE);
		
	}

	@Override
	public void confirmSync(String fileName) {
		JOptionPane confirmation = new JOptionPane();
		confirmation.showMessageDialog(null, "La base de donnée a bien été synchronisée", "Base de données synchronisé", JOptionPane.INFORMATION_MESSAGE);
		
	}

}
