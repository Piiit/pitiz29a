package dslab02.solution;

/* ================================================================
 * Distributed Systems, A.A. 2012/2013
 * Computer Science
 * Lab 2
 * Giuseppe Pirro', Valeria Fionda
 * ================================================================
 */


import java.util.LinkedList;
import java.util.ListIterator;
import java.util.concurrent.Semaphore;

public class Bank {
	private double availableAmount;
	private final double maxAmount = 1000000;
	private final int maxClientsPerBranch = 5;
	private Semaphore mutex;
	private LinkedList<AmountRequest> queue;
	private int[] loansPerBranch;

	public Bank(int numberBranch) {
		availableAmount = maxAmount;
		mutex = new Semaphore(1);
		queue = new LinkedList<AmountRequest>();
		loansPerBranch = new int[numberBranch];
		for (int i = 0; i < loansPerBranch.length; i++) {
			loansPerBranch[i] = 0;
		}
	}

	public void withdraw(double amount, int idBranch) {
		boolean waited = false;
		try {
			mutex.acquire();

			if (amount > availableAmount
			        || loansPerBranch[idBranch] == maxClientsPerBranch) {
				AmountRequest loan_request = new AmountRequest(amount, idBranch);
				queue.add(loan_request);

				// loan not approved
				print("!", amount, idBranch);

				
				mutex.release();
				loan_request.acquire();
				
				/* 
				 * Who gave me the right to proceed
				 * has not released the mutex, thus, I am the only one
				 * that can execute, I have the mutex.  
				 */
				waited = true;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// loan approved
		print("-", amount,idBranch);

		loansPerBranch[idBranch]++;
		availableAmount -= amount;
		/* 
		 * If I have waited for a not available amount it means that a certain amount has been
		 * given back to the bank.
		 * 
		 * I check id there is someone in the queue that can be eligible for the loan:
		 * if so I pass him the mutex. 
		 */
		if (!waited || !clientWaiting()) {
			mutex.release();
		}
	}

	public void giveBack(double amount, int idBranch) {
		try {
			mutex.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// loan returned
		print("+", amount, idBranch);

		loansPerBranch[idBranch]--;
		availableAmount += amount;
		/* 
		 * I verify if there is some client waiting that can be eligible for the loan:
		 * if so I pass him the mutex.
		 */
		if (!clientWaiting()) {
			mutex.release();
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
				if (a.getAmount() <= availableAmount
				        && loansPerBranch[a.getBranch()] < maxClientsPerBranch) {
					it.remove();
					a.release();
					return true;
				}
			}
		}
		return false;
	}

	private void print(String s, double amount, int idBranch) {
		System.out.printf("available_amount:%10.2f " + s + " req_amount:%10.2f ( ", availableAmount, amount);
		for (int i = 0; i < loansPerBranch.length; i++) {
			System.out.print("#b" + i + "=" + loansPerBranch[i] + " ");
		}
		System.out.printf(") branch=%d\n",idBranch);
	}

	public double getMaxAmount() {
		return maxAmount;
	}

	public double getAvailableAmount() {
		double x = 0;
		try {
			mutex.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		x = availableAmount;
		mutex.release();
		return x;
	}

}
