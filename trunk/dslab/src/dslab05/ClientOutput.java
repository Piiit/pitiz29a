package dslab05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientOutput extends Thread {
	
	private BufferedReader input = null;
	private Socket socket = null;
	
	public ClientOutput(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	    String line = "";
	    try {
	    	while (!socket.isClosed()) {
	    		line = input.readLine();
	    		if(line != null) {
	    			System.out.println(line);
	    		}
	    	}
	    } catch (IOException e) {
	    	System.out.println("Closing client output.");
	    }
		try {
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
}
