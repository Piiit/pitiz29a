package dslab02.simplebank;

/* ================================================================
 * Distributed Systems, A.A. 2012/2013
 * Computer Science
 * Lab 2
 * Giuseppe Pirro', Valeria Fionda
 * ================================================================
 */

public class BankMain {

	public static void main(String[] args) {
		int numClients = 30;
		Bank bank = new Bank(1000000);
		Thread[] clients = new Thread[numClients];
		for (int i = 0; i < numClients; i++) {
			Client s = new Client(bank);
			clients[i] = new Thread(s);
			clients[i].start();
		}
		
		// Join all clients, so that the main-thread continuous only if all client-threads have been terminated!
		try {
			for (int i = 0; i < numClients; i++) {
				clients[i].join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.printf("Final Amount = %8.2f", bank.getAvailableAmount());
	}
}
