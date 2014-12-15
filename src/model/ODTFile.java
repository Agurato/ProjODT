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
	private File repository = null;
	private String path = null;

	public ODTFile(String path) {
		this.odt = new File(path);
		this.repository = new File(odt.getParent());
		this.path = path;
	}
	
	public String getPath() {
		return path;
	}

	public File unzipODT() throws FileNotFoundException, IOException {
		// Create the folder where it will be unzipped
		File folder = new File(repository.getAbsolutePath()+"/"+odt.getName().replace(".odt", ""));
		folder.mkdir();
		
		File bufferFile = null; // Used to extract all files one by one
		ZipInputStream zipI = new ZipInputStream(new BufferedInputStream(new FileInputStream(path)));
		ZipEntry zipE = null; // Like an Iterator
		try {
			while ((zipE = zipI.getNextEntry()) != null) {
				bufferFile = new File(folder.getAbsolutePath(), zipE.getName());
				
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
					throw ioe;
				}				
			}
		}
		finally {
			zipI.close();
		}
		return folder;
	}
	
	public Result parseContentXML(File folder, CharSequence search) {
		String writing = "";
		String separator = ";", lineSeparator = "#";
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(folder.getAbsolutePath()+"/results.txt"));
			bw.close();
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		try {
			System.out.println("In file : "+path+" :");
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document parser = builder.parse(new File(folder.getAbsolutePath(), "content.xml"));
			
			Element root = parser.getDocumentElement();
			// This is the "office:document-content", which contains everything in the content.xml file
			
			// Instantly get the "office:text" list even if it's not a direct child
			NodeList officeTextList = root.getElementsByTagName("office:text");
			Element officeText = (Element) officeTextList.item(0);

			NodeList textTitleList = officeText.getElementsByTagName("text:title");
			NodeList textHList = officeText.getElementsByTagName("text:h");
			
			// Search among them the search value
			for(int i=0 ; i<textTitleList.getLength() ; i++) {
				Element textTitle = (Element) textTitleList.item(i);
				System.out.println(textTitle.getTextContent());
				
				writing += "text:title"+separator+textTitle.getTextContent()+lineSeparator;
			}
			
			
			for(int i=0 ; i<textHList.getLength() ; i++) {
				Element textH = (Element) textHList.item(i);
				System.out.println(textH.getTextContent());
				
				writing += "text:h"+separator+textH.getAttribute("text:outline-level")+separator+textH.getTextContent()+lineSeparator;
				
			}
			
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(folder.getAbsolutePath()+"/results.txt"));
				
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
		
		return new Result(0, path);
	}
	
	void saveParseSearch() {
		
	}
}
