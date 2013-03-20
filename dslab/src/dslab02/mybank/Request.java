package dslab02.mybank;

import java.util.concurrent.Semaphore;

public class Request {
	double amount;
	Semaphore amountMutex;
	
	public Request(double amount) {
		this.amount = amount;
		amountMutex = new Semaphore(0);
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public void acquire() throws InterruptedException {
		amountMutex.acquire();
	}
	
	public void release() {
		amountMutex.release();
	}

}
