package mybox.network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import piwotools.log.Log;

public class NetworkTools {

	public static void uploadFile(String localFilename, String hostname, int port, String remoteFilename) throws Exception {
		PrintWriter output = null;
		File file = new File(localFilename);

		if(!file.exists()) {
			throw new Exception("File " + localFilename + " doesn't exist! Skipping upload...");
		}
		
		Socket socket = new Socket(hostname, port);
		Log.debug("FileClient: Connecting to " + hostname + ":" + port);
		output = new PrintWriter(socket.getOutputStream(), true);
		output.println(remoteFilename + "<<<FILENAME_END>>>");

		Log.info("Uploading file " + localFilename + " to " + hostname + ":" + port + "/" + remoteFilename);

		byte buffer[]  = new byte [4096];
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(fis);
		OutputStream os = socket.getOutputStream();
		int k = -1;
		long size = 0;
		while((k = bis.read(buffer, 0, buffer.length)) > -1) {
			os.write(buffer, 0, k);
			size += k;
		}
		
		Log.debug("Uploading file " + localFilename + " completed! " + size + " bytes send.");
		fis.close();
		bis.close();
		socket.close();
	}
	
	public static void uploadFileServer(Socket socket, String defaultPath) throws Exception {
		long start = System.currentTimeMillis();

		byte[] buffer = new byte[4096];
		int k = -1;
		long size = 0;
		
		InputStream is = socket.getInputStream();
		k = is.read(buffer, 0, buffer.length);
		if(k <= 0) {
			throw new Exception("Invalid filename with length = 0 received!");
		}
	  
		String stringBuffer = new String(buffer);
		String filename = stringBuffer.substring(0, stringBuffer.indexOf("<<<FILENAME_END>>>"));
		int offset = (filename + "<<<FILENAME_END>>>").length() + 1;
				
		Log.info("Receiving file " + filename);
		String path = new File(filename).getParent();
		if(path != null) {
			File folder = new File(defaultPath + path + "/");
			folder.mkdirs();
		}
	
		// receiving file
		FileOutputStream fos = new FileOutputStream(defaultPath + filename);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		bos.write(buffer, offset, k - offset);
		size += k - offset;
		while((k = is.read(buffer, 0, buffer.length)) > -1) {
			bos.write(buffer, 0, k);
			size += k;
		}	
		
		long end = System.currentTimeMillis();
	 	Log.info("File " + filename + " with a size of " + size + " bytes uploaded in " + (end-start)/1000 + " seconds.");

	 	bos.close();
	}
}
