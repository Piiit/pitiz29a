package mybox.query;

import java.util.ArrayList;
import piwotools.database.DatabaseTools;
import piwotools.database.Row;

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
				"	SELECT filename FROM mybox_client_files mcf2 " +
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
		DatabaseTools.executeUpdate(
				"UPDATE mybox_client_files SET locked=? WHERE filename=? AND (client=? OR client=?)",
				true,
				filename,
				clientId,
				"MYBOX_SERVER"
				);
	}
	
	public static void unlockFile(String filename, String clientId) throws Exception {
		DatabaseTools.executeUpdate(
				"UPDATE mybox_client_files SET locked=? WHERE filename=? AND (client=? OR client=?)",
				false,
				filename,
				clientId,
				"MYBOX_SERVER"
				);
	}
	
	public static void updateServerEntry(String filename, String clientId) throws Exception {
		Row clientData = DatabaseTools.getOneRowQueryResult(
				"SELECT * FROM mybox_client_files WHERE filename=? AND client=?",
				filename,
				clientId
				);

		Row serverData = getServerFileInfo(filename);
		
		if(serverData == null) {
			DatabaseTools.executeUpdate(
					"INSERT INTO mybox_client_files (version, checksum, locked, size, modified, filename, client) " +
					"VALUES (?,?,?,?,?,?,?)",
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
					"UPDATE mybox_client_files SET version=?, checksum=?, locked=?, size=?, modified=? " +
					"WHERE filename=? AND client=?",
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
		return DatabaseTools.getOneRowQueryResult(
				"SELECT * FROM mybox_client_files WHERE filename=? AND client=?",
				filename,
				SERVERID
				);
	}
}
