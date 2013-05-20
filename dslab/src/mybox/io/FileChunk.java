package mybox.io;

import java.io.*;

public class FileChunk implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private String clientDir;
	private int id;
	private byte[] data;

	public FileChunk(String clientDir, String name, int id) {
		this.name = name;
		this.id = id;
		this.clientDir = clientDir;
	}

	public String getName() {
		return name;
	}
	
	public String getClientDir() {
		return clientDir;
	}
	
	public int getId() {
		return id;
	}

	public void read() throws IOException {
		FileInputStream inputStream = null;
		try {
			System.out.println("Reading file " + clientDir + name);
			File file = new File(clientDir + name);
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
