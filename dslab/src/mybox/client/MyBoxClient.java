package mybox.client;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import mybox.io.RMIFileTools;
import mybox.log.Log;
import mybox.server.ServerInterface;

/**
 * Scans a directory asynchronously and notifies if something has changed.
 * Creates a log that shows when something has been changed.
 * States can be: DELETED, MODIFIED, MOVED
 * Deleted files should be saved somewhere to enable a restore.
 * Log entries: TIME, STATE, FILE
 * Commands: COMMIT CHECKOUT
 * 
 * First iteration:
 * Scans two directories and synchronizes them. 
 * Copies every file that is not in conflict.
 * Conflicts are handled by renaming files and
 * writing log entries.
 * 
 * Second iteration:
 * Synch over network
 * 
 * Third iteration:
 * 
 * 
 * @author pemoser
 *
 */


public class MyBoxClient {
	
	private static final String DEFAULT_CLIENT_DIR = "/Users/user/mybox/";
	private static final int ERR_CLIENTDIR = 0x01;
	private static final int ERR_UPLOAD = 0x02;
	private static final int ERR_HANDSHAKE = 0x04;
	
	public static void main(String[] args) throws NotBoundException {	
		
		Log.setEnvVariableForDebug("MYBOX_CLIENT_DEBUG");
		
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
	   		rmiFT.uploadFolder();
	   	} catch (Exception e) {
	   		Log.error(e.getMessage());
			e.printStackTrace();
	   		System.exit(ERR_UPLOAD);
	   	}
	   	
	   	Log.info("All operations successfull! Exiting...");
	   	System.exit(0);
	}

}
