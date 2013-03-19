package dslab02.simplebank;

import java.util.Random;

/* ================================================================
 * Distributed Systems, A.A. 2012/2013
 * Computer Science
 * Lab 2
 * Giuseppe Pirro', Valeria Fionda
 * ================================================================
 */


public class Client implements Runnable {
	private Bank bank;
	private double amount;

	public Client(Bank i) {
		this.bank = i;
	}

	public void run() {
		amount = 100000;
		bank.withdraw(amount);
		try {
			Thread.sleep((new Random()).nextInt(500));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		bank.giveBack(amount);
	}
}
