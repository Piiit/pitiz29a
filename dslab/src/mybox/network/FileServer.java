package mybox.network;

import java.net.*;
import java.io.*;
import piwotools.log.Log;

public class FileServer extends Thread {
	
	private String directory;
	private int port;
  
	public FileServer(String directory, int port) {
		super();
		this.directory = directory;
		this.port = port;
	}

	public void run() {
		
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {
			serverSocket = new ServerSocket(port);
		    while (true) {
	    		Log.debug("FileServer: Waiting for new connections...");
	    		socket = serverSocket.accept();
	    		Log.debug("FileServer: Accepted connection: " + socket);
	    		NetworkTools.fileServer(socket, directory);
	    		if(socket != null) {
					socket.close();
	    			Log.debug("FileServer: " + socket + " closed!");
				}
		    }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
			if(serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}

