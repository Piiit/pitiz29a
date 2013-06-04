package mybox.client;

import java.util.ArrayList;
import piwotools.database.DatabaseTools;
import piwotools.database.Row;
import piwotools.log.Log;
import mybox.io.DelayedInfiniteThread;
import mybox.network.FileClientSingle;
import mybox.query.MyBoxQueryTools;

public class FileDownloader extends DelayedInfiniteThread {
	
	private String directory;
	private String clientId;
	private String server;
	private int port;
	
	public FileDownloader(String clientId, String directory, String server, int port) {
		super();
		this.directory = directory;
		this.clientId = clientId;
		this.server = server;
		this.port = port;
	}

	public ArrayList<Row> getFileToDownload() throws Exception {
		return DatabaseTools.getQueryResult(
				"select mcf1.* from mybox_client_files mcf1, mybox_client_files mcf2 " +
				"where mcf1.client=?  " +
				"and mcf1.deleted=?  " +
				"and mcf1.locked=? " +
				"and mcf2.locked=? " +
				"and (mcf1.version > mcf2.version " +
				"and mcf2.version = mcf2.sync_version " +
				"and mcf2.client=? " +
				"and mcf1.filename = mcf2.filename) " +
				"union " +
				"select * from mybox_client_files " +
				"where client=?  " +
				"and deleted=?  " +
				"and locked=? " +
				"and filename not in (select filename from mybox_client_files where client=?) ",
				MyBoxQueryTools.SERVERID,
				false, false, false,
				clientId,
				MyBoxQueryTools.SERVERID,
				false, false,
				clientId
				);
	}
	

	@Override
	public void beforeRun() throws Exception {
	}

	@Override
	public void duringRun() throws Exception {
		for(Row fileInfo: getFileToDownload()) {
			String filename = fileInfo.getValueAsString("filename");
			FileClientSingle fileClient = new FileClientSingle(clientId, directory, filename, server, port);
			fileClient.setType(false);
			fileClient.start();
//			fileClient.join();
		}
	}

	@Override
	public void afterRun() throws Exception {
	}
}
