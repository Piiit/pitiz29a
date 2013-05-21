package mybox.client;


import java.io.File;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;

import mybox.io.FileChunk;
import mybox.server.ServerImpl;
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
	
	private static final String clientDir = "/Users/user/mybox/";
	
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
		System.out.println(" - Available commands: get, put, status");
		System.out.println(" - Available options:");
		System.out.println("   -s server address (default = localhost)");
		System.out.println("   -h mybox home directory (default = ~/mybox)");
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
		   	File file = new File(clientDir + filename);
		   	if(file.exists()) {
		   		FileChunk chunk = new FileChunk(clientDir, filename);
		   		chunk.read();
		   		server.receiveFile(chunk);
		   	} else {
		   		System.out.println("File " + clientDir + filename + " doesn't exist!");
		   		System.exit(-2);
		   	}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

}
