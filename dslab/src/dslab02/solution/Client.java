package dslab02.solution;

/* ================================================================
 * Distributed Systems, A.A. 2012/2013
 * Computer Science
 * Lab 2
 * Giuseppe Pirro', Valeria Fionda
 * ================================================================
 */


public class Client implements Runnable {
	private Bank bank;
	private int idBranch;
	private int numOp;
	private double amount;

	public Client(Bank i, int id, int n) {
		this.bank = i;
		idBranch = id;
		numOp = n;
	}

	public void run() {
		for (int j = 0; j < numOp; j++) {
			try {
				Thread.sleep((long) (Math.random() * 4000 + 1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			amount = Math.random() * 99000 + 1000;
			bank.withdraw(amount, idBranch);
			try {
				Thread.sleep((long) (Math.random() * 4000 + 1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			bank.giveBack(amount, idBranch);
		}
	}

	public int getIdBranch() {
		return idBranch;
	}

	public double getAmount() {
		return amount;
	}

}
