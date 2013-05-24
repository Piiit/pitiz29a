package mybox.server;


import java.rmi.*;
import java.rmi.server.ServerNotActiveException;

import mybox.io.FileChunk;

public interface ServerInterface extends Remote {
	public void register(String name) throws RemoteException;
	public void status() throws RemoteException;
	public void receiveFile(FileChunk chunk) throws RemoteException;
	public void sayHello() throws RemoteException, ServerNotActiveException;
}
