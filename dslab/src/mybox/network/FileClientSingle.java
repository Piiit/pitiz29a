package mybox.network;

import mybox.query.MyBoxQueryTools;
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
	
	public static void uploadAsync(String filename, String clientId, String directory, String hostname, int port) throws Exception {
		MyBoxQueryTools.lockFile(filename, clientId);
		FileClientSingle fileClient = new FileClientSingle(clientId, directory, filename, hostname, port);
		fileClient.setType(true);
		fileClient.start();
		fileClient.join();
		MyBoxQueryTools.unlockFile(filename, clientId);
	}
	
	public static void downloadAsync(String filename, String clientId, String directory, String hostname, int port) throws Exception {
		MyBoxQueryTools.lockFile(filename, clientId);
		FileClientSingle fileClient = new FileClientSingle(clientId, directory, filename, hostname, port);
		fileClient.setType(false);
		fileClient.start();
		fileClient.join();
		MyBoxQueryTools.unlockFile(filename, clientId);
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
