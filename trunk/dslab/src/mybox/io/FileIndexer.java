package mybox.io;

import piwotools.io.FileTools;
import piwotools.io.FileWalker;
import piwotools.log.Log;

public abstract class FileIndexer extends Thread {
	private static final int FILEINDEXER_DEFAULT_WAIT = 10000;
	private static final int ERR_BEFORE_RUN = 0x10;
	private static final int ERR_DURING_RUN = 0x20;
	private static final int ERR_AFTER_RUN = 0x40;
	private static int countRuns = 0;

	private int waitInterval = FILEINDEXER_DEFAULT_WAIT;
	private String directory = "";
	
	public abstract void onDirectory(String dirname);
	public abstract void onFile(String filename);
	public abstract void beforeRun() throws Exception;
	public abstract void duringRun() throws Exception;
	public abstract void afterRun() throws Exception;
	
	public int getRun() {
		return countRuns;
	}
	
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
	
	private class MyFileWalker implements FileWalker {

		@Override
		public void isDirectory(String dir) {
			onDirectory(dir);
		}

		@Override
		public void isFile(String file) {
			onFile(file);
		}
		
	}
	
	public void run() {

		try {
			beforeRun();
		} catch (Exception e) {
			Log.error("BeforeRun routine exited with errors! " + e.getMessage());
			e.printStackTrace();
			System.exit(ERR_BEFORE_RUN);
		}
		
		try {
			while(true) {
				Log.debug("FileIndexer for directory '" + getDirectory() + "': run " + countRuns + " started!");
				FileTools.fileWalker(directory, new MyFileWalker());
				duringRun();
				Log.debug("FileIndexer for directory '" + getDirectory() + "': run " + countRuns + " completed! Next start in " + waitInterval/1000 + " seconds.");
				sleep(waitInterval);
				countRuns++;
			}
		} catch (Exception e) {
			Log.error("DuringRun routine exited with errors! " + e.getMessage());
			e.printStackTrace();
			System.exit(ERR_DURING_RUN);
		}
		
		try {
			afterRun();
		} catch (Exception e) {
			Log.error("AfterRun routine exited with errors! " + e.getMessage());
			e.printStackTrace();
			System.exit(ERR_AFTER_RUN);
		}
	}
}
