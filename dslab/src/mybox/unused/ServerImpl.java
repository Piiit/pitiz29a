package mybox.unused;

import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.rmi.*;
import java.rmi.server.*;
import java.security.NoSuchAlgorithmException;
import piwotools.io.FileTools;
import piwotools.log.Log;

public class ServerImpl extends UnicastRemoteObject implements ServerInterface {
	private static final long serialVersionUID = 1L;
	public static final String SERVER_DIR = "/Users/user/mybox_server/";
	
	protected ServerImpl() throws RemoteException {
		super();
	}

	@Override
	public void register(String name) throws RemoteException {
		
	}

	@Override
	public void status() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receiveFile(FileChunk chunk) throws RemoteException {

		String filename = SERVER_DIR + chunk.getName();
		File f = new File(filename);
		String path = f.getParent();
		
		try {
			
			// Create folders, if they don't exist 
			if(path != null) {
				File serverDir = new File(path + "/");
				if(!serverDir.exists()) {
					Log.info("Creating directories: " + path);
					serverDir.mkdirs();
				}
			}


			//Skipping hidden files (this is done outside the fileExists-block, because on some systems
			//hidden files are considered not existent...
			boolean isHiddenFile = (new File(filename)).isHidden();
			if(isHiddenFile) {
				Log.debug("Skipping hidden file " + chunk.getName() + ".");
				return;
			}
			
			// Write files, if they are new or have been changed since last synchronization run...
			boolean fileExists = (new File(filename)).exists();
			String localFileChecksum = null;
			if(fileExists) {
				localFileChecksum = FileTools.createSHA1checksum(SERVER_DIR + chunk.getName()); 
				if(localFileChecksum.equalsIgnoreCase(chunk.getChecksum())) {
					Log.debug("Skipping file " + chunk.getName() + ". Not modified since last update!");
					return;
				} 
			} 
			
			Log.info("Writing file " + chunk.getName());
	       	chunk.write(new FileOutputStream(SERVER_DIR + chunk.getName()));
	       	
	    } catch(IOException | NoSuchAlgorithmException e) {
	    	e.printStackTrace();
	    }		
	}

	@Override
	public void sayHello() throws RemoteException, ServerNotActiveException {
		Log.info("Handshaking with client " + RemoteServer.getClientHost());
	}

	@Override
	public void createFolder(String myBoxDir) throws RemoteException {
		File folder = new File(SERVER_DIR + myBoxDir);
		
		Log.debug("Request received: Create folder (if it doesn't exist) " + myBoxDir);
		if(!folder.isDirectory()) {
			folder.mkdirs();
			Log.info("Created new directory " + myBoxDir);
		}
		
	}
}
