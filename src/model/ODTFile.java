package model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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

	public void unzipODT() throws FileNotFoundException, IOException {
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
	}
	/*
		for(File file : folder.listFiles()) {
			if(!file.getName().equals("content.xml")) {
				if(!file.isDirectory()) {
					file.delete();
				}
			}
		}
	*/
}
