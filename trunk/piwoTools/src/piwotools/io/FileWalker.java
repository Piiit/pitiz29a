package piwotools.io;

public interface FileWalker {
	public void onDirectory(final String dir);
	public void onFile(final String file);
}
