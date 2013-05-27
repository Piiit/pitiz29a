package mybox.client;

import java.rmi.Naming;
import java.rmi.NotBoundException;

import piwotools.database.DatabaseConnection;
import piwotools.log.Log;
import mybox.io.RMIFileTools;
import mybox.server.ServerInterface;

//TODO Delete files from server when deleted from client
//TODO Ignore hidden files
//TODO Create empty folders
//TODO Lock files until they are uploaded/downloaded
//TODO Download files from server
//TODO Register client with ID and password
//TODO Login with ID and password

public class MyBoxClient {
	
	private static final int CLIENT_INDEXER_WAIT = 10000;
	public static final String DEFAULT_CLIENT_DIR = "/Users/user/mybox/";
	private static final int ERR_CLIENTDIR = 0x01;
	private static final int ERR_UPLOAD = 0x02;
	private static final int ERR_HANDSHAKE = 0x04;
	private static final int ERR_DATABASE = 0x08;
	
	public static void main(String[] args) throws NotBoundException {	
		
		Log.setEnvVariableForDebug("MYBOX_CLIENT_DEBUG2");
		
		ServerInterface server = null;
		
		String serverName = "localhost";
		String command = "sync";
		String filename = ".";
		
		Log.info("Welcome to myBox!");
		
		try {
		   	server = (ServerInterface)Naming.lookup("//" + serverName + "/MyBoxService");
			Log.info("Service lookup at '" + serverName + "' done.");
			Log.info("Handshaking...");
			server.sayHello();
		} catch (Exception e) {
			Log.error("Server offline? " + e.getMessage());
			e.printStackTrace();
			System.exit(ERR_HANDSHAKE);
		} 
		
		try {
			DatabaseConnection.setup("jdbc:postgresql://localhost/openreg?user=user&password=qwertz");
		} catch (Exception e) {
			Log.error("Unable to connect to specified database! " + e.getMessage());
			e.printStackTrace();
			System.exit(ERR_DATABASE);
		}
		
	   	Log.debug("Command: " + command + " " + filename);
	   	
	   	RMIFileTools rmiFT = new RMIFileTools(server);

	   	try {
	   		rmiFT.setClientFolder(DEFAULT_CLIENT_DIR);
	   	} catch (Exception e) {
	   		Log.error(e.getMessage());
			e.printStackTrace();
	   		System.exit(ERR_CLIENTDIR);
	   	}
	   	
	   	try {
	   		while(true) {
	   			Log.debug("Updating indexer...");
	   			rmiFT.uploadFolder();
	   			Log.debug("Update done. Next run in " + CLIENT_INDEXER_WAIT/1000 + " seconds.");
	   			Thread.sleep(CLIENT_INDEXER_WAIT);
	   		}
	   	} catch (Exception e) {
	   		Log.error(e.getMessage());
			e.printStackTrace();
	   		System.exit(ERR_UPLOAD);
	   	}
	   	
	   	try {
			DatabaseConnection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	   	
	   	Log.info("All operations successfull! Exiting...");
	   	System.exit(0);
	}

}
