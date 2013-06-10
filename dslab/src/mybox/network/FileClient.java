package mybox.network;

import java.util.ArrayList;

import mybox.io.DelayedInfiniteThread;
import mybox.query.MyBoxQueryTools;
import piwotools.database.Row;
import piwotools.log.Log;

public class FileClient extends DelayedInfiniteThread {
	
	private String directory;
	private String clientId;
	private String hostname;
	private int port;
	
	public FileClient(String clientId, String directory, String hostname, int port) {
		super();
		this.clientId = clientId;
		this.directory = directory;
		this.hostname = hostname;
		this.port = port;
	}

	@Override
	public void beforeRun() throws Exception {
	}

	@Override
	public void duringRun() throws Exception {
		try {
			
			ArrayList<Row> fileEntries = MyBoxQueryTools.getUploadableFiles(clientId);
			
			for(Row fileEntry : fileEntries) {
				String filename = fileEntry.getValueAsString("filename");
				
				MyBoxQueryTools.lockFile(filename, clientId);
				
				NetworkTools.uploadFile(directory + filename, hostname, port, filename);
			
				MyBoxQueryTools.unlockFile(filename, clientId);
				MyBoxQueryTools.updateServerEntryAndSyncVersion(fileEntry, MyBoxQueryTools.getServerFileInfo(filename));
			}
			
			Log.debug("FileClient: Ready, next run in 10 seconds.");
			Thread.sleep(getDelay());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				MyBoxQueryTools.unlockAllFiles(clientId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void afterRun() throws Exception {
	}
}