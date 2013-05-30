package mybox.client;

import java.rmi.NotBoundException;
import piwotools.database.DatabaseConnection;
import piwotools.log.Log;
import mybox.io.DeletedFileRemover;
import mybox.io.DeletionDetector;

public class MyBoxClient {
	
	public static final String DEFAULT_CLIENT_DIR = "/Users/user/mybox/";
	
	public static void main(String[] args) throws NotBoundException {	
		
		Log.setEnvVariableForDebug("MYBOX_CLIENT_DEBUG");
		
		Log.info("Welcome to myBox!");
		
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
		
		FileClient fileClient = new FileClient("pemoser", DEFAULT_CLIENT_DIR);
		fileClient.start();
		
		DeletedFileRemover deletedFileRemover = new DeletedFileRemover("pemoser", DEFAULT_CLIENT_DIR);
		deletedFileRemover.start();
		
		try {
			fileIndexer.join();
			deletionDetector.join();
			fileClient.join();
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
