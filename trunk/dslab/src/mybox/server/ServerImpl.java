package mybox.server;

import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.rmi.*;
import java.rmi.server.*;
import java.security.NoSuchAlgorithmException;
import mybox.io.FileChunk;
import mybox.io.FileTools;
import mybox.log.Log;

public class ServerImpl extends UnicastRemoteObject implements ServerInterface {
	private static final long serialVersionUID = 1L;
	private static final String SERVER_DIR = "/Users/user/mybox_server/";
	
	protected ServerImpl() throws RemoteException {
		super();
	}

	@Override
	public void register(String name) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void status() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receiveFile(FileChunk chunk) throws RemoteException {
		
		File f = new File(chunk.getName());
		String path = f.getParent();
		
		try {
			
			// Create folders, if they don't exist 
			if(path != null) {
				File serverDir = new File(SERVER_DIR + "/" + path + "/");
				if(!serverDir.exists()) {
					Log.info("Creating directories: " + path);
					serverDir.mkdirs();
				}
			}

			// Write files, if they are new or have been changed since last synchronization run...
			boolean fileExists = (new File(SERVER_DIR + chunk.getName())).exists();
			String localFileChecksum = null;
			
			if(fileExists) {
				localFileChecksum = FileTools.createSHA1checksum(SERVER_DIR + chunk.getName()); 
				if(localFileChecksum.equalsIgnoreCase(chunk.getChecksum())) {
					Log.info("Skipping file " + chunk.getName() + ". Not modified since last update!");
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
}
