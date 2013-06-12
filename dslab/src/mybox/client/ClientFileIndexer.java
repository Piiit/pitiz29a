package mybox.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import piwotools.database.DatabaseTools;
import piwotools.database.Row;
import piwotools.io.FileTools;
import piwotools.log.Log;
import piwotools.thread.FileIndexer;
import mybox.network.FileClientSingle;
import mybox.query.MyBoxQueryTools;

public class ClientFileIndexer extends FileIndexer {
	
	private String clientId;
	private String myboxFilename;
	private String filename;
	private String server;
	private String checksum;
	private Timestamp fileTimestamp = null;
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
		
		//File found on local file system...
		try {
			clientFileInfo = MyBoxQueryTools.getFileInfo(clientId, myboxFilename);
			serverFileInfo = MyBoxQueryTools.getServerFileInfo(myboxFilename);
			fileTimestamp = new Timestamp(getFile().lastModified());

			checksum = null;
			try {
				checksum = isFile() && getFile().exists() ? FileTools.createSHA1checksum(filename) : null;
			} catch (FileNotFoundException e) {
				clientFileInfo = MyBoxQueryTools.getFileInfo(clientId, myboxFilename);
				if(clientFileInfo.getValueAsBoolean("deleted")) {
					Log.info("ClientFileIndexer: Previously selected file '" + myboxFilename + "' has been deleted. Skipping...");
					return;
				} 
				throw e;
			}
			
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

			// Client file has been deleted before... perform a restore!
			if(clientFileInfo.getValueAsBoolean("is_deleted")) {
				onFilePreviouslyDeleted();
			}
			
			//File changed locally, but new on server! (No need to check contents here)
			if(serverFileInfo == null) {
				onFileNotOnServer();
				return;
			}
			
			long serverVersion = serverFileInfo.getValueAsLong("version");
			long syncVersion = clientFileInfo.getValueAsLong("sync_version");
			long clientVersion = clientFileInfo.getValueAsLong("version");
			
			//File changed locally...
			if(fileContentChanged()) {
				
				clientVersion++;
				
				// Local file is in sync with server...
				if(syncVersion == serverVersion) {
					onFileUpdateServer();
					return;
				}
				
				// Move local file, insert local file info with new name... do not insert the old filename, should be done by FileDownloader...
				conflictHandling();

			}

			// Local file has not been changed since last sync
			// Server version is newer, download new file...
			if(serverVersion > syncVersion && syncVersion == clientVersion) {
				onFileUpdateClient();
				return;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void onFilePreviouslyDeleted() throws Exception {
		DatabaseTools.executeUpdate(
				"UPDATE mybox_client_files SET deleted=?, is_deleted=? WHERE client=? AND filename=?",
				false, false, clientId, myboxFilename);
		
		
	}

	private boolean fileContentChanged() throws Exception {
		
		if(isFile()) {

			if(clientFileInfo.getValueAsLong("size") != getFile().length()) {
				return true;
			}
			
			if(!clientFileInfo.getValueAsDate("modified").equals(fileTimestamp)) {
				
				if(!checksum.equalsIgnoreCase(clientFileInfo.getValueAsString("checksum"))) {
					DatabaseTools.executeUpdate(
							"UPDATE mybox_client_files SET modified=? WHERE client=? AND filename=? AND locked=?",
							fileTimestamp, clientId, myboxFilename, false);
					Log.info("ClientFileIndexer: Only timestamps of file '" + myboxFilename + "' changed, updated...");
					return false;
				}
				
				return true;
			}

			return false;
		}
		
		if(!clientFileInfo.getValueAsDate("modified").equals(fileTimestamp)) {
			DatabaseTools.executeUpdate(
					"UPDATE mybox_client_files SET modified=? WHERE client=? AND filename=? AND locked=?",
					fileTimestamp, clientId, myboxFilename, false);
			Log.info("ClientFileIndexer: Only timestamps of file '" + myboxFilename + "' changed, updated...");
		}
		
		return false;
	}

	
	private void onFileUpdateClient() throws Exception {
		Log.info("ClientFileIndexer: onFileUpdateClient: Downloading " + getTypeString() + " " + filename);
		
		long clientVersion = clientFileInfo.getValueAsLong("version");
		if((isFile()  && !checksum.equalsIgnoreCase(clientFileInfo.getValueAsStringNotNull("checksum"))) || clientFileInfo.getValueAsBoolean("deleted")) {
			clientVersion++;
		}

		if(isFile()) {
			MyBoxQueryTools.updateFile(clientId, myboxFilename, checksum, getFile().length(), fileTimestamp, clientVersion, clientVersion, false);
			FileClientSingle.downloadAsync(myboxFilename, clientId, getDirectory(), server, port);
		} else {
			MyBoxQueryTools.updateDirectory(clientId, myboxFilename, fileTimestamp, clientVersion, clientVersion);
		}
		clientFileInfo = MyBoxQueryTools.getFileInfo(clientId, myboxFilename);
		MyBoxQueryTools.updateServerEntryAndSyncVersion(clientFileInfo, serverFileInfo);
	}

	private void onFileUpdateServer() throws Exception {
		Log.info("ClientFileIndexer: onFileUpdateServer: " + filename);
		
		long clientVersion = clientFileInfo.getValueAsLong("version");
		if((isFile()  && !checksum.equalsIgnoreCase(clientFileInfo.getValueAsStringNotNull("checksum"))) || clientFileInfo.getValueAsBoolean("deleted")) {
			clientVersion++;
		}

		if(isFile()) {
			MyBoxQueryTools.updateFile(clientId, myboxFilename, checksum, getFile().length(), fileTimestamp, clientVersion, clientVersion, false);
			if(!checksum.equalsIgnoreCase(clientFileInfo.getValueAsStringNotNull("checksum"))) {
				FileClientSingle.uploadAsync(myboxFilename, clientId, getDirectory(), server, port);
			}
		} else {
			MyBoxQueryTools.updateDirectory(clientId, myboxFilename, fileTimestamp, clientVersion, clientVersion);
		}
		clientFileInfo = MyBoxQueryTools.getFileInfo(clientId, myboxFilename);
		MyBoxQueryTools.updateServerEntryAndSyncVersion(clientFileInfo, serverFileInfo);
	}

	private void onFileNotOnServer() throws Exception {
		Log.info("ClientFileIndexer: onFileNotOnServer: " + filename);
		
		long clientVersion = clientFileInfo.getValueAsLong("version");
		if((isFile()  && !checksum.equalsIgnoreCase(clientFileInfo.getValueAsStringNotNull("checksum"))) || clientFileInfo.getValueAsBoolean("deleted")) {
			clientVersion++;
		}

		if(isFile()) {
			MyBoxQueryTools.updateFile(clientId, myboxFilename, checksum, getFile().length(), fileTimestamp, clientVersion, clientVersion, false);
			FileClientSingle.uploadAsync(myboxFilename, clientId, getDirectory(), server, port);
		} else {
			MyBoxQueryTools.updateDirectory(clientId, myboxFilename, fileTimestamp, clientVersion, clientVersion);
		}
		clientFileInfo = MyBoxQueryTools.getFileInfo(clientId, myboxFilename);
		MyBoxQueryTools.updateServerEntryAndSyncVersion(clientFileInfo, serverFileInfo);
	}

	private void onNewFileOnServerExists() throws Exception {
		Log.info("ClientFileIndexer: onNewFileOnServerExists: " + filename);

		if(isFile()) {
			conflictHandling();
		} else {
			Log.info("ClientFileIndexer: Directory found: " + myboxFilename);
			MyBoxQueryTools.insertDirectory(clientId, myboxFilename, (Timestamp)serverFileInfo.getValue("modified"), serverFileInfo.getValueAsLong("version"), serverFileInfo.getValueAsLong("version"));
		}
	}

	private void conflictHandling() throws Exception {
		
		//Try to restore file info if checksums are equal...
		if(checksum.equalsIgnoreCase(serverFileInfo.getValueAsString("checksum"))) {
			Log.info("Restoring fileinfo of " + myboxFilename + " successful!");
			
			//TODO What if entry already exists?
			MyBoxQueryTools.insertFile(clientId, myboxFilename, checksum, getFile().length(), (Timestamp)serverFileInfo.getValue("modified"), serverFileInfo.getValueAsLong("version"), serverFileInfo.getValueAsLong("version"));
			return;
		}

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
		Log.info("ClientFileIndexer: onNewFileLockedOnServer: New " + getTypeString() + " '" + myboxFilename + "' found. Locked on server! Skipping...");
	}

	private void onNewFilePreviouslyDeleted() throws Exception {
		Log.info("ClientFileIndexer: onNewFilePreviouslyDeleted: New " + getTypeString() + " '" + myboxFilename + "' found. Previously deleted on server!");

		long version = serverFileInfo.getValueAsLong("version") + 1;

		if(isFile()) {
			MyBoxQueryTools.insertFile(clientId, myboxFilename, checksum, getFile().length(), fileTimestamp, version, version);
			FileClientSingle.uploadAsync(myboxFilename, clientId, getDirectory(), server, port);
		} else {
			MyBoxQueryTools.insertDirectory(clientId, myboxFilename, fileTimestamp, version, version);
		}
		clientFileInfo = MyBoxQueryTools.getFileInfo(clientId, myboxFilename);
		MyBoxQueryTools.updateServerEntryAndSyncVersion(clientFileInfo, serverFileInfo);
		
	}

	private void onNewFile() throws Exception {
		Log.info("ClientFileIndexer: onNewFile: New " + getTypeString() + " '" + myboxFilename + "' found. Not present on server!");

		if(isFile()) {
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
