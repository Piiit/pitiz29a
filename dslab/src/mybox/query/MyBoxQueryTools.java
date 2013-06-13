package mybox.query;

import java.sql.Timestamp;
import java.util.ArrayList;
import piwotools.database.DatabaseTools;
import piwotools.database.Row;
import piwotools.log.Log;

public class MyBoxQueryTools {
	
	public final static String SERVERID = "MYBOX_SERVER";

	public static ArrayList<Row> getUploadableFiles(final String clientId) throws Exception {
		return DatabaseTools.getQueryResult(
				"SELECT * FROM mybox_client_files mcf0 " +
				"WHERE client = ? " +
				"AND deleted = ? " +
				"AND locked = ? " +
				"AND checksum IS NOT NULL " +
				"AND  " +
				"(filename IN ( " +
				"	SELECT filename FROM mybox_client_files mcf2 " +
				"	WHERE client = ? " +
				"	AND mcf2.checksum <> mcf0.checksum " +
				"	AND mcf2.version <= mcf0.version " +
				")  " +
				"OR filename NOT IN ( " +
				"	SELECT filename FROM mybox_client_files " +
				"	WHERE client = ? " +
				")) ",
				clientId,
				false,
				false,
				SERVERID,
				SERVERID
				);
	}
	
	public static void lockFile(String filename, String clientId) throws Exception {
		Log.info("Locking: " + filename + " on " + clientId);
		DatabaseTools.executeUpdate(
				"UPDATE mybox_client_files SET locked=? WHERE filename=? AND (client=? OR client=?)",
				true,
				filename,
				clientId,
				SERVERID
				);
	}
	
	public static void unlockFile(String filename, String clientId) throws Exception {
		Log.info("Unlocking: " + filename + " on " + clientId);
		DatabaseTools.executeUpdate(
				"UPDATE mybox_client_files SET locked=? WHERE filename=? AND (client=? OR client=?)",
				false,
				filename,
				clientId,
				SERVERID
				);
	}
	
	public static void updateServerEntryAndSyncVersion(Row clientData, Row serverData) throws Exception {
		
		if(serverData != null && !clientData.getValueAsString("filename").equals(serverData.getValueAsString("filename"))) {
			throw new Exception("Can't update server entry ");
		}
		
		String clientId = clientData.getValueAsString("client");
		
		if(serverData == null) {
			DatabaseTools.executeUpdate(
					"INSERT INTO mybox_client_files (deleted, version, checksum, locked, size, modified, filename, client) " +
					"VALUES (?,?,?,?,?,?,?,?)",
					clientData.getValueAsBoolean("deleted"),
					clientData.getValueAsLong("version"),
					clientData.getValueAsString("checksum"),
					clientData.getValueAsBoolean("locked"),
					clientData.getValueAsLong("size"),
					clientData.getValue("modified"),
					clientData.getValueAsString("filename"),
					SERVERID
					);
		} else {
			DatabaseTools.executeUpdate(
					"UPDATE mybox_client_files SET deleted=?, version=?, checksum=?, locked=?, size=?, modified=? " +
					"WHERE filename=? AND client=?",
					clientData.getValueAsBoolean("deleted"),
					clientData.getValueAsLong("version"),
					clientData.getValueAsString("checksum"),
					clientData.getValueAsBoolean("locked"),
					clientData.getValueAsLong("size"),
					clientData.getValue("modified"),
					clientData.getValueAsString("filename"),
					SERVERID
					);
		}
		DatabaseTools.executeUpdate(
				"UPDATE mybox_client_files SET sync_version=? " +
				"WHERE filename=? AND client=?",
				clientData.getValueAsLong("version"),
				clientData.getValueAsString("filename"),
				clientId
				);
	}
	
	public static Row getServerFileInfo(String filename) throws Exception {
		return getFileInfo(SERVERID, filename);
	}

