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
	
	public static void main(String[] args) throws NotBoundException {	
		
		Log.setEnvVariableForDebug("MYBOX_CLIENT_DEBUG");
		
		Log.info(Log.getLevelInfo());
		Log.info("Welcome to myBox! Your MyBox directory is " + DEFAULT_CLIENT_DIR);
		
		try {
			DatabaseConnection.setup("jdbc:postgresql://localhost/openreg?user=user&password=qwertz");
		} catch (Exception e) {
			Log.error("Unable to connect to specified database! " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
	   	
		ClientFileIndexer fileIndexer = new ClientFileIndexer("pemoser", DEFAULT_CLIENT_DIR);
		fileIndexer.start();
		
		DeletionDetector deletionDetector = new DeletionDetector("pemoser", DEFAULT_CLIENT_DIR);
		deletionDetector.start();
		
		FileClient fileClient = new FileClient("pemoser", DEFAULT_CLIENT_DIR, DEFAULT_SERVER, DEFAULT_PORT);
//		fileClient.start();
		
		FileServer fileServer = new FileServer(DEFAULT_CLIENT_DIR, 13268);
//		fileServer.start();
		
		DeletedFileRemover deletedFileRemover = new DeletedFileRemover("pemoser", DEFAULT_CLIENT_DIR);
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
