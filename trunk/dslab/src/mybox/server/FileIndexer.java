package mybox.server;

import java.io.File;
import java.util.ArrayList;
import piwotools.database.DatabaseConnection;
import piwotools.database.DatabaseTools;
import piwotools.database.Row;
import piwotools.io.FileTools;
import piwotools.io.FileWalker;
import piwotools.log.Log;

public class FileIndexer extends Thread {
	
	private static final int FILEINDEXER_WAIT = 10000;
	private static final int ERR_DATABASE = 0x08;
	private static final int FILEINDEXER_REMOVE_DELETED = 10;	//Remove deleted files from database every n-th run...
	private static int countRuns = 0;

	private class MyFileWalker implements FileWalker {

		@Override
		public void isDirectory(String dir) {
		}

		@Override
		public void isFile(String file) {
			try {
				String fileId = file.substring(ServerImpl.SERVER_DIR.length());
				
				//TODO Fetch all files only once per run...
				ArrayList<Row> fileEntries = DatabaseTools.getQueryResult(
						"SELECT * FROM mybox_files WHERE filename = ?",
						fileId
						);
				
				Row fileEntry = fileEntries.size() == 0 ? null : fileEntries.get(0);

				File f = new File(file);

				if(fileEntry == null) {
					DatabaseTools.executeUpdate(
							"INSERT INTO mybox_files (filename, checksum, size, modified, indexer_run) VALUES (?, ?, ?, ?, ?)", 
							fileId,
							FileTools.createSHA1checksum(file),
							f.length(),
							new java.sql.Timestamp(f.lastModified()),
							countRuns
							);
				} else {
		
					java.sql.Timestamp fileTimestamp = new java.sql.Timestamp(f.lastModified());
					long dbFilesize = fileEntry.getValueAsLong("size");
					
					if(dbFilesize != f.length() || fileEntry.getValueAsDate("modified").before(fileTimestamp)) {
						Log.info("File '" + fileId + "' has been changed.");
						DatabaseTools.executeUpdate(
								"UPDATE mybox_files SET checksum=?, size=?, modified=?, indexer_run=? WHERE filename=?", 
								FileTools.createSHA1checksum(file),
								f.length(),
								fileTimestamp,
								countRuns,
								fileId
								);
					} 
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private void removeDeleted() {
		try {
			ArrayList<Row> fileEntries = DatabaseTools.getQueryResult("SELECT * FROM mybox_files");
			for(Row fileEntry : fileEntries) {
				String filename = fileEntry.getValueAsString("filename");
				File file = new File(ServerImpl.SERVER_DIR + "/" + filename);
				Log.debug("Deleting entries from database: Check file " + filename);
				if(!file.exists()) {
					DatabaseTools.executeUpdate("DELETE FROM mybox_files WHERE filename = ?", filename);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		
		try {
			DatabaseConnection.setup("jdbc:postgresql://localhost/openreg?user=user&password=qwertz");
		} catch (Exception e) {
			Log.error("Unable to connect to specified database! " + e.getMessage());
			e.printStackTrace();
			System.exit(ERR_DATABASE);
		}
		
		try {
			while(true) {
				Log.debug("FileIndexer run " + countRuns + " started!");
				FileTools.fileWalker(ServerImpl.SERVER_DIR, new MyFileWalker());
				if(countRuns % FILEINDEXER_REMOVE_DELETED == 0) {
					Log.debug("FileIndexer: Removing deleted files from database!");
					removeDeleted();
				}
				Log.debug("FileIndexer run " + countRuns + " completed! Next start in " + FILEINDEXER_WAIT/1000 + " seconds.");
				sleep(FILEINDEXER_WAIT);
				countRuns++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	   	try {
			DatabaseConnection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}
}
