package mybox.io;

import java.io.File;
import mybox.server.ServerInterface;

public class RMIFileTools {
	
	private ServerInterface server;
	private String clientDir;
	
	public RMIFileTools(ServerInterface server) {
		super();
		this.server = server;
	}
	
	public void setClientFolder(String clientDir) throws Exception {
		File file = new File(clientDir);
	   	if(!file.exists()) {
	   		throw new Exception("Client: Folder '" + clientDir + "' doesn't exist!");
	   	}
		this.clientDir = clientDir;
	}
	
	public String getClientDir() throws Exception {
		if(clientDir == null) {
			throw new Exception("Client: Client directory not set!");
		}
		return clientDir;
	}
	
	public void upload(String filename) throws Exception {
	   	File file = new File(getClientDir() + filename);
	   	if(!file.exists()) {
	   		throw new Exception("Client: File '" + getClientDir() + filename + "' doesn't exist!");
	   	}
	   	
	   	FileChunk chunk = new FileChunk(filename);
   		chunk.read(getClientDir());
   		server.receiveFile(chunk);
	}
}
