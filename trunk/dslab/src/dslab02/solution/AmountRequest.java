package dslab02.solution;

/* ================================================================
 * Distributed Systems, A.A. 2012/2013
 * Computer Science
 * Lab 2
 * Giuseppe Pirro', Valeria Fionda
 * ================================================================
 */


import java.util.concurrent.Semaphore;

public class AmountRequest {
	private double amount;
	private Semaphore sem;
	private int branch;

	public AmountRequest(double s, int f) throws InterruptedException {
		amount = s;
		branch = f;
		sem = new Semaphore(0);
	}

	public double getAmount() {
		return amount;
	}

	public int getBranch() {
		return branch;
	}

	public void acquire() throws InterruptedException {
		sem.acquire();
	}

	public void release() {
		sem.release();
	}
}
