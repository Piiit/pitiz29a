package dslab05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	
	private static PrintWriter output = null;
	private static BufferedReader input = null;
	private static Socket socket = null;
	private static final int PORT = 9999;
	private static final String ADDRESS = "localhost";
	
	public static void main(String args[]) {
		try {
			socket = new Socket(ADDRESS, PORT);
			try {
				output = new PrintWriter(socket.getOutputStream(), true);
				input = new BufferedReader(new InputStreamReader(System.in));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			ClientOutput clientOut = new ClientOutput(socket);
			clientOut.start();
			
			String line = "";
			boolean done = false;
			while(!done && !socket.isClosed() && line != null) {
				try {
					line = input.readLine();
					output.println(line);
					done = line.equals("/quit");
				} catch (IOException e) {
					e.getStackTrace();
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				System.out.println("Connection closed by foreign host!");
				output.close();
				input.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
		
	}

}
