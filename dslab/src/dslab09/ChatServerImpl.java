package dslab09;

import java.rmi.*;
import java.rmi.server.*;
import java.util.HashMap;
import java.util.Map;

public class ChatServerImpl extends UnicastRemoteObject implements ChatServer {

	private static final long serialVersionUID = 1L;
	private Map<String, String> users;
	private Map<String, String> logins;
	
	public ChatServerImpl() throws RemoteException { 
		super();
		users = new HashMap<String, String>();
		logins = new HashMap<String, String>();
		users.put("JOE", "JOE123");
	}
	
	public void login(String name, String pass) throws RemoteException {
		if(!users.containsKey(name)) {
			throw new RemoteException("User with name <" + name + "> does not exist.");
		} 
		if(logins.containsKey(name)) {
			throw new RemoteException("User with name <" + name + "> is already logged in.");
		}
		if(!users.get(name).equals(pass)) {
			throw new RemoteException("Wrong password!");
		}

		logins.put(name, pass);
		System.out.format("Login: <%s>", name);
	}
	
	public void logout(String name) throws RemoteException { 
		if(!users.containsKey(name)) {
			throw new RemoteException("User with name <" + name + "> is not registered.");
		}
		if(!logins.containsKey(name)) {
			throw new RemoteException("User with name <" + name + "> is not logged in.");
		}
		logins.remove(name);
		System.out.format("Logout: <%s>", name);
	}
	
	public void chat(String name, String msg) throws RemoteException{
		if(!users.containsKey(name)) {
			throw new RemoteException("User with name <" + name + "> is not registered.");
		}
		if(!logins.containsKey(name)) {
			throw new RemoteException("User with name <" + name + "> is not logged in.");
		}
		System.out.format("<%s> %s", name, msg);
	}
}