package mybox.io;

import java.io.*;

import piwotools.log.Log;

public class FileChunk implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private long lastModified;
	private String checksum;
	private String name;
	private byte[] data;

	public FileChunk(String filename) {
		this.name = filename;
	}
	
	public String getName() {
		return name;
	}
	
	public long lastModified() {
		return lastModified;
	}
	
	public String getChecksum() {
		return checksum;
	}
	
	public void read(String directory) throws IOException {
		FileInputStream inputStream = null;
		try {
			Log.debug("Reading file " + directory + name);
			File file = new File(directory + name);
			lastModified = file.lastModified();
			data = new byte[(int) file.length()];
			inputStream = new FileInputStream(file);
			inputStream.read(data);
			
			checksum = FileTools.createSHA1checksum(directory + name);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(inputStream != null) {
				inputStream.close();
			}
		}
	}
	
	public void write(OutputStream outputStream){
		try {
			outputStream.write(data);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
