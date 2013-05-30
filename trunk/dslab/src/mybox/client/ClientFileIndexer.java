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
	
	private String id;
	
	public ClientFileIndexer(String clientId, String directory) {
		super();
		this.id = clientId;
		setDirectory(directory);
	}
	
	private void uploadAsync(String filename) throws InterruptedException {
		FileClientSingle fileClient = new FileClientSingle(id, getDirectory(), filename, "localhost", 13267);
		fileClient.start();
//		fileClient.join();
	}
	
	private void handleChanges(String filename, boolean isFile) {
		
		String typeString = isFile ? "file" : "directory";
		String myboxFilename = filename.substring(getDirectory().length());
		File file = new File(filename);
		Row clientFileInfo = null;
		Row serverFileInfo = null;

		//Skip hidden files...
		if(file.isHidden()) {
			return;
		}

		//File found on local file system...
		try {
			clientFileInfo = MyBoxQueryTools.getFileInfo(id, myboxFilename);
			serverFileInfo = MyBoxQueryTools.getServerFileInfo(myboxFilename);
			
			java.sql.Timestamp fileTimestamp = new java.sql.Timestamp(file.lastModified());

			//New file found, no client file info present...
			if(clientFileInfo == null) {
				
				//no server file info present > insert new client file info...
				if(serverFileInfo == null) {
					Log.info("New " + typeString + " '" + myboxFilename + "' found. Not present on server!");
					if(isFile) {
						//nothing to do on server side for files... FileClient should do the job!
						String checksum = FileTools.createSHA1checksum(filename);
						MyBoxQueryTools.insertFile(id, myboxFilename, checksum, file.length(), fileTimestamp, 0, 0);
						
						uploadAsync(myboxFilename);
						
					} else {
						MyBoxQueryTools.insertDirectory(id, myboxFilename, fileTimestamp, 0, 0);
						MyBoxQueryTools.insertServerDirectory(myboxFilename, fileTimestamp, 0);
					}
					return;
				}
				
				//server file info exists, but server file has been deleted before...
				if(serverFileInfo.getValueAsBoolean("deleted")) {
					Log.info("New " + typeString + " '" + myboxFilename + "' found. Previously deleted on server!");

					long version = serverFileInfo.getValueAsLong("version") + 1;
					if(isFile) {
						String checksum = FileTools.createSHA1checksum(filename);
						MyBoxQueryTools.insertFile(id, myboxFilename, checksum, file.length(), fileTimestamp, version, version);
						
						uploadAsync(myboxFilename);
						
					} else {
						MyBoxQueryTools.insertDirectory(id, myboxFilename, fileTimestamp, version, version);
					}
					clientFileInfo = MyBoxQueryTools.getFileInfo(id, myboxFilename);
					MyBoxQueryTools.updateServerEntryAndSyncVersion(clientFileInfo, serverFileInfo);
					return;
				} 

				//server file info exists, must it is different from newly found local file... 
				if(isFile) {
					
					//...trying to restore...
					String checksum = FileTools.createSHA1checksum(filename);
					if(checksum.equalsIgnoreCase(serverFileInfo.getValueAsString("checksum"))) {
						MyBoxQueryTools.insertFile(id, myboxFilename, checksum, file.length(), fileTimestamp, serverFileInfo.getValueAsLong("version"), serverFileInfo.getValueAsLong("version"));
						return;
					}
					
					//...CONFLICT... Trying to move to a new file...
					Log.warn("New " + typeString + " '" + myboxFilename + "' is out of sync. Different file exists on server with same name.");
					
					// Move local file, insert local file info with new name... do not insert the old filename, should be done by FileDownloader...
					String newFilenameExtension = "(OUT OF SYNC " + id + ")";
					String newFilename = filename + newFilenameExtension ;
					if(!file.renameTo(new File(newFilename))) {
						Log.warn("Not possible to move " + typeString + " " + filename + " to " + newFilename);
						throw new Exception("Not possible to move " + typeString + " " + filename);
		    	    }
					MyBoxQueryTools.insertFile(id, myboxFilename + newFilenameExtension, checksum, file.length(), fileTimestamp, 0, 0);
					
					uploadAsync(myboxFilename + newFilenameExtension);
					
					return;
				} 
				
				//server file info exists, it is a directory...
				MyBoxQueryTools.insertDirectory(id, myboxFilename, (Timestamp)serverFileInfo.getValue("modified"), serverFileInfo.getValueAsLong("version"), serverFileInfo.getValueAsLong("version"));
				return;
			}					
				
			//File changed, client file info present...
			long dbFilesize = isFile ? clientFileInfo.getValueAsLong("size") : 0;
			
			if((isFile && dbFilesize != file.length()) || clientFileInfo.getValueAsDate("modified").before(fileTimestamp) || serverFileInfo == null) {

				String checksum = isFile ? FileTools.createSHA1checksum(filename) : null;

				long version = clientFileInfo.getValueAsLong("version");
				if((isFile  && !checksum.equalsIgnoreCase(clientFileInfo.getValueAsStringNotNull("checksum"))) || clientFileInfo.getValueAsBoolean("deleted")) {
					version++;
				}
				
				Log.info(typeString + " '" + myboxFilename + "' has been changed.");
				
				//File changed locally, but server hasn't any file info...
				if(serverFileInfo == null) {
					if(isFile) {
						MyBoxQueryTools.updateFile(id, myboxFilename, checksum, file.length(), fileTimestamp, version, version);
						uploadAsync(myboxFilename);
					} else {
						MyBoxQueryTools.updateDirectory(id, myboxFilename, fileTimestamp, version, version);
					}
					clientFileInfo = MyBoxQueryTools.getFileInfo(id, myboxFilename);
					MyBoxQueryTools.updateServerEntryAndSyncVersion(clientFileInfo, serverFileInfo);
					return;
				}
				
				
				// Server file info: status = deleted....
//				if(serverFileInfo.getValueAsBoolean("deleted")) {

					// If server file and client file have been deleted before 
					// and server_version == sync_version... perform a restore!
					long serverVersion = serverFileInfo.getValueAsLong("version");
					long syncVersion = clientFileInfo.getValueAsLong("sync_version");
//					if(clientFileInfo.getValueAsBoolean("deleted") && serverVersion == syncVersion) {
					if(serverVersion == syncVersion || (checksum.equalsIgnoreCase(serverFileInfo.getValueAsString("checksum")) && !clientFileInfo.getValueAsBoolean("deleted"))) {
//						Log.info("Deleted " + typeString + " restored! " + filename);
						if(isFile) {
							MyBoxQueryTools.updateFile(id, myboxFilename, checksum, file.length(), fileTimestamp, version, version);
							uploadAsync(myboxFilename);
						} else {
							MyBoxQueryTools.updateDirectory(id, myboxFilename, fileTimestamp, version, version);
						}
						clientFileInfo = MyBoxQueryTools.getFileInfo(id, myboxFilename);
						MyBoxQueryTools.updateServerEntryAndSyncVersion(clientFileInfo, serverFileInfo);
						return;
					}
					
					// Move local file, insert local file info with new name... do not insert the old filename, should be done by FileDownloader...
					String newFilenameExtension = "(OUT OF SYNC " + id + ")";
					String newFilename = filename + newFilenameExtension ;
					if(file.renameTo(new File(newFilename))) {
						MyBoxQueryTools.insertFile(id, myboxFilename + newFilenameExtension, checksum, file.length(), fileTimestamp, 0, 0);
						return;
					}
					
					Log.error("CONFLICT: Not possible to move " + typeString + " " + filename + " to " + newFilename);
					throw new Exception("Not possible to move " + typeString + " " + filename);
					
					// If file has not been deleted on client side and the sync_version differs only by one, and now further 
					// changes have been made... delete the file.
					// >>> SHOULD BE DONE BY FILE REMOVER...
//					if(!clientFileInfo.getValueAsBoolean("deleted") && (serverVersion - 1) == syncVersion && version == syncVersion) {
//						
//					}
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onDirectory(String dirname) {
		handleChanges(dirname, false);
	}

	@Override
	public void onFile(String filename) {
		handleChanges(filename, true);
	}

	@Override
	public void beforeRun() throws Exception {
	}

	@Override
	public void duringRun() throws Exception {
	}

	@Override
	public void afterRun() throws Exception {
	}

}
