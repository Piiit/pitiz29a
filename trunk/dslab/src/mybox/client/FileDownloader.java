package mybox.client;

import java.io.File;
import java.util.ArrayList;
import piwotools.database.DatabaseTools;
import piwotools.database.Row;
import piwotools.log.Log;
import piwotools.thread.DelayedInfiniteThread;
import mybox.network.FileClientSingle;
import mybox.query.MyBoxQueryTools;

public class FileDownloader extends DelayedInfiniteThread {
	
	private String directory;
	private String clientId;
	private String server;
	private int port;
	
	public FileDownloader(String clientId, String directory, String server, int port) {
		super();
		this.directory = directory;
		this.clientId = clientId;
		this.server = server;
		this.port = port;
	}

	public ArrayList<Row> getFileToDownload() throws Exception {
		return DatabaseTools.getQueryResult(
				"select mcf1.* from mybox_client_files mcf1, mybox_client_files mcf2 " +
				"where mcf1.client=? " +
				"and mcf1.deleted=?  " +
				"and mcf1.locked=? " +
				"and mcf2.locked=? " +
				"and (mcf1.version > mcf2.version " +
				"and mcf2.version = mcf2.sync_version " +
				"and mcf2.client=? " +
				"and mcf1.filename = mcf2.filename) " +
				"union " +
				"select * from mybox_client_files " +
				"where client=? " +
				"and deleted=?  " +
				"and locked=? " +
				"and filename not in (select filename from mybox_client_files where client=?) ",
				MyBoxQueryTools.SERVERID,
				false, false, false,
				clientId,
				MyBoxQueryTools.SERVERID,
				false, false,
				clientId
				);
	}
	

	@Override
	public void beforeRun() throws Exception {
	}

	@Override
	public void duringRun() throws Exception {
		for(Row fileInfo: getFileToDownload()) {
			
			String filename = fileInfo.getValueAsString("filename");
			Row clientFileInfo = MyBoxQueryTools.getFileInfo(clientId, filename);
			
			if(clientFileInfo == null) {
				MyBoxQueryTools.insertFileOrDirectoryAndLock(clientId, filename, 
						fileInfo.getValueAsString("checksum"), 
						fileInfo.getValueAsLong("size"),
						fileInfo.getValueAsTimestamp("modified"),
						fileInfo.getValueAsLong("version"),
						fileInfo.getValueAsLong("sync_version")
						);
			} else {
				clientFileInfo.setValue("client", clientId);
				MyBoxQueryTools.updateFileOrDirectoryAndLock(clientFileInfo);
			}
			MyBoxQueryTools.lockFile(filename, clientId);
			
			//Directory found, if no checksum present...
			if(fileInfo.getValueAsString("checksum") == null) {
				if ((new File(directory + filename)).mkdirs()) {
					Log.info("FileDownloader: New folder '" + directory + filename + "' created!");
				} else {
					Log.error("FileDownloader: Can't create new folder '" + directory + filename + "'!");
				}
			} else {
				FileClientSingle.downloadAsync(filename, clientId, directory, server, port);
			}
			MyBoxQueryTools.unlockFile(filename, clientId);
			
		}
	}

	@Override
	public void afterRun() throws Exception {
	}
}
