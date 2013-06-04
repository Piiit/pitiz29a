package mybox.io;

import java.io.File;

import piwotools.io.FileTools;
import piwotools.io.FileWalker;
import piwotools.log.Log;

public abstract class FileIndexer extends DelayedInfiniteThread {

	private String directory = "";
	
	public abstract void onDirectory(String dirname);
	public abstract void onFile(String filename);
	
	public String getDirectory() {
		return directory;
	}
	
	public void setDirectory(String dir) {
		File f = new File(dir);
		directory = f.getAbsolutePath() + "/";
	}
	
	@Override
	public void beforeRun() throws Exception {
	}
	
	@Override
	public void duringRun() throws Exception {
		Log.debug("FileIndexer for directory '" + directory + "': run " + getRun() + " started!");
		
		FileTools.fileWalker(directory, new FileWalker() {

			@Override
			public void isDirectory(String dir) {
				onDirectory(dir);
			}

			@Override
			public void isFile(String file) {
				onFile(file);
			}
			
		});
		
		Log.debug("FileIndexer for directory '" + directory + "': run " + getRun() + " completed! Next start in " + getDelay()/1000 + " seconds.");
	}
	
	@Override
	public void afterRun() throws Exception {
	}
	
	
	
}
