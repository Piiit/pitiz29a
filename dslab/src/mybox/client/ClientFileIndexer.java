package mybox.client;

import java.io.File;
import java.sql.Timestamp;
import piwotools.database.Row;
import piwotools.io.FileTools;
import piwotools.log.Log;
import mybox.io.FileIndexer;
import mybox.network.FileClientSingle;
import mybox.query.MyBoxQueryTools;

public class ClientFileIndexer extends FileIndexer {
	
	private String clientId;
	private String myboxFilename;
	private String filename;
	private String server;
	private int port;
	private Row clientFileInfo = null;
	private Row serverFileInfo = null;
	

	
	public ClientFileIndexer(String clientId, String directory, String server, int port) {
		super();
		this.clientId = clientId;
		setDirectory(directory);
		this.server = server;
		this.port = port;
	}
	
	private void handleChanges() {

		//Skip hidden files...
		if(getFile().isHidden()) {
			return;
		}
		
//		System.out.println(getDirectory());

		//File found on local file system...
		try {
			clientFileInfo = MyBoxQueryTools.getFileInfo(clientId, myboxFilename);
			serverFileInfo = MyBoxQueryTools.getServerFileInfo(myboxFilename);
			
			java.sql.Timestamp fileTimestamp = new java.sql.Timestamp(getFile().lastModified());

			//New file found, no client file info present...
			if(clientFileInfo == null) {
				
				//no server file info present > insert new client file info...
				if(serverFileInfo == null) {
					onNewFile();
					return;
				}
				
				if(serverFileInfo.getValueAsBoolean("locked")) {
					onNewFileLockedOnServer();
					return;
				}
				
				//server file info exists, but server file has been deleted before...
				if(serverFileInfo.getValueAsBoolean("deleted")) {
					onNewFilePreviouslyDeleted();
					return;
				} 

				//server file info exists, it is a directory OR it must be different from newly found local file... 
				onNewFileOnServerExists();
				return;
			}					
				
			//File changed, client file info present...
			long dbFilesize = isFile() ? clientFileInfo.getValueAsLong("size") : 0;
			
			if((isFile() && dbFilesize != getFile().length()) || clientFileInfo.getValueAsDate("modified").before(fileTimestamp) || serverFileInfo == null) {

//				System.out.println(1);
				String checksum = isFile() ? FileTools.createSHA1checksum(filename) : null;

				long clientVersion = clientFileInfo.getValueAsLong("version");
				if((isFile()  && !checksum.equalsIgnoreCase(clientFileInfo.getValueAsStringNotNull("checksum"))) || clientFileInfo.getValueAsBoolean("deleted")) {
					clientVersion++;
				}
				
				Log.info(getTypeString() + " '" + myboxFilename + "' has been changed.");
				
				//File changed locally, but server hasn't any file info...
				if(serverFileInfo == null) {
					onFileNotOnServer();
					return;
				}
				
				// If server file and client file have been deleted before 
				// and server_version == sync_version... perform a restore!
				long serverVersion = serverFileInfo.getValueAsLong("version");
				long syncVersion = clientFileInfo.getValueAsLong("sync_version");
				if(serverVersion == syncVersion || (checksum.equalsIgnoreCase(serverFileInfo.getValueAsString("checksum")) && !clientFileInfo.getValueAsBoolean("deleted"))) {
					onFileUpdateServer();
					return;
				}
				
				// Local files have not been changed since last sync
				// Server version is newer, download new file...
				if(serverVersion > syncVersion && syncVersion == clientVersion) {
					onFileUpdateClient();
					return;
				}
				
				
				// Move local file, insert local file info with new name... do not insert the old filename, should be done by FileDownloader...
				conflictHandling();
				
				// If file has not been deleted on client side and the sync_version differs only by one, and now further 
				// changes have been made... delete the file.
				// >>> SHOULD BE DONE BY FILE REMOVER...
//				if(!clientFileInfo.getValueAsBoolean("deleted") && (serverVersion - 1) == syncVersion && version == syncVersion) {
//					
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	private void onFileUpdateClient() throws Exception {
		Log.info("Downloading " + getTypeString() + " " + filename);
		
		String checksum = isFile() ? FileTools.createSHA1checksum(filename) : null;
		long clientVersion = clientFileInfo.getValueAsLong("version");
		if((isFile()  && !checksum.equalsIgnoreCase(clientFileInfo.getValueAsStringNotNull("checksum"))) || clientFileInfo.getValueAsBoolean("deleted")) {
			clientVersion++;
		}

		java.sql.Timestamp fileTimestamp = new java.sql.Timestamp(getFile().lastModified());


		if(isFile()) {
			MyBoxQueryTools.updateFile(clientId, myboxFilename, checksum, getFile().length(), fileTimestamp, clientVersion, clientVersion);
			FileClientSingle.downloadAsync(myboxFilename, clientId, getDirectory(), server, port);
		} else {
			MyBoxQueryTools.updateDirectory(clientId, myboxFilename, fileTimestamp, clientVersion, clientVersion);
		}
		clientFileInfo = MyBoxQueryTools.getFileInfo(clientId, myboxFilename);
		MyBoxQueryTools.updateServerEntryAndSyncVersion(clientFileInfo, serverFileInfo);
	}

	private void onFileUpdateServer() throws Exception {
		
		String checksum = isFile() ? FileTools.createSHA1checksum(filename) : null;
		long clientVersion = clientFileInfo.getValueAsLong("version");
		if((isFile()  && !checksum.equalsIgnoreCase(clientFileInfo.getValueAsStringNotNull("checksum"))) || clientFileInfo.getValueAsBoolean("deleted")) {
			clientVersion++;
		}

		java.sql.Timestamp fileTimestamp = new java.sql.Timestamp(getFile().lastModified());


		Log.info("Uploading " + getTypeString() + " " + filename);
		if(isFile()) {
			MyBoxQueryTools.updateFile(clientId, myboxFilename, checksum, getFile().length(), fileTimestamp, clientVersion, clientVersion);
			FileClientSingle.uploadAsync(myboxFilename, clientId, getDirectory(), server, port);
		} else {
			MyBoxQueryTools.updateDirectory(clientId, myboxFilename, fileTimestamp, clientVersion, clientVersion);
		}
		clientFileInfo = MyBoxQueryTools.getFileInfo(clientId, myboxFilename);
		MyBoxQueryTools.updateServerEntryAndSyncVersion(clientFileInfo, serverFileInfo);
	}

	private void onFileNotOnServer() throws Exception {
		
		String checksum = isFile() ? FileTools.createSHA1checksum(filename) : null;
		long clientVersion = clientFileInfo.getValueAsLong("version");
		if((isFile()  && !checksum.equalsIgnoreCase(clientFileInfo.getValueAsStringNotNull("checksum"))) || clientFileInfo.getValueAsBoolean("deleted")) {
			clientVersion++;
		}
		java.sql.Timestamp fileTimestamp = new java.sql.Timestamp(getFile().lastModified());


		if(isFile()) {
			MyBoxQueryTools.updateFile(clientId, myboxFilename, checksum, getFile().length(), fileTimestamp, clientVersion, clientVersion);
			FileClientSingle.uploadAsync(myboxFilename, clientId, getDirectory(), server, port);
		} else {
			MyBoxQueryTools.updateDirectory(clientId, myboxFilename, fileTimestamp, clientVersion, clientVersion);
		}
		clientFileInfo = MyBoxQueryTools.getFileInfo(clientId, myboxFilename);
		MyBoxQueryTools.updateServerEntryAndSyncVersion(clientFileInfo, serverFileInfo);
	}

	private void onNewFileOnServerExists() throws Exception {
		
		if(isFile()) {
			conflictHandling();
		} else {
			Log.info("Directory found: " + myboxFilename);
			MyBoxQueryTools.insertDirectory(clientId, myboxFilename, (Timestamp)serverFileInfo.getValue("modified"), serverFileInfo.getValueAsLong("version"), serverFileInfo.getValueAsLong("version"));
		}
	}

	private void conflictHandling() throws Exception {
		
		//Try to restore file info if checksums are equal...
		String checksum = FileTools.createSHA1checksum(filename);
		if(checksum.equalsIgnoreCase(serverFileInfo.getValueAsString("checksum"))) {
			Log.info("Restoring fileinfo of " + myboxFilename + " successfull!");
			
			//TODO What if entry already exists?
			MyBoxQueryTools.insertFile(clientId, myboxFilename, checksum, getFile().length(), (Timestamp)serverFileInfo.getValue("modified"), serverFileInfo.getValueAsLong("version"), serverFileInfo.getValueAsLong("version"));
			return;
		}

		Timestamp fileTimestamp = FileTools.getFileModifiedAsSqlTimestamp(getFile());

		//...CONFLICT... Trying to move to a new file...
		Log.warn("New " + getTypeString() + " '" + myboxFilename + "' is out of sync. Different file exists on server with same name.");
		
		// Move local file, insert local file info with new name... do not insert the old filename, should be done by FileDownloader...
		String newFilenameExtension = "(OUT OF SYNC " + clientId + ")";
		String newFilename = filename + newFilenameExtension ;
		if(getFile().renameTo(new File(newFilename))) {
			Log.info("File in conflict renamed to " + newFilename);
			MyBoxQueryTools.insertFile(clientId, myboxFilename + newFilenameExtension, checksum, getFile().length(), fileTimestamp, 0, 0);
			FileClientSingle.uploadAsync(myboxFilename + newFilenameExtension, clientId, getDirectory(), server, port);
	    }
		
		Log.error("CONFLICT: Not possible to move " + getTypeString() + " " + filename + " to " + newFilename);
		throw new Exception("Not possible to move " + getTypeString() + " " + filename);

	}

	private void onNewFileLockedOnServer() {
		Log.info("New " + getTypeString() + " '" + myboxFilename + "' found. Locked on server! Skipping...");
	}

	private void onNewFilePreviouslyDeleted() throws Exception {
		Log.info("New " + getTypeString() + " '" + myboxFilename + "' found. Previously deleted on server!");

		long version = serverFileInfo.getValueAsLong("version") + 1;
		Timestamp fileTimestamp = FileTools.getFileModifiedAsSqlTimestamp(getFile());

		if(isFile()) {
			String checksum = FileTools.createSHA1checksum(filename);
			MyBoxQueryTools.insertFile(clientId, myboxFilename, checksum, getFile().length(), fileTimestamp, version, version);
			FileClientSingle.uploadAsync(myboxFilename, clientId, getDirectory(), server, port);
		} else {
			MyBoxQueryTools.insertDirectory(clientId, myboxFilename, fileTimestamp, version, version);
		}
		clientFileInfo = MyBoxQueryTools.getFileInfo(clientId, myboxFilename);
		MyBoxQueryTools.updateServerEntryAndSyncVersion(clientFileInfo, serverFileInfo);
		
	}

	private void onNewFile() throws Exception {
		Log.info("New " + getTypeString() + " '" + myboxFilename + "' found. Not present on server!");
		
		Timestamp fileTimestamp = FileTools.getFileModifiedAsSqlTimestamp(getFile());
		if(isFile()) {
			String checksum = FileTools.createSHA1checksum(filename);
			MyBoxQueryTools.insertFile(clientId, myboxFilename, checksum, getFile().length(), fileTimestamp, 0, 0);
			FileClientSingle.uploadAsync(myboxFilename, clientId, getDirectory(), server, port);
		} else {
			MyBoxQueryTools.insertDirectory(clientId, myboxFilename, fileTimestamp, 0, 0);
			MyBoxQueryTools.insertServerDirectory(myboxFilename, fileTimestamp, 0);
		}
	}

	@Override
	public void onDirectory(String dirname) {
		onFile(dirname);
	}

	@Override
	public void onFile(String filename) {
		this.filename = filename;
		myboxFilename = filename.substring(getDirectory().length());
		handleChanges();		
	}

}
