package mybox.io;

import java.io.File;
import java.util.ArrayList;
import mybox.query.MyBoxQueryTools;
import piwotools.database.DatabaseTools;
import piwotools.database.Row;
import piwotools.log.Log;
import piwotools.thread.DelayedInfiniteThread;

public class DeletedFileRemover extends DelayedInfiniteThread {
	
	private String directory;
	private String id;
	
	private final static int DELAY = 30000;
	
	public DeletedFileRemover(String id, String directory) {
		super(DELAY);
		this.directory = directory;
		this.id = id;
	}

	private ArrayList<Row> getRemovingRequests() throws Exception {
		return DatabaseTools.getQueryResult(
				"SELECT * FROM mybox_client_files WHERE client=? AND deleted=? AND is_deleted=? AND locked=? UNION " +
				"SELECT m2.* FROM mybox_client_files m1, mybox_client_files m2  " +
				"WHERE m1.client=? AND m1.deleted=? AND m1.locked=? AND m2.locked=? AND m2.is_deleted=? AND m1.filename NOT IN " +
				"(SELECT filename FROM mybox_client_files WHERE client=? AND deleted=? AND is_deleted=? AND locked=?) " +
				"AND m1.filename = m2.filename " +
				"AND m2.client=? " +
				"AND m2.version = m2.sync_version  " +
				"AND m1.version >= m2.sync_version",
				id, true, false, false,
				MyBoxQueryTools.SERVERID, true, false, false, false,
				id, true, false, false,
				id);
	}
	
	@Override
	public void beforeRun() throws Exception {
	}

	@Override
	public void duringRun() throws Exception {
		for(Row fileEntry : getRemovingRequests()) {
			String filename = directory + fileEntry.getValueAsString("filename");
			File file = new File(filename);
			if(file.exists()) {
				if(file.delete()) {
					Log.info("DeletedFileRemover: Removing file " + file.getCanonicalPath());
					Row serverFileInfo = MyBoxQueryTools.getServerFileInfo(fileEntry.getValueAsString("filename"));
					DatabaseTools.executeUpdate(
							"UPDATE mybox_client_files SET is_deleted=?, deleted=?, version=?, sync_version=? WHERE client=? AND filename=?",
							true, true,
							serverFileInfo.getValueAsLong("version"), serverFileInfo.getValueAsLong("version"),
							id,
							fileEntry.getValueAsString("filename")
							);
				} else {
					Log.warn("DeletedFileRemover: Can't delete file or directory " + filename);
				}
			}
		}
	}

	@Override
	public void afterRun() throws Exception {
	}
	
	

}
