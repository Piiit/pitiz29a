package mybox.client;

import java.rmi.NotBoundException;
import piwotools.database.DatabaseConnection;
import piwotools.log.Log;
import mybox.io.DeletedFileRemover;
import mybox.io.DeletionDetector;
import mybox.network.FileClient;
import mybox.network.FileServer;

public class MyBoxClient {
	
	public static final String DEFAULT_CLIENT_DIR = "/Users/user/mybox/";
	public static final String DEFAULT_SERVER = "localhost";
	public static final int DEFAULT_PORT = 13267;
	
	private static String clientDir = DEFAULT_CLIENT_DIR;
	private static String clientId = null;
	
	public static void main(String[] args) throws NotBoundException {	
		
		Log.setEnvVariableForDebug("MYBOX_CLIENT_DEBUG");

		if(args.length != 2) {
			Log.error("Parameter missing! Usage: MyBoxClient clientID myBoxHomeDirectory");
			System.exit(1);
		}

		clientId = args[0];
		clientDir = args[1];

		Log.info(Log.getLevelInfo());
		Log.info("Welcome to myBox, " + clientId + "! Your MyBox directory is " + clientDir);
		
		try {
			DatabaseConnection.setup("jdbc:postgresql://localhost/openreg?user=user&password=qwertz");
		} catch (Exception e) {
			Log.error("Unable to connect to specified database! " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
	   	
		ClientFileIndexer fileIndexer = new ClientFileIndexer(clientId, clientDir);
		fileIndexer.start();
		
		DeletionDetector deletionDetector = new DeletionDetector(clientId, clientDir);
		deletionDetector.start();
		
		FileClient fileClient = new FileClient(clientId, clientDir, DEFAULT_SERVER, DEFAULT_PORT);
//		fileClient.start();
		
		FileServer fileServer = new FileServer(clientDir, 13268);
//		fileServer.start();
		
		DeletedFileRemover deletedFileRemover = new DeletedFileRemover(clientId, clientDir);
		deletedFileRemover.start();
		
		try {
			fileIndexer.join();
			Thread.sleep(10000);
			deletionDetector.join();
			fileClient.join();
			fileServer.join();
			deletedFileRemover.join();
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
