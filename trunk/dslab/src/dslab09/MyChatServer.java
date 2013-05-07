package dslab09;

import java.rmi.*;

public class MyChatServer { 
	
	public MyChatServer() throws Exception {
		ChatServer c = new ChatServerImpl();
		Naming.rebind("rmi://localhost/ChatService", c);
	}

	public static void main(String args[]) {
		try {
			new MyChatServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
}
