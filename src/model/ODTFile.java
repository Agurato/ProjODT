package model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Monot
 *
 */

public class ODTFile implements TextFile, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private File odt = null;
	private File repertory = null;
	private File extractedRepertory = null;
	private ArrayList<Result> titles = null;
	private HashMap<String, String> infos;
	private String path;
	ImageIcon thumbnail;

	/**
	 * 
	 * @param path The path of the {@link ODTFile}
	 */

	public ODTFile(String path) {
		if (path.endsWith(".odt")) {
			this.odt = new File(path);
			this.repertory = new File(odt.getParent());
			this.extractedRepertory = new File(repertory.getAbsolutePath()
					+ "/" + odt.getName().replace(".odt", ""));
			this.unzipODT();
			this.titles = new ArrayList<Result>();
			parseContentXML();
			infos = new HashMap<String, String>();
			parseMetaXML();
			parseThumbnail();
			suppExtract(extractedRepertory);
		} else {
			System.out.println("The given path : " + path
					+ " doesn't correspond to a odt file");
		}
		this.path = path;
	}

	/**
	 * 
	 * @return the odt as a {@link File}
	 */

	public File getFile() {
		return odt;
	}

	/**
	 * 
	 * @return the odt as a {@link String} (pathname)
	 */

	public String getFilename() {
		return path;
	}

	/**
	 * Delete the extract folder entirely
	 * 
	 * @param supp The {@link File} to be deleted. Can be a folder (Recusive).
	 */

	private void suppExtract(File supp) {
		for (File file : supp.listFiles()) {
			if (file.isDirectory()) {
				suppExtract(file);
			}
			file.delete();
		}
		if (extractedRepertory.listFiles().length == 0) {
			extractedRepertory.delete();
		}
	}

	/**
	 * Extract the odt file in a folder which has the same name (without ".odt"
	 * at the end)
	 */

	private void unzipODT() {
		extractedRepertory.mkdir();
		// Create the folder for the extraction

		File bufferFile = null; // Used to extract all files one by one
		try {
			ZipInputStream zipI = new ZipInputStream(new BufferedInputStream(
					new FileInputStream(odt.getAbsolutePath())));
			ZipEntry zipE = null; // Like an Iterator

			while ((zipE = zipI.getNextEntry()) != null) {
				bufferFile = new File(extractedRepertory.getAbsolutePath(),
						zipE.getName());

				if (zipE.isDirectory()) {
					bufferFile.mkdirs();
					continue;
				}
				bufferFile.getParentFile().mkdirs();

				BufferedOutputStream extractBuffer = new BufferedOutputStream(
						new FileOutputStream(bufferFile));

				try {
					try {
						byte[] byteBuffer = new byte[8192];
						int bytesRead;
						while (-1 != (bytesRead = zipI.read(byteBuffer))) {
							extractBuffer.write(byteBuffer, 0, bytesRead);
						}
					} finally {
						extractBuffer.close();
					}
				} catch (IOException ioe) {
					bufferFile.delete();
				}
			}

			zipI.close();
		} catch (FileNotFoundException fnne) {
			fnne.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * Parse the content.xml file contained in the extract folder Save the
	 * titles in "results.txt"
	 */

	private void parseContentXML() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document parser = builder.parse(new File(extractedRepertory
					.getAbsolutePath(), "content.xml"));

			// This is the "office:document-content", which contains everything
			// in the content.xml file
			Element root = parser.getDocumentElement();

			// Instantly get the "office:text" list even if it's not a direct
			// child
			Element officeText = (Element) root.getElementsByTagName(
					"office:text").item(0);

			// Same, it takes the "text:title" and "text:h" elements
			NodeList textTitleList = officeText
					.getElementsByTagName("text:title");
			NodeList textHList = officeText.getElementsByTagName("text:h");

			for (int i = 0; i < textTitleList.getLength(); i++) {
				Element textTitle = (Element) textTitleList.item(i);

				titles.add(new Result(0, 1, odt.getAbsolutePath(), textTitle
						.getTextContent()));
				// Add the useful informations to write in the file later
			}

			for (int i = 0; i < textHList.getLength(); i++) {
				Element textH = (Element) textHList.item(i);
				titles.add(new Result(Integer.parseInt(textH
						.getAttribute("text:outline-level")), 1, odt
						.getAbsolutePath(), textH.getTextContent()));
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Parse the meta.xml file
	 */

	private void parseMetaXML() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document parser = builder.parse(new File(extractedRepertory
					.getAbsolutePath(), "meta.xml"));

			Element root = parser.getDocumentElement();

			// Get the office version
			String officeVersion = root.getAttribute("office:version");
			if (officeVersion != null) {
				infos.put("officeVersion", officeVersion);
			}

			Element officeMeta = (Element) root.getElementsByTagName(
					"office:meta").item(0);

			// Get the creator
			Element dcCreator = (Element) officeMeta.getElementsByTagName(
					"dc:creator").item(0);
			if (dcCreator != null) {
				infos.put("creator", dcCreator.getTextContent());
			}

			// Get the initial creator
			Element initialCreator = (Element) officeMeta.getElementsByTagName(
					"meta:initial-creator").item(0);
			if (initialCreator != null) {
				infos.put("initialCreator", initialCreator.getTextContent());
			}

			// Get the date
			Element dcDate = (Element) officeMeta.getElementsByTagName(
					"dc:date").item(0);
			if (dcDate != null) {
				infos.put("date", dcDate.getTextContent());
			}

			// Get the initial date
			Element initialDate = (Element) officeMeta.getElementsByTagName(
					"meta:creation-date").item(0);
			if (initialDate != null) {
				infos.put("initialDate", initialDate.getTextContent());
			}

			// Get the title
			Element dcTitle = (Element) officeMeta.getElementsByTagName(
					"dc:title").item(0);
			if (dcTitle != null) {
				infos.put("title", dcTitle.getTextContent());
			}

			// Get the subject
			Element dcSubject = (Element) officeMeta.getElementsByTagName(
					"dc:subject").item(0);
			if (dcSubject != null) {
				infos.put("subject", dcSubject.getTextContent());
			}

			Element pageCount = (Element) officeMeta.getElementsByTagName(
					"meta:document-statistic").item(0);
			infos.put("pageCount", pageCount.getAttribute("meta:page-count"));
			infos.put("wordCount", pageCount.getAttribute("meta:word-count"));

		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Return Infos about the {@link ODTFile}
	 * @return An {@link HashMap} of the infos
	 */
	public HashMap<String, String> getInfos() {
		return infos;
	}

	/**
	 * Used to search a String among the titles of the odt file
	 * 
	 * @return a list of {@link Result} after searching in the file
	 */
	public ArrayList<Result> contains(String search) {
		ArrayList<Result> contained = new ArrayList<Result>();

		for (Result title : titles) {
			// correct Previous modifications

			String quote = title.getQuote();
			quote = Normalizer.normalize(quote, Normalizer.Form.NFD)
					.replaceAll("[\u0300-\u036F]", "");

			title.setFrequency(1);
			if (quote.toLowerCase().contains(search.toLowerCase())) {
				contained.add(title);
			}
		}

		return contained;
	}

	/**
	 * Parse the thumbnail from the {@link ODTFile}
	 */
	private void parseThumbnail() {
		try {
			thumbnail = new ImageIcon(ImageIO.read(new File(extractedRepertory
					.getAbsolutePath() + "/Thumbnails/thumbnail.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Return the thumbnail
	 * @return The thumbnail as an {@link ImageIcon}
	 */
	public ImageIcon getThumbnail() {
		return thumbnail;
	}

	/**
	 * @return a list of titles as {@link Result}
	 */
	public ArrayList<Result> listTitles() {
		return titles;
	}
}