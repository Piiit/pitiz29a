package mybox.client;

import mybox.io.DelayedInfiniteThread;
import mybox.network.FileClientSingle;

public class FileDownloader extends DelayedInfiniteThread {

	@Override
	public void beforeRun() throws Exception {
	}

	@Override
	public void duringRun() throws Exception {
		FileClientSingle fileClient = new FileClientSingle(id, getDirectory(), filename, "localhost", 13267);
		fileClient.setType(false);
		fileClient.start();
		fileClient.join();
	}

	@Override
	public void afterRun() throws Exception {
	}
}
