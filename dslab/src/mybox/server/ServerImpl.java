package mybox.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.*;
import java.rmi.server.*;
import java.util.HashMap;
import java.util.Map;

import mybox.io.FileChunk;
import mybox.log.Log;

public class ServerImpl extends UnicastRemoteObject implements ServerInterface {
	private static final long serialVersionUID = 1L;
	private static final String serverDir = "/Users/user/mybox_server/";
	
	private Map<String, String> users;
	private Map<String, String> logins;
	
	protected ServerImpl() throws RemoteException {
		super();
		users = new HashMap<String, String>();
		logins = new HashMap<String, String>();
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
			if(path != null) {
				Log.info("Creating directories: " + serverDir + path);
				(new File(serverDir + "/" + path + "/")).mkdirs();
			}
			Log.info("Writing file " + serverDir + chunk.getName());
	       	chunk.write(new FileOutputStream(serverDir + chunk.getName()));
	    } catch(IOException e) {
	    	e.printStackTrace();
	    }		
	}
}
