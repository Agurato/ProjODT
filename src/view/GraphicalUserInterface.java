package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Map.Entry;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import controller.Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.BorderLayout;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import model.ODTFile;
import model.Result;

/**
 * Graphical User Interface
 * 
 * @author Louis Desportes
 * @version 1.0
 */

public class GraphicalUserInterface extends JFrame implements UserInterface {
	private static final long serialVersionUID = 1L;
	JMenuBar menuWIMP;
	JMenu fileMenu;
	JMenuItem chooseItem;
	JMenuItem syncItem;
	JMenuItem closeItem;
	JMenu helpMenu;
	JMenuItem helpItem;
	JMenuItem aboutItem;

	JTextField searchField;

	JList<Result> resultsList;
	DefaultListModel<Result> resultsModel;
	JPanel infoPanel;

	Controller controller;

	String root;

	public GraphicalUserInterface() {
		super("ProjODT");
		root = System.getProperty("user.dir");
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		getContentPane().setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Insets insets = getContentPane().getInsets();

		// Menu
		// adding the JmenuBar
		menuWIMP = new JMenuBar();
		this.setJMenuBar(menuWIMP);

		// adding the fileMenu
		fileMenu = new JMenu("Fichier");
		menuWIMP.add(fileMenu);
		// adding elements of fileMenu
		// ChangeRoot
		chooseItem = new JMenuItem("Changer la racine");
		chooseItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		fileMenu.add(chooseItem);
		chooseItem.addActionListener(new ChooseReact());
		// Sync
		syncItem = new JMenuItem("Synchroniser");
		syncItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		fileMenu.add(syncItem);
		syncItem.addActionListener(new SyncReact());
		// Close
		closeItem = new JMenuItem("Fermer");
		closeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		fileMenu.add(closeItem);
		closeItem.addActionListener(new CloseReact());

		// adding the helpMenu
		helpMenu = new JMenu("Aide");
		menuWIMP.add(helpMenu);
		// adding elements of helpMenu
		// Help
		helpItem = new JMenuItem("Manuel d'utilisation");
		helpItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		helpMenu.add(helpItem);
		helpItem.addActionListener(new HelpReact());
		// About
		aboutItem = new JMenuItem("À propos");
		aboutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		helpMenu.add(aboutItem);
		aboutItem.addActionListener(new AboutReact());

		// searchField
		searchField = new JTextField(10);
		getContentPane().add(searchField, BorderLayout.NORTH);

		// Results List
		resultsModel = new DefaultListModel<Result>();
		resultsList = new JList<Result>(resultsModel);
		resultsList.setCellRenderer(new ListResultCellRenderer());
		resultsList
				.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		resultsList.addListSelectionListener(new resultSelectReact());
		resultsList.addMouseListener(new resultClicReact());
		resultsList.setVisibleRowCount(-1);
		getContentPane().add(new JScrollPane(resultsList), BorderLayout.CENTER);
		resultsModel.addElement(new Result(-1, -1,
				"Entrez une chaine à rechercher", "", null));

		// infoPanel
		infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.PAGE_AXIS));
		getContentPane().add(new JScrollPane(infoPanel), BorderLayout.EAST);

		// Set size
		this.setSize(700 + insets.left + insets.right, 300 + insets.top
				+ insets.bottom);
		// Center the window
		this.setLocationRelativeTo(null);

		// Listen to actions
		searchField.addCaretListener(new SearchReact());

		setVisible(true);

		// Start controller
		controller = new Controller();

	}

	public GraphicalUserInterface(String root) {
		this();
		this.root = root;
		controller.changeRoot(root);
	}

	/**
	 * Listener to searchField modifications
	 * 
	 * @author Louis Desportes
	 * @version 1
	 */
	public class SearchReact implements CaretListener {

		@Override
		public void caretUpdate(CaretEvent e) {
			try {
				displayResults(controller.search(searchField.getText()));
			} catch (NoSuchElementException eNoElement) {
				JOptionPane.showMessageDialog(getContentPane(),
						"Impossible de trouver le dosser racine:",
						"Racine introuvable", JOptionPane.ERROR_MESSAGE);
				chooseRoot();
				displayResults(controller.search(searchField.getText()));
			}
		}

	}

	public class ListResultCellRenderer extends JLabel implements
			ListCellRenderer<Result> {

		private static final long serialVersionUID = 1;

		@Override
		public Component getListCellRendererComponent(
				JList<? extends Result> list, Result value, int index,
				boolean isSelected, boolean cellHasFocus) {
			JPanel cellPanel = new JPanel();
			cellPanel.setLayout(new BorderLayout());
			Result result = (Result) value;
			if (result.getLevel() == 0) {
				cellPanel.setFont(list.getFont());
			} else {
				cellPanel.setFont(cellPanel.getFont().deriveFont(Font.PLAIN));
			}

			if (isSelected) {
				cellPanel.setBackground(list.getSelectionBackground());
				cellPanel.setForeground(list.getSelectionForeground());
			} else {
				cellPanel.setBackground(list.getBackground());
				cellPanel.setForeground(list.getForeground());
			}

			cellPanel.add(new JLabel(result.getFilename().replace(root, "")),
					BorderLayout.WEST);
			if (result.getLevel() == -1) {
				cellPanel.add(new JLabel(result.getQuote()), BorderLayout.EAST);
			} else {
				cellPanel.add(new JLabel("« " + result.getQuote() + " »"),
						BorderLayout.EAST);
			}

			cellPanel.setEnabled(list.isEnabled());
			cellPanel.setOpaque(true);
			return cellPanel;
		}
	}

	/**
	 * Listener to doubleClic on resultsList
	 * 
	 * @author Louis Desportes
	 * @version 1
	 */
	public class resultClicReact implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				try {
					Controller.openFile(resultsModel.get(
							resultsList.locationToIndex(e.getPoint()))
							.getFilename());
				} catch (IOException e1) {
					JOptionPane
							.showMessageDialog(
									GraphicalUserInterface.this,
									"Impossible d'ouvrir le fichier, aucune application associé",
									"Ouverture impossible",
									JOptionPane.ERROR_MESSAGE);
				}
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}
	}

	/**
	 * resultsList element select listener
	 * 
	 * @author Louis Desportes
	 * @version 1
	 */
	private class resultSelectReact implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			BufferedImage thumbnail = resultsList.getSelectedValue()
					.getThumbnail();
			infoPanel.removeAll();
			// If thumbnail, add it
			if (thumbnail != null) {
				infoPanel.add(new JLabel(new ImageIcon(thumbnail)));
			}
			ODTFile file = new ODTFile(resultsList.getSelectedValue()
					.getFilename());
			displayInfos(file.parseMetaXML());
			infoPanel.setSize(thumbnail.getWidth() +
					getContentPane().getInsets().left +
					getContentPane().getInsets().right, 10*thumbnail.getHeight());
			setVisible(true);
		}
	}

	/**
	 * ChooseItem Listener
	 * 
	 * @author Louis Desportes
	 * @version 1
	 */
	private class ChooseReact implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			chooseRoot();
		}

	}

	/**
	 * syncItem Listener
	 * 
	 * @author Louis Desportes
	 * @version 1
	 */
	public class SyncReact implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			sync();
		}
	}

	/**
	 * closeItem Listener
	 * 
	 * @author Louis Desportes
	 * @version 1
	 */
	public class CloseReact implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
			System.exit(NORMAL);
		}
	}

	/**
	 * helpItem Listener
	 * 
	 * @author Louis Desportes
	 * @version 1
	 */
	public class HelpReact implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			displayHelp();
		}
	}

	/**
	 * aboutItem Listener
	 * 
	 * @author Louis Desportes
	 * @version 1
	 */
	public class AboutReact implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(getContentPane(),
					"ProjODT v1.0 — Vincent Monot & Louis Desportes",
					"À propos", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * Display results of search
	 * 
	 * @param results
	 *            the results to display
	 */
	@Override
	public void displayResults(ArrayList<Result> results) {
		resultsModel.removeAllElements();
		if (!results.isEmpty()) {
			for (Result result : results) {
				resultsModel.addElement(result);
			}
		} else {
			resultsModel.addElement(new Result(-1, -1,
					"Entrez une chaine à rechercher", "", null));
		}
		setVisible(true);
	}

	/**
	 * Change root folder dialog
	 */
	public void chooseRoot() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setDialogTitle("Sélectionnez le dossier où rechercher les fichiers");
		int returnVal = chooser.showOpenDialog(getContentPane());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			controller.changeRoot(chooser.getSelectedFile().getAbsolutePath());
			confirmChangeRoot(chooser.getSelectedFile().getAbsolutePath());
		}
	}

	/**
	 * Confirm that root folder has been changed
	 * 
	 * @param rootPath
	 *            The new root folder
	 */
	@Override
	public void confirmChangeRoot(String rootPath) {
		JOptionPane.showMessageDialog(this, "Racine changée vers: " + rootPath,
				"Racine changée", JOptionPane.INFORMATION_MESSAGE);

	}

	/**
	 * Sync dialog
	 */
	public void sync() {
		try {
			controller.sync();
			confirmSync();
		} catch (FileNotFoundException eNotFound) {
			JOptionPane
					.showMessageDialog(
							GraphicalUserInterface.this,
							"Le dossier sélectionné n'existe plus, veuillez en choisir un nouveau",
							"Dossier Inexistant", JOptionPane.ERROR_MESSAGE);
			chooseRoot();
		}
	}

	/**
	 * Confirm that the database has been sync
	 */
	@Override
	public void confirmSync() {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(this,
				"La base de donnée a bien été synchronisée",
				"Base de données synchronisé", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Open help page in default browser
	 */
	@Override
	public void displayHelp() {
		Desktop d = Desktop.getDesktop();
		try {
			d.browse(new URI("http://akkes.fr/projODT/"));
		} catch (IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * No need
	 */
	@Override
	public void listTitles(ArrayList<Result> titles) {
		// TODO Auto-generated method stub

	}

	/**
	 * No need
	 */
	@Override
	public void confirmOpening(String filename) {
		// TODO Auto-generated method stub

	}

	/**
	 * No need
	 */
	@Override
	public void listFiles(ArrayList<Result> files) {
		// TODO Auto-generated method stub

	}

	/**
	 * No need
	 */
	@Override
	public void displayInfos(HashMap<String, String> infos) {
		for (Entry<String, String> entry : infos.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
			JPanel tempPanel = new JPanel(new BorderLayout());
			tempPanel.add(new JLabel(entry.getKey()), BorderLayout.LINE_START);
			tempPanel.add(new JLabel(entry.getValue()), BorderLayout.LINE_END);
			infoPanel.add(tempPanel);
		}
	}
}
