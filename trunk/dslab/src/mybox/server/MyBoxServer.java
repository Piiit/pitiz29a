package mybox.server;

import java.rmi.*;

import mybox.log.Log;

public class MyBoxServer { 
	
	public MyBoxServer() throws Exception {
		ServerInterface c = new ServerImpl();
		Naming.rebind("rmi://localhost/MyBoxService", c);
	}

	public static void main(String args[]) {
		
		Log.setEnvVariableForDebug("MYBOX_SERVER_DEBUG");
		
		Log.info("Starting file indexer...");
		FileIndexer indexer = new FileIndexer();
		indexer.start();
		Log.info("File indexer running!");
		
		try {
			new MyBoxServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
}
