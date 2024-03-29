package piwotools.network;

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
	
	private static final int BUFFER_SIZE = 8192;
	private static final String HEADER_FILENAME_END = "<<<FILENAME_END>>>";
	private static final String HEADER_UPLOAD_CHAR = "U";
	private static final String HEADER_DOWNLOAD_CHAR = "D";

	public static String createHeader(boolean isUpload, String remoteFilename) {
		String header = isUpload ? HEADER_UPLOAD_CHAR : HEADER_DOWNLOAD_CHAR;
		header += remoteFilename + HEADER_FILENAME_END;
		return header;
	}
	
	public static void uploadFile(String localFilename, String hostname, int port, String remoteFilename) throws Exception {
		PrintWriter output = null;
		File file = new File(localFilename);

		if(!file.exists()) {
			throw new Exception("NetworkTools: File " + localFilename + " doesn't exist! Skipping upload...");
		}
		
		Socket socket = new Socket(hostname, port);
		Log.debug("NetworkTools: Connecting to " + hostname + ":" + port);
		
		output = new PrintWriter(socket.getOutputStream(), true);
		output.println(createHeader(true, remoteFilename));

		Log.info("NetworkTools: Uploading file " + localFilename + " to " + hostname + ":" + port + "/" + remoteFilename);

		byte buffer[]  = new byte [BUFFER_SIZE];
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(fis);
		OutputStream os = socket.getOutputStream();
		int k = -1;
		long size = 0;
		while((k = bis.read(buffer, 0, buffer.length)) > -1) {
			os.write(buffer, 0, k);
			size += k;
		}
		
		fis.close();
		bis.close();
		Log.info("NetworkTools: Uploading file " + localFilename + " completed! " + size + " bytes send.");
		socket.close();
	}
	
	public static void downloadFile(String localFilename, String hostname, int port, String remoteFilename, String defaultPath) throws Exception {
		PrintWriter output = null;
		File file = new File(localFilename);

		Socket socket = new Socket(hostname, port);
		Log.debug("NetworkTools: Connecting to " + hostname + ":" + port);
		
		output = new PrintWriter(socket.getOutputStream(), true);
		output.println(createHeader(false, remoteFilename));
		output.flush();

		Log.info("NetworkTools: Downloading file from " + hostname + ":" + port + "/" + remoteFilename + " to " + localFilename);
		
		String path = file.getParent();
		if(path != null) {
			File folder = new File(path + "/");
			folder.mkdirs();
		}
		
		byte[] buffer = new byte[BUFFER_SIZE];
		int k = -1;
		long size = 0;
	
		// receiving file
		InputStream is = socket.getInputStream();
		FileOutputStream fos = new FileOutputStream(localFilename);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		while((k = is.read(buffer, 0, buffer.length)) > -1) {
			bos.write(buffer, 0, k);
			size += k;
		}

		bos.flush();
		fos.close();
	 	bos.close();
	 	socket.close();

	 	Log.info("NetworkTools: File " + localFilename + " with a size of " + size + " bytes downloaded.");
		
	}
	
	public static void fileServer(Socket socket, String defaultPath) throws Exception {
		long start = System.currentTimeMillis();

		byte[] buffer = new byte[BUFFER_SIZE];
		int k = -1;
		long size = 0;
		
		InputStream is = socket.getInputStream();
		k = is.read(buffer, 0, buffer.length);
		if(k <= 0) {
			throw new Exception("NetworkTools: Invalid filename with length = 0 received!");
		}
	  
		String stringBuffer = new String(buffer);
		boolean isClientUpload = stringBuffer.substring(0, 1).equalsIgnoreCase(HEADER_UPLOAD_CHAR);
		String filename = stringBuffer.substring(1, stringBuffer.indexOf(HEADER_FILENAME_END));
		int offset = (filename + HEADER_FILENAME_END).length() + 2;
		
		if(isClientUpload) {
			
			Log.info("NetworkTools: Receiving file " + filename);
			File file = new File(filename);
			String path = file.getParent();
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
	
			bos.flush();
			fos.close();
		 	bos.close();
		 	
		} else {
			File file = new File(defaultPath + "/" + filename);
			filename = file.getCanonicalPath();
			Log.info("NetworkTools: Sending file " + filename);

			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			OutputStream os = socket.getOutputStream();
			k = -1;
			size = 0;
			while((k = bis.read(buffer, 0, buffer.length)) > -1) {
				os.write(buffer, 0, k);
				size += k;
			}
			
			fis.close();
			bis.close();

		}
		
		long end = System.currentTimeMillis();
	 	Log.info("NetworkTools: File " + filename + " with a size of " + size + " bytes uploaded in " + (end-start)/1000 + " seconds.");

	}
}
