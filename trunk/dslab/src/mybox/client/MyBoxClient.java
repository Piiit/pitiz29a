package mybox.client;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import piwotools.database.DatabaseConnection;
import piwotools.log.Log;
import mybox.server.ServerInterface;

//TODO Delete files from server when deleted from client
//TODO Lock files until they are uploaded/downloaded
//TODO Download files from server
//TODO Register client with ID and password
//TODO Login with ID and password

public class MyBoxClient {
	
	public static final String DEFAULT_CLIENT_DIR = "/Users/user/mybox/";
	
	public static void main(String[] args) throws NotBoundException {	
		
		Log.setEnvVariableForDebug("MYBOX_CLIENT_DEBUG2");
		
		ServerInterface server = null;
		
		String serverName = "localhost";
		
		Log.info("Welcome to myBox!");
		
		try {
		   	server = (ServerInterface)Naming.lookup("//" + serverName + "/MyBoxService");
			Log.info("Service lookup at '" + serverName + "' done.");
			Log.info("Handshaking...");
			server.sayHello();
		} catch (Exception e) {
			Log.error("Server offline? " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		} 
		
		try {
			DatabaseConnection.setup("jdbc:postgresql://localhost/openreg?user=user&password=qwertz");
		} catch (Exception e) {
			Log.error("Unable to connect to specified database! " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
//	   	
//	   	RMIFileTools rmiFT = new RMIFileTools(server);
//
//	   	try {
//	   		rmiFT.setClientFolder(DEFAULT_CLIENT_DIR);
//	   	} catch (Exception e) {
//	   		Log.error(e.getMessage());
//			e.printStackTrace();
//	   		System.exit(ERR_CLIENTDIR);
//	   	}
//	   	
//	   	try {
//			server.register("pemoser");
//		} catch (RemoteException e) {
//	   		Log.error(e.getMessage());
//			e.printStackTrace();
//	   		System.exit(ERR_REGISTRATION);
//		}
	   	
//	   	try {
//	   		while(true) {
//	   			Log.debug("Updating indexer...");
////	   			rmiFT.uploadFolder();
//	   			Log.debug("Update done. Next run in " + CLIENT_INDEXER_WAIT/1000 + " seconds.");
//	   			Thread.sleep(CLIENT_INDEXER_WAIT);
//	   		}
//	   	} catch (Exception e) {
//	   		Log.error(e.getMessage());
//			e.printStackTrace();
//	   		System.exit(ERR_UPLOAD);
//	   	}
	   	
		
		ClientFileIndexer fileIndexer = new ClientFileIndexer("pemoser", DEFAULT_CLIENT_DIR);
		fileIndexer.start();
		
		DeletionDetector deletionDetector = new DeletionDetector("pemoser", DEFAULT_CLIENT_DIR);
		deletionDetector.start();
		
		try {
			fileIndexer.join();
			deletionDetector.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
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
