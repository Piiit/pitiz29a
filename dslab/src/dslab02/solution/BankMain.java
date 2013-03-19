package dslab02.solution;

/* ================================================================
 * Distributed Systems, A.A. 2012/2013
 * Computer Science
 * Lab 2
 * Giuseppe Pirro', Valeria Fionda
 * ================================================================
 */


public class BankMain {

	public static void main(String[] args) {
		int numBranch = 5;
		int numClients = 30;
		int numOperations = 5;
		Bank bank = new Bank(numBranch);
		Thread[] clients = new Thread[numClients];
		for (int i = 0; i < numClients; i++) {
			Client s = new Client(bank,
			        (int) (Math.random() * numBranch), numOperations);
			clients[i] = new Thread(s);
			clients[i].start();
		}
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
