package mybox.server;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import piwotools.database.DatabaseTools;
import piwotools.database.Row;
import piwotools.io.FileTools;
import piwotools.log.Log;
import mybox.io.FileIndexer;

public class ServerFileIndexer extends FileIndexer {
	
	private static final int FILEINDEXER_REMOVE_DELETED = 10;	//Remove deleted files from database every n-th run...
	private static int countRuns = 0;
	private String id;
	
	public ServerFileIndexer(String clientId, String directory) {
		super();
		this.id = clientId;
		setDirectory(directory);
	}

	@Override
	public void onDirectory(String dirname) {
		try {
			String myboxDirname = dirname.substring(getDirectory().length()) + "/";
			File d = new File(dirname);
			
			//Skip hidden files...
			if(d.isHidden()) {
				return;
			}

			Row data = DatabaseTools.getOneRowQueryResult(
					"SELECT * FROM mybox_client_files WHERE filename=? AND client=?", 
					myboxDirname, 
					id
					);
			
			java.sql.Timestamp fileTimestamp = new java.sql.Timestamp(d.lastModified());
			
			//New file found...
			if(data == null) {
				Log.info("New directory '" + myboxDirname + "' found.");
				DatabaseTools.executeUpdate(
						"INSERT INTO mybox_client_files (client, filename, modified, version) VALUES (?,?,?,?)",
						id,
						myboxDirname,
						fileTimestamp,
						0
						);
			} else {
				
				if(data.getValueAsDate("modified").before(fileTimestamp)) {
					Log.info("Directory '" + myboxDirname + "' has been changed.");
					DatabaseTools.executeUpdate("UPDATE mybox_client_files SET modified=?, version=? " +
							"WHERE client=? AND filename=?",
							fileTimestamp,
							data.getValueAsLong("version") + 1,
							id, 
							myboxDirname
							);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onFile(String filename) {
		try {
			String myboxFilename = filename.substring(getDirectory().length());
			File f = new File(filename);
			
			//Skip hidden files...
			if(f.isHidden()) {
				return;
			}

			//TODO Take all files at once, and compare them...
			Row data = DatabaseTools.getOneRowQueryResult(
					"SELECT * FROM mybox_client_files WHERE filename=? AND client=?", 
					myboxFilename, 
					id
					);
			
			java.sql.Timestamp fileTimestamp = new java.sql.Timestamp(f.lastModified());
			
			//New file found...
			if(data == null) {
				Log.info("New file '" + myboxFilename + "' found.");
				DatabaseTools.executeUpdate(
						"INSERT INTO mybox_client_files (client, filename, checksum, size, modified, version) VALUES (?,?,?,?,?,?)",
						id,
						myboxFilename,
						FileTools.createSHA1checksum(filename),
						f.length(),
						fileTimestamp,
						0
						);
			} else {
				
				//File changed?
				long dbFilesize = data.getValueAsLong("size");
				
				if(dbFilesize != f.length() || data.getValueAsDate("modified").before(fileTimestamp)) {
					Log.info("File '" + myboxFilename + "' has been changed.");
					DatabaseTools.executeUpdate(
							"UPDATE mybox_client_files SET checksum=?, size=?, modified=?, version=?, deleted=? " +
							"WHERE client=? AND filename=?",
							FileTools.createSHA1checksum(filename),
							f.length(),
							fileTimestamp,
							data.getValueAsLong("version") + 1,
							false,
							id, 
							myboxFilename
							);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void beforeRun() throws Exception {
	}
	
	private void removeDeleted() {
		try {
			ArrayList<Row> fileEntries = DatabaseTools.getQueryResult(
					"SELECT * FROM mybox_client_files WHERE client=? AND deleted=?",
					id,
					false
					);
			
			for(Row fileEntry : fileEntries) {
				
				String filename = fileEntry.getValueAsString("filename");
				File file = new File(ServerImpl.SERVER_DIR + "/" + filename);
			
				if(!file.exists() || file.isHidden()) {
					Log.info("Found deleted file or directory: " + filename);
					DatabaseTools.executeUpdate(
							"UPDATE mybox_client_files SET deleted=?, modified=? WHERE filename=? AND client=?", 
							true,
							new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()),
							filename,
							id
							);				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void duringRun() throws Exception {
		if(countRuns % FILEINDEXER_REMOVE_DELETED == 0) {
			Log.debug("FileIndexer: Removing deleted files from database!");
			removeDeleted();
		}
	}

	@Override
	public void afterRun() throws Exception {
	}

}
