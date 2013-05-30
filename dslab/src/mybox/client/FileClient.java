package mybox.client;

import java.util.ArrayList;
import mybox.io.NetworkTools;
import mybox.query.MyBoxQueryTools;
import piwotools.database.Row;
import piwotools.log.Log;

public class FileClient extends Thread {
	
	private String directory;
	private String clientId;
	private String hostname = "localhost";
	private int port = 13267;
	
	public FileClient(String clientId, String directory) {
		super();
		this.clientId = clientId;
		this.directory = directory;
	}

	public void run() {

		while(true) {
			try {
				
				ArrayList<Row> fileEntries = MyBoxQueryTools.getUploadableFiles(clientId);
				
				for(Row fileEntry : fileEntries) {
					String filename = fileEntry.getValueAsString("filename");
					
					MyBoxQueryTools.lockFile(filename, clientId);
					
					NetworkTools.uploadFile(directory + filename, hostname, port, filename);
				
					MyBoxQueryTools.unlockFile(filename, clientId);
					MyBoxQueryTools.updateServerEntryAndSyncVersion(fileEntry, MyBoxQueryTools.getServerFileInfo(filename));
				}
				
				Log.debug("FileClient ready, next run in 10 seconds.");
				Thread.sleep(10000);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
	}
}