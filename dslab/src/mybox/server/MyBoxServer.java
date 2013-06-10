package mybox.server;

import mybox.io.DeletedFileRemover;
import mybox.io.DeletionDetector;
import mybox.network.FileClient;
import mybox.network.FileServer;
import mybox.unused.ServerImpl;
import piwotools.database.DatabaseConnection;
import piwotools.log.Log;

public class MyBoxServer { 
	
	public static void main(String args[]) {
		
		Log.setEnvVariableForDebug("MYBOX_SERVER_DEBUG2");
		
		Log.info(Log.getLevelInfo());
		
		try {
			DatabaseConnection.setup("jdbc:postgresql://localhost/openreg?user=user&password=qwertz");
		} catch (Exception e) {
			Log.error("Unable to connect to specified database! " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		
		try {

			//TODO Change & run it to check mismatches from clients...
			ServerFileIndexer fileIndexer = new ServerFileIndexer("MYBOX_SERVER", ServerImpl.SERVER_DIR);
//			fileIndexer.start();
			
			DeletedFileRemover fileRemover = new DeletedFileRemover("MYBOX_SERVER", ServerImpl.SERVER_DIR);
			fileRemover.start();
			
			DeletionDetector deletionDetector = new DeletionDetector("MYBOX_SERVER", ServerImpl.SERVER_DIR);
//			deletionDetector.start();
			
			FileServer fileServer = new FileServer(ServerImpl.SERVER_DIR, 13267);
			fileServer.start();
			
			FileClient fileClient = new FileClient("MYBOX_SERVER", ServerImpl.SERVER_DIR, "localhost", 13268);
//			fileClient.start();
			
			fileRemover.join();
			fileIndexer.join();
			fileServer.join();
			fileClient.join();
			deletionDetector.join();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			DatabaseConnection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	} 
}
