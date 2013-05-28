package mybox.server;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import piwotools.database.DatabaseTools;
import piwotools.database.Row;
import piwotools.log.Log;

public class DeletedFileRemover extends Thread {
	
	private static final String SERVERID = "MYBOX_SERVER";
	private static final int DEFAULT_WAIT = 10000;

	private int waitInterval = DEFAULT_WAIT;
	
	private ArrayList<Row> getRemovingRequests() throws Exception {
		return DatabaseTools.getQueryResult(
				"SELECT * FROM mybox_client_files " +
				"WHERE client=? " +
				"AND filename IN (SELECT filename FROM mybox_client_files WHERE client<>? AND deleted=?)",
				SERVERID,
				SERVERID,
				true);
	}
	
	public void run() {
		try {
			while(true) {
				ArrayList<Row> filesToDelete = getRemovingRequests();
				for(Row fileEntry : filesToDelete) {
					Log.debug("Removing file " + fileEntry.getValueAsString("filename"));
					String filename = ServerImpl.SERVER_DIR + fileEntry.getValueAsString("filename");
					File file = new File(filename);
					if(file.exists()) {
						if(!file.delete()) {
							throw new Exception("Can't delete file " + filename);
						}
					}
					DatabaseTools.executeUpdate(
							"UPDATE mybox_client_files SET deleted=?, modified=? WHERE filename=? AND client=?", 
							true,
							new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()),
							filename,
							SERVERID
							);
				}
				
				DatabaseTools.executeUpdate(
						"DELETE FROM mybox_client_files WHERE client<>? AND deleted=?",
						SERVERID,
						true
						);
				
				sleep(waitInterval);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	

}
