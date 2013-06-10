package mybox.network;

import java.net.ConnectException;

import mybox.query.MyBoxQueryTools;
import piwotools.database.Row;
import piwotools.log.Log;

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
		FileClientSingle fileClient = new FileClientSingle(clientId, directory, filename, hostname, port);
		fileClient.setType(true);
		fileClient.start();
		fileClient.join();
	}
	
	public static void downloadAsync(String filename, String clientId, String directory, String hostname, int port) throws Exception {
		FileClientSingle fileClient = new FileClientSingle(clientId, directory, filename, hostname, port);
		fileClient.setType(false);
		fileClient.start();
		fileClient.join();
	}
	
	public void run() {
		boolean success = false;
		try {

			if (isUpload) {
				
				Row fileEntry = MyBoxQueryTools.getFileInfo(clientId, filename);
				MyBoxQueryTools.lockFile(filename, clientId);
				try {
					NetworkTools.uploadFile(directory + filename, hostname, port, filename);
					success = true;
				} catch (ConnectException e) {
					Log.info("Server not reachable... try later!");
				}
				MyBoxQueryTools.unlockFile(filename, clientId);
				if(success) {
					MyBoxQueryTools.updateServerEntryAndSyncVersion(fileEntry, MyBoxQueryTools.getServerFileInfo(filename));
				}

			} else {
				
				MyBoxQueryTools.lockFile(filename, clientId);
				try {
					NetworkTools.downloadFile(directory + filename, hostname, port, filename, directory);
					success = true;
				} catch (ConnectException e) {
					Log.info("Server not reachable... try later!");
				}
				MyBoxQueryTools.unlockFile(filename, clientId);
				if(success) {
					MyBoxQueryTools.updateServerEntryAndSyncVersion(MyBoxQueryTools.getFileInfo(clientId, filename), MyBoxQueryTools.getServerFileInfo(filename));
				}
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