	public static Row getFileInfo(String id, String filename) throws Exception {
		return DatabaseTools.getOneRowQueryResult(
				"SELECT * FROM mybox_client_files WHERE filename=? AND client=?",
				filename,
				id
				);
	}
	
	public static void insertFileOrDirectoryAndLock(String client, String filename, String checksum, long size, Timestamp modified, long version, long syncVersion) throws Exception {
		//...is a directory...
		if(checksum == null || checksum.equals("")) {
			insertDirectory(client, filename, modified, version, syncVersion, true);
		} else {
			insertFile(client, filename, checksum, size, modified, version, syncVersion, true);
		}
	}
	

	public static void insertFile(String client, String filename, String checksum, long size, Timestamp modified, long version, long syncVersion) throws Exception {
		insertFile(client, filename, checksum, size, modified, version, syncVersion, false);
	}
	
	public static void insertFile(String client, String filename, String checksum, long size, Timestamp modified, long version, long syncVersion, boolean locked) throws Exception {
		DatabaseTools.executeUpdate(
				"INSERT INTO mybox_client_files (client, filename, checksum, size, modified, version, sync_version, locked) VALUES (?,?,?,?,?,?,?,?)",
				client,
				filename,
				checksum,
				size,
				modified,
				version,
				syncVersion,
				locked
				);
	}

	public static void insertDirectory(String client, String filename, Timestamp modified, long version, long syncVersion) throws Exception {
		insertDirectory(client, filename, modified, version, syncVersion, false);
	}
	
	public static void insertDirectory(String client, String filename, Timestamp modified, long version, long syncVersion, boolean locked) throws Exception {
		DatabaseTools.executeUpdate(
				"INSERT INTO mybox_client_files (client, filename, modified, version, sync_version, locked) VALUES (?,?,?,?,?,?)",
				client,
				filename,
				modified,
				version,
				syncVersion,
				locked
				);
	}
	
	public static void insertServerDirectory(String filename, Timestamp modified, long version) throws Exception {
		insertDirectory(SERVERID, filename, modified, version, 0, false);
	}

	public static void updateServerDeleted(String filename, boolean deleted) throws Exception {
		DatabaseTools.executeUpdate(
				"UPDATE mybox_client_files SET deleted=? WHERE client=? AND filename=?",
				deleted,
				SERVERID,
				filename);
	}
	
	public static void updateFile(String client, String filename, String checksum, long size, Timestamp modified, long version, long syncVersion, boolean locked) throws Exception {
		DatabaseTools.executeUpdate(
				"UPDATE mybox_client_files SET checksum=?, size=?, modified=?, version=?, deleted=?, is_deleted=?, sync_version=?, locked=? " +
				"WHERE client=? AND filename=?",
				checksum,
				size,
				modified,
				version,
				false,
				false,
				syncVersion,
				locked,
				client, 
				filename
				);
	}
	
	public static void updateFileOrDirectoryAndLock(Row fileInfo) throws Exception {
		updateFile(
				fileInfo.getValueAsString("client"),
				fileInfo.getValueAsString("filename"),
				fileInfo.getValueAsString("checksum"),
				fileInfo.getValueAsLong("size"),
				fileInfo.getValueAsTimestamp("modified"),
				fileInfo.getValueAsLong("version"),
				fileInfo.getValueAsLong("sync_version"),
				true
				);
	}

	
	public static void updateDirectory(String client, String filename, Timestamp modified, long version, long syncVersion) throws Exception {
		updateFile(client, filename, null, 0, modified, version, syncVersion, false);
	}

	public static void updateServerFile(String filename, String checksum, long size, Timestamp modified, long version) throws Exception {
		updateFile(SERVERID, filename, checksum, size, modified, version, 0, false);
		
	}

	public static void unlockAllFiles(String clientId) throws Exception {
		//TODO unlock also server files...
		DatabaseTools.executeUpdate(
				"UPDATE mybox_client_files m1 SET locked=? WHERE client=?",
				false,
				clientId
				);
	}
	
}
