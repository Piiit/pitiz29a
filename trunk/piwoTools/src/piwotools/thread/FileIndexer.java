package piwotools.thread;

import java.io.File;

import piwotools.io.FileTools;
import piwotools.io.FileWalker;
import piwotools.log.Log;

public abstract class FileIndexer extends DelayedInfiniteThread {

	private String directory = "";
	private boolean isFile;
	private String typeString = "[undefined]";
	private File file;
	
	public abstract void onDirectory(String dirname);
	public abstract void onFile(String filename);
	
	public String getDirectory() {
		return directory;
	}
	
	public void setDirectory(String dir) {
		File f = new File(dir);
		directory = f.getAbsolutePath() + "/";
	}
	
	public boolean isFile() {
		return isFile;
	}
	
	public File getFile() {
		return file;
	}
	
	public String getTypeString() {
		return typeString;
	}
	
	@Override
	public void beforeRun() throws Exception {
	}
	
	@Override
	public void duringRun() throws Exception {
		Log.debug("FileIndexer: Directory '" + directory + "': run " + getRun() + " started!");
		
		FileTools.fileWalker(directory, new FileWalker() {

			@Override
			public void onDirectory(String dirname) {
				isFile = false;
				typeString = "directory";
				file = new File(dirname);
				FileIndexer.this.onDirectory(dirname);
			}

			@Override
			public void onFile(String filename) {
				isFile = true;
				typeString = "file";
				file = new File(filename);
				FileIndexer.this.onFile(filename);
			}
			
		});
		
		Log.debug("FileIndexer: Directory '" + directory + "': run " + getRun() + " completed! Next start in " + getDelay()/1000 + " seconds.");
	}
	
	@Override
	public void afterRun() throws Exception {
	}
	
	
	
}
