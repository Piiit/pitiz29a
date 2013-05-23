package mybox.client;


import java.io.File;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import mybox.io.RMIFileTools;
import mybox.server.ServerInterface;

/**
 * Scans a directory asynchronously and notifies if something has changed.
 * Creates a log that shows when something has been changed.
 * States can be: DELETED, MODIFIED, MOVED
 * Deleted files should be saved somewhere to enable a restore.
 * Log entries: TIME, STATE, FILE
 * Commands: COMMIT CHECKOUT
 * 
 * First iteration:
 * Scans two directories and synchronizes them. 
 * Copies every file that is not in conflict.
 * Conflicts are handled by renaming files and
 * writing log entries.
 * 
 * Second iteration:
 * Synch over network
 * 
 * Third iteration:
 * 
 * 
 * @author pemoser
 *
 */


public class MyBoxClient {
	
	private static final String CLIENT_DIR = "/Users/user/mybox/";
	
	public static void directory(File dir) throws IOException{
	    File[] files = dir.listFiles();
	    for(File file:files){
	        System.out.println(file.getCanonicalPath());
	        if(file.listFiles() != null)
	            directory(file);        
	    }
	} 
	
	private static void usageInfo() {
		System.out.println("Usage: java MyBoxClient <options> <command> <file>");
		System.out.println(" - Available commands: sync, status");
		System.out.println(" - Available options:");
		System.out.println("   -s server address (default = localhost)");
		System.out.println("   -h mybox home directory (default = ~/mybox)");
	}
	
	public static void listFilesForFolder(final File folder, RMIFileTools rmiFT) throws Exception {
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry, rmiFT);
	        } else {
	        	String myBoxDir = folder.getCanonicalPath().substring(CLIENT_DIR.length()-1);
	        	if(myBoxDir.length() > 0) {
	        		myBoxDir += "/";
	        	}
	        	
	            System.out.println(myBoxDir + fileEntry.getName());
	            rmiFT.upload(myBoxDir + fileEntry.getName());
	        }
	    }
	}
	
	public static void main(String[] args) throws NotBoundException {	
		
		try {
			
			if(args.length == 1 && args[0].equalsIgnoreCase("help")) {
				usageInfo();
				System.exit(0);
			}
			
			if(args.length != 3) {
				System.out.println("Type 'java MyBoxClient help' for usage.");
				System.exit(-1);
			}
			String serverName = args[0];
			String command = args[1];
			String filename = args[2];
			
			System.out.println("Welcome to myBox!");
			System.out.println("...connecting to " + serverName);
		   	ServerInterface server = (ServerInterface)Naming.lookup("//" + serverName + "/MyBoxService");
		   	System.out.println("...connected!");
		   	
		   	System.out.println("Command: " + command + " " + filename);
		   	
		   	RMIFileTools rmiFT = new RMIFileTools(server);
		   	rmiFT.setClientFolder(CLIENT_DIR);

		   	listFilesForFolder(new File(CLIENT_DIR), rmiFT);
		   	
		   	
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

}
