package mybox.server;

import java.rmi.*;

public class MyBoxServer { 
	
	public MyBoxServer() throws Exception {
		ServerInterface c = new ServerImpl();
		Naming.rebind("rmi://localhost/MyBoxService", c);
	}

	public static void main(String args[]) {
		try {
			new MyBoxServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
}
