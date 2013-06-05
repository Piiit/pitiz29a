package mybox.client;

import java.io.File;
import piwotools.database.DatabaseConnection;
import piwotools.database.DatabaseTools;
import piwotools.log.Log;
import mybox.io.DeletedFileRemover;
import mybox.io.DeletionDetector;

public class MyBoxClient {
	
	public static final String DEFAULT_CLIENT_DIR = "/Users/user/mybox/";
	public static final String DEFAULT_SERVER = "localhost";
	public static final int DEFAULT_PORT = 13267;
	
	private static String clientDir = DEFAULT_CLIENT_DIR;
	private static String clientId = null;
	
	public static void main(String[] args) throws Exception {	
		
		Log.setEnvVariableForDebug("MYBOX_CLIENT_DEBUG");

		if(args.length != 2) {
			Log.error("Parameter missing! Usage: MyBoxClient clientID myBoxHomeDirectory");
			System.exit(1);
		}

		clientId = args[0];
		clientDir = args[1];
		
		clientDir = (new File(clientDir)).getCanonicalPath() + "/";

		Log.info(Log.getLevelInfo());
		Log.info("Welcome to myBox, " + clientId + "! Your MyBox directory is " + clientDir);
		
		try {
			DatabaseConnection.setup("jdbc:postgresql://localhost/openreg?user=user&password=qwertz");
		} catch (Exception e) {
			Log.error("Unable to connect to specified database! " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}

		//Unlock all files...
		DatabaseTools.executeUpdate(
				"UPDATE mybox_client_files SET locked=? WHERE client=?",
				false,
				clientId
				);
	   	
		ClientFileIndexer fileIndexer = new ClientFileIndexer(clientId, clientDir, DEFAULT_SERVER, DEFAULT_PORT);
		fileIndexer.start();
		
		DeletionDetector deletionDetector = new DeletionDetector(clientId, clientDir);
		deletionDetector.start();
		
		FileDownloader fileDownloader = new FileDownloader(clientId, clientDir, DEFAULT_SERVER, DEFAULT_PORT);
		fileDownloader.start();
		
		DeletedFileRemover deletedFileRemover = new DeletedFileRemover(clientId, clientDir);
		deletedFileRemover.start();
		
		try {
			fileIndexer.join();
			Thread.sleep(10000);
			fileDownloader.join();
			deletionDetector.join();
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
