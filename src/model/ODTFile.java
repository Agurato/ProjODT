package model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * @author vincent
 *
 */

public class ODTFile implements TextFile {
	
	private File odt = null;
	private File repertory = null;
	private File extract = null;
	private File results = null;
	private final String separator = ";";

	public ODTFile(String path) {
		this.odt = new File(path);
		this.repertory = new File(odt.getParent());
		this.extract = new File(repertory.getAbsolutePath()+"/"+odt.getName().replace(".odt", ""));
		this.results = new File(extract.getAbsolutePath()+"/results.txt");
		
		unzipODT();
		parseContentXML();
	}
	
	public File getOdt() {
		return odt;
	}
	
	public File getExtract() {
		return extract;
	}

	public void unzipODT() {
		extract.mkdir();
		// Create the folder for the extraction
		
		File bufferFile = null; // Used to extract all files one by one
		try {
			ZipInputStream zipI = new ZipInputStream(new BufferedInputStream(new FileInputStream(odt.getAbsolutePath())));
			ZipEntry zipE = null; // Like an Iterator
			
			while ((zipE = zipI.getNextEntry()) != null) {
				bufferFile = new File(extract.getAbsolutePath(), zipE.getName());
				
				if(zipE.isDirectory()) {
					bufferFile.mkdirs();
					continue;
				}
				bufferFile.getParentFile().mkdirs();
				
				BufferedOutputStream extractBuffer = new BufferedOutputStream(new FileOutputStream(bufferFile));
				
				try {
					try {
						byte[] byteBuffer = new byte[8192];
						int bytesRead;
						while(-1 != (bytesRead = zipI.read(byteBuffer))) {
							extractBuffer.write(byteBuffer, 0, bytesRead);
						}
					} finally {
						extractBuffer.close();
					}
				} catch(IOException ioe) {
					bufferFile.delete();
				}				
			}

			zipI.close();
		}
		catch(FileNotFoundException fnne) {
			fnne.printStackTrace();
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public void parseContentXML() {
		String writing = "", lineSeparator = "#";
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		try {
			System.out.println("In file : "+odt.getAbsolutePath()+" :");
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document parser = builder.parse(new File(extract.getAbsolutePath(), "content.xml"));
			
			Element root = parser.getDocumentElement();
			// This is the "office:document-content", which contains everything in the content.xml file
			
			// Instantly get the "office:text" list even if it's not a direct child
			NodeList officeTextList = root.getElementsByTagName("office:text");
			Element officeText = (Element) officeTextList.item(0);
			
			// Same, it takes the "text:title" and "text:h" elements
			NodeList textTitleList = officeText.getElementsByTagName("text:title");
			NodeList textHList = officeText.getElementsByTagName("text:h");
			
			for(int i=0 ; i<textTitleList.getLength() ; i++) {
				Element textTitle = (Element) textTitleList.item(i);
				System.out.println(textTitle.getTextContent());
				
				writing += "text:title"+separator+"0"+separator+textTitle.getTextContent()+lineSeparator;
				// Add the useful informations to write in the file later
			}
			
			
			for(int i=0 ; i<textHList.getLength() ; i++) {
				Element textH = (Element) textHList.item(i);
				System.out.println(textH.getTextContent());
				
				writing += "text:h"+separator+textH.getAttribute("text:outline-level")+separator+textH.getTextContent()+lineSeparator;
				
			}
			
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(results));
				
				for(String str : writing.split(lineSeparator)) {
					bw.write(str);
					bw.newLine();
				}
				
				bw.close();
			}
			catch(IOException ioe) {
				ioe.printStackTrace();
			}
		}
		catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}
		catch(SAXException saxe) {
			saxe.printStackTrace();
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public ArrayList<Result> examination(String search) {
		boolean resultsExists = false;
		ArrayList<Result> result = new ArrayList<Result>();
		
		for(File file : repertory.listFiles()) {
			if(file.getName().equals("results.txt")) {
				resultsExists = true;
			}
		}
		
		if(!resultsExists) {
			this.parseContentXML();
		}
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(results));
			String temp = null;
			String[] split;
			
			while((temp = br.readLine()) != null) {
				split = temp.split(separator);
				if(split[2].contains(search)) {
					result.add(new Result(Integer.parseInt(split[1]), odt.getAbsolutePath()));
				}
			}
			
			br.close();
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
		
<<<<<<< HEAD
		return result;
=======
		return new Result(0, odt.getAbsolutePath(), "Quote");
>>>>>>> 07c5efef2f41de8855188e28a4fa2140d8e89520
	}
}
