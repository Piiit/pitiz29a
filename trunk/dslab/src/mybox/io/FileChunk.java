package mybox.io;

import java.io.*;

public class FileChunk implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private String directory;
	private byte[] data;

	public FileChunk(String clientDir, String filename) {
		this.name = filename;
		directory = clientDir;
	}

	public String getName() {
		return name;
	}
	
	public String getDir() {
		return directory;
	}
	
	public void read() throws IOException {
		FileInputStream inputStream = null;
		try {
			System.out.println("Reading file " + directory + name);
			File file = new File(directory + name);
			data = new byte[(int) file.length()];
			inputStream = new FileInputStream(file);
			inputStream.read(data);
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
