package mybox.io;

import java.io.File;
import java.util.ArrayList;
import piwotools.database.DatabaseTools;
import piwotools.database.Row;
import piwotools.log.Log;

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
				"SELECT * FROM mybox_client_files " +
				"WHERE client=? " +
				"AND deleted=? AND is_deleted=?", 
				id,
				true,
				false);
	}
	
	@Override
	public void beforeRun() throws Exception {
	}

	@Override
	public void duringRun() throws Exception {
		ArrayList<Row> filesToDelete = getRemovingRequests();
		for(Row fileEntry : filesToDelete) {
			String filename = directory + fileEntry.getValueAsString("filename");
			File file = new File(filename);
			if(file.exists()) {
				if(file.delete()) {
					Log.info("DeletedFileRemover: Removing file " + fileEntry.getValueAsString("filename"));
					DatabaseTools.executeUpdate(
							"UPDATE mybox_client_files SET is_deleted=? WHERE client=? AND filename=?",
							true,
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
