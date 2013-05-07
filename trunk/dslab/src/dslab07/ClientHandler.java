package dslab07;

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
	private Semaphore gamesPermit = new Semaphore(1);
	
	//Vector is nearly deprecated(?), but works with concurrent access
	private Vector<ClientHandler> clients; 	
	
	private Vector<TicTacToe> games;
	
	private TicTacToe joinedGame;
	
	private Socket socket;
	private BufferedReader input = null;
	private PrintWriter output = null;
	private String name = "";
	private final int maxClients;
	
	public ClientHandler(Socket socket, Vector<ClientHandler> clients, Vector<TicTacToe> games, final int maxClients) {
		this.socket = socket;
		this.clients = clients;
		this.maxClients = maxClients;
		this.games = games;
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
				output.println("Sorry, the tic tac toe server is full (max=" +maxClients+ "). You can not enter now!");
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
		
		boolean done = false;
		String line = "";
		
		name = "Player" + clients.size();
		output.println("Welcome <" + name + ">! Type HELP to see all commands.");
		broadcast("<" + name + "> entered the lobby...");
		
		done = false;
		while(!done) {
			try {
				line = input.readLine();
				done = handleProtocol(line);
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
		broadcast("<" + name + "> left the chat room...");
	}
	
	private boolean handleProtocol(String line) {
		if (line == null) {
			return false;
		}
		line = line.trim();
		
		String regex = "\\s";
		String parts[] = line.split(regex);
		
		String cmd = parts[0].toUpperCase();
		int i = 0;
		String gameName = "";

		switch(cmd) {
			case "QUIT":
				return true;
			case "NEWGAME":
				i = 0;
				gameName = "";
				for(String part: parts) {
					if(i > 0) {
						gameName += part;
					}
					i++;
				}
				try {
					gamesPermit.acquire();
					games.add(new TicTacToe(gameName));
					gamesPermit.release();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			break;
			case "JOIN":
				i = 0;
				gameName = "";
				for(String part: parts) {
					if(i > 0) {
						gameName += part;
					}
					i++;
				}
				
				for(TicTacToe game: games) {
					if(game.getName().equalsIgnoreCase(gameName)) {
						joinedGame = game;
						output.println("You joined the game " + joinedGame.getName());
						break;
					}
				}
				if(joinedGame == null) {
					output.println("You can not join the game " + gameName);
				}
			break;
			case "LIST":
				output.println("--- LIST OF GAMES --------------------");
				i = 0;
				for(TicTacToe game : games) {
					i++;
					output.println(i + " > " + game.getName());
				}
			break;
			case "HELP":
				output.println("--- HELP -----------------------------");
				output.println("QUIT: exit tic tac toe");
				output.println("NEWGAME [gameName]: create a new game");
				output.println("JOIN [gameName]: join a created game");
				output.println("RENAME [name]: enter a new nick name");
				output.println("LIST: list all games");
				output.println("CHAT [text]: broadcasts some chat to all players");
				output.println("--------------------------------------");
			break;
			case "CHAT":
				i = 0;
				String msg = "";
				for(String part: parts) {
					if(i > 0) {
						msg += part;
					}
					i++;
				}
            	broadcast("<" + name + "> " + msg);
            break;
			case "RENAME":
				i = 0;
				String newName = "";
				for(String part: parts) {
					if(i > 0) {
						newName += part;
					}
					i++;
				}
				if (newName.length() == 0) {
					output.println("This name is too short!");
				} else if (nameExists(newName)) {
					output.println("This name exists already. Please choose another one!");
				} else {
					broadcast(name + " changed to " + newName);
					output.println("Your new name is " + newName);
					name = newName;
				}
				
			break;
			default:
				output.println("Please enter a valid command. Type HELP to see all commands!");


		}
		
		return false;
	}
	

	private void broadcast(String text) {
		for(ClientHandler client : clients) {
			if(client != this) {
				client.output.println(text);
			}
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
