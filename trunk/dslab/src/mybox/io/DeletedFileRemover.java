package mybox.io;

import java.io.File;
import java.util.ArrayList;
import piwotools.database.DatabaseTools;
import piwotools.database.Row;
import piwotools.log.Log;

public class DeletedFileRemover extends Thread {
	
	private static final int DEFAULT_WAIT = 30000;
	
	private String directory;
	private String id;
	private int waitInterval = DEFAULT_WAIT;

	
	public DeletedFileRemover(String id, String directory) {
		super();
		this.directory = directory;
		this.id = id;
	}

	private ArrayList<Row> getRemovingRequests() throws Exception {
		return DatabaseTools.getQueryResult(
				"SELECT * FROM mybox_client_files " +
				"WHERE client=? " +
				"AND deleted=?", 
				id,
				true);
	}
	
	public void run() {
		try {
			while(true) {
				ArrayList<Row> filesToDelete = getRemovingRequests();
				for(Row fileEntry : filesToDelete) {
					String filename = directory + fileEntry.getValueAsString("filename");
					File file = new File(filename);
					if(file.exists()) {
						if(file.delete()) {
							Log.info("DeletedFileRemover: Removing file " + fileEntry.getValueAsString("filename"));
						} else {
							Log.warn("DeletedFileRemover: Can't delete file or directory " + filename);
						}
					}
				}
				sleep(waitInterval);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	

}
