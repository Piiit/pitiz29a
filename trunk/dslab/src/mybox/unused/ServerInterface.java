package mybox.unused;

import java.rmi.*;
import java.rmi.server.ServerNotActiveException;

public interface ServerInterface extends Remote {
	public void register(String name) throws RemoteException;
	public void status() throws RemoteException;
	public void receiveFile(FileChunk chunk) throws RemoteException;
	public void sayHello() throws RemoteException, ServerNotActiveException;
	public void createFolder(String folder) throws RemoteException;
//	public void removeFile(FileChunk chunk)
}
