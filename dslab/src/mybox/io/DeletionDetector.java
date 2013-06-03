package mybox.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import mybox.query.MyBoxQueryTools;
import piwotools.database.DatabaseTools;
import piwotools.database.Row;
import piwotools.log.Log;

public class DeletionDetector extends DelayedInfiniteThread {
	
	private String directory;
	private String id;
	
	public String getDirectory() {
		return directory;
	}
	
	public void setDirectory(String dir) {
		directory = dir;
	}

	public DeletionDetector(String clientId, String myboxClientHome) {
		super();
		this.directory = myboxClientHome;
		this.id = clientId;
	}

	@Override
	public void beforeRun() throws Exception {
	}

	@Override
	public void duringRun() throws Exception {
		ArrayList<Row> fileEntries = DatabaseTools.getQueryResult(
				"SELECT * FROM mybox_client_files WHERE deleted=? AND client=?",
				false,
				id
				);
		
		for(Row fileEntry : fileEntries) {
			String filename = fileEntry.getValueAsString("filename");
			File file = new File(directory + filename);
			if(!file.exists() || file.isHidden()) {
				Log.info("Found deleted file or directory: " + filename);
				
				DatabaseTools.executeUpdate(
						"UPDATE mybox_client_files SET deleted=?, modified=?, version=? WHERE filename=? AND client=?", 
						true,
						new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()),
						fileEntry.getValueAsLong("version") + 1,
						filename,
						id
						);
				
				//Check if server file can be deleted...
				Row serverData = MyBoxQueryTools.getServerFileInfo(filename);
				if(serverData != null) {
					long serverVersion = serverData.getValueAsLong("version");
					long clientSyncVersion = fileEntry.getValueAsLong("sync_version");
					if(serverData.getValueAsBoolean("locked") == false && serverVersion <= clientSyncVersion) {
						DatabaseTools.executeUpdate(
								"UPDATE mybox_client_files SET deleted=?, modified=?, version=? WHERE filename=? AND client=?", 
								true,
								new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()),
								fileEntry.getValueAsLong("version") + 1,
								filename,
								MyBoxQueryTools.SERVERID
								);
						DatabaseTools.executeUpdate(
								"UPDATE mybox_client_files SET sync_version=? WHERE filename=? AND client=?",
								fileEntry.getValueAsLong("version") + 1,
								filename,
								id);
					} else {
						Log.warn("The file " + filename + " is out of synch!");
					}
				}
			}
		}
	}

	@Override
	public void afterRun() throws Exception {
	}
	
}
