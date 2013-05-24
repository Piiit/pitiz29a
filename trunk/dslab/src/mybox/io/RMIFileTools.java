package mybox.io;

import java.io.File;
import java.util.ArrayList;

import mybox.database.DatabaseTools;
import mybox.database.Row;
import mybox.log.Log;
import mybox.server.ServerInterface;

public class RMIFileTools {
	
	private ServerInterface server;
	private String clientDir;
	
	public RMIFileTools(ServerInterface server) {
		super();
		this.server = server;
	}
	
	public void setClientFolder(String clientDir) throws Exception {
		File folder = new File(clientDir);
	   	if(!folder.isDirectory()) {
	   		if(folder.mkdirs()) {
	   			Log.info("Client: New home directory created at " + clientDir);
	   		} else {
	   			throw new Exception("Client: Not able to create new home directory at '" + clientDir);
	   		}
	   	}
		this.clientDir = clientDir;
	}
	
	public String getClientDir() throws Exception {
		if(clientDir == null) {
			throw new Exception("Client: Client directory not set!");
		}
		return clientDir;
	}
	
	public void upload(final String filename) throws Exception {
	   	File file = new File(getClientDir() + filename);
	   	if(!file.exists()) {
	   		throw new Exception("Client: File '" + getClientDir() + filename + "' doesn't exist!");
	   	}
	   	
	   	ArrayList<Row> fileEntries = DatabaseTools.getQueryResult(
				"SELECT * FROM mybox_files WHERE filename = ?",
				filename
				);
		
		Row fileEntry = fileEntries.size() == 0 ? null : fileEntries.get(0);
		
		if(fileEntry != null) {
			long dbFilesize = fileEntry.getValueAsLong("size");
			String checksum = FileTools.createSHA1checksum(getClientDir() + filename);
			
			Log.debug("SUM : " + checksum + "; " + fileEntry.getValueAsString("checksum"));
			Log.debug("SIZE: " + file.length() + "; " + dbFilesize);
			
			if(dbFilesize == file.length() && checksum.equalsIgnoreCase(fileEntry.getValueAsString("checksum"))) {
				Log.info("Skipping file '" + filename + "'.");
				return;
			}
		} 

		Log.info("Uploading file '" + filename + "'.");
	   	FileChunk chunk = new FileChunk(filename);
   		chunk.read(getClientDir());
   		server.receiveFile(chunk);
		
	}
	
	private void uploadFolderRecursive(final File folder) throws Exception {
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            uploadFolderRecursive(fileEntry);
	        } else {
	        	String myBoxDir = folder.getCanonicalPath().substring(getClientDir().length()-1);
	        	if(myBoxDir.length() > 0) {
	        		myBoxDir += "/";
	        	}
	        	
	            Log.debug("Checking " + myBoxDir + fileEntry.getName());
	            upload(myBoxDir + fileEntry.getName());
	        }
	    }
	}
	
	public void uploadFolder(final String foldername) throws Exception {
		File folder = new File(getClientDir() + foldername);
	   	if(!folder.isDirectory()) {
	   		throw new Exception("Client: Folder '" + getClientDir() + foldername + "' doesn't exist!");
	   	}
	   	
	   	uploadFolderRecursive(folder);
	}
	
	public void uploadFolder() throws Exception {
		uploadFolder("");
	}
	

}
