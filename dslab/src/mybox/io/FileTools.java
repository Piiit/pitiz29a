package mybox.io;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import mybox.log.Log;

public class FileTools {
	
	// Source: http://www.mkyong.com
	public static String createSHA1checksum(String filename) throws NoSuchAlgorithmException, IOException {
		 
	    MessageDigest md = MessageDigest.getInstance("SHA1");
	    FileInputStream fis = new FileInputStream(filename);
	    byte[] dataBytes = new byte[1024];
	 
	    int nread = 0; 
	 
	    try {
			while ((nread = fis.read(dataBytes)) != -1) {
				md.update(dataBytes, 0, nread);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(fis != null) {
				fis.close();
			}
		}
	 
	    byte[] mdbytes = md.digest();
	 
	    //convert the byte to hex format
	    StringBuffer sb = new StringBuffer("");
	    for (int i = 0; i < mdbytes.length; i++) {
	    	sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
	    }
	 
	    Log.debug(filename + "; checksum = " + sb.toString());
	 
	    return sb.toString();
	}
}
