package mybox.server;

import java.io.File;
import piwotools.database.DatabaseTools;
import piwotools.database.Row;
import piwotools.io.FileTools;
import piwotools.log.Log;
import mybox.io.FileIndexer;

public class ServerFileIndexer extends FileIndexer {
	
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

	@Override
	public void duringRun() throws Exception {
	}

	@Override
	public void afterRun() throws Exception {
	}

}
