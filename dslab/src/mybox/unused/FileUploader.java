package mybox.unused;

import java.rmi.Naming;
import piwotools.log.Log;

public class FileUploader extends Thread {
	
	private String directory;
	private String clientId;

	public FileUploader(String clientId, String directory) {
		super();
		this.directory = directory;
		this.clientId = clientId;
	}

	public void run() {
		ServerInterface server = null;
		String serverName = "localhost";
		
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
		
		RMIFileTools rmiFT = null;

	   	try {
		   	rmiFT = new RMIFileTools(server, directory, clientId);
	   	} catch (Exception e) {
	   		Log.error(e.getMessage());
			e.printStackTrace();
	   		System.exit(1);
	   	}
		
	   	try {
	   		while(true) {
	   			Log.debug("Updating indexer...");
	   			
	   			// TODO do not scan the entire directory, get files to upload from database... 
	   			//      let clientfileindexer do the job!
	   			rmiFT.uploadFolder();
	   			Log.debug("Update done. Next run in " + 10 + " seconds.");
	   			Thread.sleep(10000);
	   		}
	   	} catch (Exception e) {
	   		Log.error(e.getMessage());
			e.printStackTrace();
	   		System.exit(1);
	   	}

	}
}
