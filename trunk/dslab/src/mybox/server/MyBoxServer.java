package mybox.server;

import java.rmi.*;

import piwotools.database.DatabaseConnection;
import piwotools.log.Log;

public class MyBoxServer { 
	
	public static void main(String args[]) {
		
		Log.setEnvVariableForDebug("MYBOX_SERVER_DEBUG");
		
		try {
			DatabaseConnection.setup("jdbc:postgresql://localhost/openreg?user=user&password=qwertz");
		} catch (Exception e) {
			Log.error("Unable to connect to specified database! " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		
		try {
			ServerInterface c = new ServerImpl();
			Naming.rebind("rmi://localhost/MyBoxService", c);
			
			ServerFileIndexer fileIndexer = new ServerFileIndexer("MYBOX_SERVER", ServerImpl.SERVER_DIR);
			fileIndexer.start();
			
			DeletedFileRemover fileRemover = new DeletedFileRemover();
			fileRemover.start();
			
			fileRemover.join();
			fileIndexer.join();
			
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
