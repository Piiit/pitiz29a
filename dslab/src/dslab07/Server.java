package dslab07;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {
	
	private static final int MAXCLIENTS = 10;
	private static final int PORT = 9999;
	private static Vector<ClientHandler> clients = new Vector<ClientHandler>();
	private static Vector<TicTacToe> games = new Vector<TicTacToe>();

	public static void main(String args[]) {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(PORT);
			System.out.println("Starting tic tac toe server...");
			while (true) {  
				try {  
					Socket clientSocket = serverSocket.accept();
					System.out.println("Client likes to connected @ local port " + clientSocket.getLocalPort());
					
					ClientHandler client = new ClientHandler(clientSocket, clients, games, MAXCLIENTS);
					client.start();
					
	            } catch(IOException e) {  
	            	e.getStackTrace();
				}
	         }
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
