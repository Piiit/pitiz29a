package dslab09;

import java.net.MalformedURLException;
import java.rmi.*;

public class ChatClient{
	public static void main(String[] args) {
		try {
			ChatServer c = (ChatServer) Naming.lookup("rmi://localhost/ChatService");
			c.login("JOE","JOE123");
			c.chat("JOE", "Hello from DS");
			
			c.chat("XY", "hallo");
			c.logout("JOE");
		} catch (MalformedURLException | NotBoundException | RemoteException re) { 
			System.out.println("RemoteException"+re);
			re.printStackTrace();
		} 
	}
}