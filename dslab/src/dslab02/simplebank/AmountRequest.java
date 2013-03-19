package dslab02.simplebank;

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

	public AmountRequest(double s) throws InterruptedException {
		amount = s;
		sem = new Semaphore(0);
	}

	public double getAmount() {
		return amount;
	}

	public void acquire() throws InterruptedException {
		sem.acquire();
	}

	public void release() {
		sem.release();
	}
}
