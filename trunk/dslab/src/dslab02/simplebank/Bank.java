package dslab02.simplebank;

/* ================================================================
 * Computer Science
 * Distributed Systems, A.A. 2012/2013
 * Lab 2
 * Giuseppe Pirro', Valeria Fionda
 * ================================================================
 */

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.concurrent.Semaphore;

public class Bank {
	private double availableAmount;
	private Semaphore availableAmountMutex;
	private LinkedList<AmountRequest> queue;

	public Bank(int availableAmount) {
		this.availableAmount = availableAmount;
		availableAmountMutex = new Semaphore(1);
		queue = new LinkedList<AmountRequest>();
	}

	public void withdraw(double amount) {
		boolean waited = false;
		try {
			availableAmountMutex.acquire();

			if (amount > availableAmount) {
				AmountRequest loan_request = new AmountRequest(amount);
				queue.add(loan_request);

				// loan not approved
				print("!", amount);
				
				availableAmountMutex.release();
				loan_request.acquire();
				
				waited = true;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// loan approved
		print("-", amount);

		availableAmount -= amount;
		/* 
		 * If I have waited for a not available amount it means that a certain amount has been
		 * given back to the bank.
		 * 
		 * I check id there is someone in the queue that can be eligible for the loan:
		 * if so I pass him the mutex. 
		 */
		if (!waited || !clientWaiting()) {
			availableAmountMutex.release();
		}
	}

	public void giveBack(double amount) {
		try {
			availableAmountMutex.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// loan returned
		print("+", amount);

		availableAmount += amount;
		/* 
		 * I verify if there is some client waiting that can be eligible for the loan:
		 * if so I pass him the mutex.
		 */
		if (!clientWaiting()) {
			availableAmountMutex.release();
		}
	}

	/**
	 * If there exists a client waiting for which the required amount is now available
	 * and the loan does not violate the constraint over the number of loans per branch
	 * then remove the client from the waiting list, "wake him up" and return true.
	 */
	private boolean clientWaiting() {
		if (queue.size() > 0) {
			ListIterator<AmountRequest> it = queue.listIterator();
			while (it.hasNext()) {
				AmountRequest a = it.next();
				if (a.getAmount() <= availableAmount) {
					it.remove();
					a.release();
					return true;
				}
			}
		}
		return false;
	}

	private void print(String s, double amount) {
		System.out.printf("available_amount:%10.2f " + s + " req_amount:%10.2f\n", availableAmount, amount);
	}

	public double getAvailableAmount() {
		double x = 0;
		try {
			availableAmountMutex.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		x = availableAmount;
		availableAmountMutex.release();
		return x;
	}

}
