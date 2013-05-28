package mybox.client;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import piwotools.database.DatabaseTools;
import piwotools.database.Row;
import piwotools.log.Log;

public class DeletionDetector extends Thread {
	
	private String directory;
	private String clientId;
	private int waitInterval = 10000;
	
	public void setWaitInterval(int wait_ms) {
		waitInterval = wait_ms;
	}
	
	public int getWaitInterval() {
		return waitInterval;
	}
	
	public String getDirectory() {
		return directory;
	}
	
	public void setDirectory(String dir) {
		directory = dir;
	}

	public DeletionDetector(String clientId, String myboxClientHome) {
		super();
		this.directory = myboxClientHome;
		this.clientId = clientId;
	}

	public void run() {
		try {
			while(true) {
				ArrayList<Row> fileEntries = DatabaseTools.getQueryResult(
						"SELECT * FROM mybox_client_files WHERE deleted=?",
						false
						);
				
				for(Row fileEntry : fileEntries) {
					String filename = fileEntry.getValueAsString("filename");
					File file = new File(directory + filename);
					if(!file.exists() || file.isHidden()) {
						Log.info("Found deleted file or directory: " + filename);
						DatabaseTools.executeUpdate(
								"UPDATE mybox_client_files SET deleted=?, modified=? WHERE filename=? AND client=?", 
								true,
								new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()),
								filename,
								clientId
								);
					}
				}
				sleep(waitInterval);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
