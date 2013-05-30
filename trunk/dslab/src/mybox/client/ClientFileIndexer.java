package mybox.client;

import java.io.File;
import piwotools.database.DatabaseTools;
import piwotools.database.Row;
import piwotools.io.FileTools;
import piwotools.log.Log;
import mybox.io.FileIndexer;

public class ClientFileIndexer extends FileIndexer {
	
	private String id;
	
	public ClientFileIndexer(String clientId, String directory) {
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

			Row data = DatabaseTools.getOneRowQueryResult(
					"SELECT * FROM mybox_client_files WHERE filename=? AND client=?", 
					myboxFilename, 
					id
					);
			
			java.sql.Timestamp fileTimestamp = new java.sql.Timestamp(f.lastModified());
			
			//New file found...
			if(data == null) {
				Log.info("New file '" + myboxFilename + "' found.");
				//TODO Check if a file with the same name has been deleted from server before... restore states!
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

					String checksum = FileTools.createSHA1checksum(filename);

					long version = data.getValueAsLong("version");
					if(!checksum.equalsIgnoreCase(data.getValueAsStringNotNull("checksum"))) {
						version++;
					}
					
					Log.info("File '" + myboxFilename + "' has been changed.");
					DatabaseTools.executeUpdate(
							"UPDATE mybox_client_files SET checksum=?, size=?, modified=?, version=?, deleted=? " +
							"WHERE client=? AND filename=?",
							checksum,
							f.length(),
							fileTimestamp,
							version,
							false,
							id, 
							myboxFilename
							);
					
					//If server_file is deleted and server_version < version... change server_state to deleted=false 
					Row serverData = DatabaseTools.getOneRowQueryResult(
							"SELECT * FROM mybox_client_files WHERE client=? AND filename=?",
							"MYBOX_SERVER",
							myboxFilename);
					
					if(serverData.getValueAsBoolean("deleted") && serverData.getValueAsLong("version") < version) {
						Log.info("Deleted file restored! " + filename);
						DatabaseTools.executeUpdate(
								"UPDATE mybox_client_files SET checksum=?, size=?, modified=?, version=?, deleted=? " +
								"WHERE client=? AND filename=?",
								checksum,
								f.length(),
								fileTimestamp,
								version,
								false,
								"MYBOX_SERVER", 
								myboxFilename
								);
					}
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
