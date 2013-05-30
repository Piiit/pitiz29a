package mybox.server;

import java.net.*;
import java.io.*;

import mybox.io.NetworkTools;

import piwotools.log.Log;

public class FileServer extends Thread {
  
	public void run() {
		
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {
			serverSocket = new ServerSocket(13267);
		    while (true) {
	    		Log.info("FileServer is waiting for new connections...");
	    		socket = serverSocket.accept();
	    		Log.info("Accepted connection: " + socket);
	    		NetworkTools.uploadFileServer(socket, ServerImpl.SERVER_DIR);
	    		if(socket != null) {
					socket.close();
	    			Log.debug(socket + " closed!");
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

