package dslab09;

import java.rmi.*;

public interface ChatServer extends Remote {
	public void login(String name, String password) throws RemoteException;
	public void logout(String name) throws RemoteException;
	public void chat(String name, String message) throws RemoteException;
}