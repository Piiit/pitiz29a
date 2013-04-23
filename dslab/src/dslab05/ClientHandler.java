package dslab05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.Semaphore;

public class ClientHandler extends Thread {
	
	//vector should do the job, but just to be sure, I use semaphores.
	private Semaphore clientsPermit = new Semaphore(1); 
	
	//Vector is nearly deprecated(?), but works with concurrent access
	private Vector<ClientHandler> clients; 	
	
	private Socket socket;
	private BufferedReader input = null;
	private PrintWriter output = null;
	private String name = "";
	private final int maxClients;
	
	public ClientHandler(Socket socket, Vector<ClientHandler> clients, final int maxClients) {
		this.socket = socket;
		this.clients = clients;
		this.maxClients = maxClients;
	}
	
	public void run() {
		System.out.println("Client @ port " + socket.getPort());
		try {
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			if(clients.size() == maxClients) {
				output.println("!!! Sorry, the chat room is full (" +maxClients+ "). You can not enter now!");
				socket.close();
				return;
			} 
			clientsPermit.acquire();
			clients.add(this);
			clientsPermit.release();
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		output.println("!!! What's your name?");
		boolean done = false;
		String line = "";
		do {
			try {
				line = input.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (line.length() == 0) {
				output.println("This name is too short!");
			} else if (nameExists(line)) {
				output.println("This name exists already. Please choose another one!");
			} else {
				done = true;
			}
		} while (!done);
		name = line;
		
		output.println("!!! Welcome <" + name + ">! You can exit this chat with /quit");
		broadcast("!!! <" + name + "> entered the chat room...");
		
		done = false;
		while(!done) {
			try {
				line = input.readLine();
				done = (line == null || line.equals("/quit"));
	            if(!done && line.trim().length() > 0) {
	            	broadcast("<" + name + "> " + line);
	            }
			} catch (IOException e) {
			}
		}	

		try {
			socket.close();
			clientsPermit.acquire();
			clients.remove(this);
			clientsPermit.release();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Client <" + name + "> @ port " + socket.getPort() + " left the room!");
		output.println("*** Bye " + name + "***");
		broadcast("!!! <" + name + "> left the chat room...");
	}
	
	private void broadcast(String text) {
		for(ClientHandler client : clients) {
			client.output.println(text);
		}
	}
	
	private boolean nameExists(String name) {
		for(ClientHandler client : clients) {
			if(name.equalsIgnoreCase(client.name)) {
				return true;
			}
		}
		return false;
	}

}
