package mybox.network;

import mybox.query.MyBoxQueryTools;
import piwotools.database.DatabaseTools;
import piwotools.database.Row;

public class FileClientSingle extends Thread {
	
	private String directory;
	private String clientId;
	private String hostname;
	private int port;
	private String filename;
	private boolean isUpload;
	
	public FileClientSingle(String clientId, String directory, String filename, String hostname, int port) {
		super();
		this.clientId = clientId;
		this.directory = directory;
		this.hostname = hostname;
		this.port = port;
		this.filename = filename;
	}
	
	public void setType(boolean isUpload) {
		this.isUpload = isUpload;
	}
	
	public void run() {
		try {

			if (isUpload) {
				
				Row fileEntry = MyBoxQueryTools.getFileInfo(clientId, filename);
				MyBoxQueryTools.lockFile(filename, clientId);
				NetworkTools.uploadFile(directory + filename, hostname, port, filename);
				MyBoxQueryTools.unlockFile(filename, clientId);
				MyBoxQueryTools.updateServerEntryAndSyncVersion(fileEntry, MyBoxQueryTools.getServerFileInfo(filename));

			} else {
				
				MyBoxQueryTools.lockFile(filename, clientId);
				NetworkTools.downloadFile(directory + filename, hostname, port, filename, directory);
				MyBoxQueryTools.unlockFile(filename, clientId);
				MyBoxQueryTools.updateServerEntryAndSyncVersion(MyBoxQueryTools.getFileInfo(clientId, filename), MyBoxQueryTools.getServerFileInfo(filename));
			}
			
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

}
