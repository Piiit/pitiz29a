package mybox.io;

import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;

import piwotools.database.DatabaseTools;
import piwotools.database.Row;
import piwotools.io.FileTools;
import piwotools.log.Log;
import mybox.server.ServerInterface;

public class RMIFileTools {
	
	private ServerInterface server;
	private String clientDir;
	private String clientId;
	
	public RMIFileTools(ServerInterface server, String clientDir, String clientId) throws Exception {
		super();
		this.server = server;
		setClientFolder(clientDir);
		this.clientId = clientId;
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
				"SELECT * FROM mybox_client_files WHERE filename=? AND client=?",
				filename,
				"MYBOX_SERVER"
				);
		
		Row fileEntry = fileEntries.size() == 0 ? null : fileEntries.get(0);
		
		if(fileEntry != null) {
			long dbFilesize = fileEntry.getValueAsLong("size");
//			Log.debug("SIZE: " + file.length() + "; " + dbFilesize);
			
			if(dbFilesize == file.length()) {
				String checksum = FileTools.createSHA1checksum(getClientDir() + filename);
//				Log.debug("SUM : " + checksum + "; " + fileEntry.getValueAsString("checksum"));
				if (checksum.equalsIgnoreCase(fileEntry.getValueAsString("checksum"))) {
//					Log.debug("Skipping file '" + filename + "'.");
					return;
				}
			}
		} 

		Log.info("Client " + clientId + ": Uploading file '" + filename + "'.");
	   	FileChunk chunk = new FileChunk(filename);
   		chunk.read(getClientDir());
   		server.receiveFile(chunk);
	}
	
	private void uploadFolderRecursive(final File folder) throws Exception {
    	String myBoxDir = folder.getCanonicalPath().substring(getClientDir().length()-1);
    	if(myBoxDir.length() > 0) {
    		myBoxDir = myBoxDir.substring(1) + "/";
    	}

    	for (final File fileEntry : folder.listFiles()) {
	    	if (fileEntry.isDirectory()) {
	        	createDirectoryIfNotExists(myBoxDir + fileEntry.getName());
	            uploadFolderRecursive(fileEntry);
	        } else {
	            Log.debug("Checking " + myBoxDir + fileEntry.getName());
	            
	            //Skipping hidden files...
	            if(! (new File(myBoxDir + fileEntry.getName())).isHidden()) {
	            	upload(myBoxDir + fileEntry.getName());
	            }
	        }
	    }
	}
	
	private void createDirectoryIfNotExists(String myBoxDir) throws RemoteException {
		Log.debug("Sending request to server: Create directory " + myBoxDir);
//		FileChunk chunk = new FileChunk(myBoxDir);
		server.createFolder(myBoxDir);
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
