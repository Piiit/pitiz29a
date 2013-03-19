package dslab02;

import java.util.concurrent.Semaphore;

public class Bank {
	double money;
	Semaphore moneyMutex = new Semaphore(1);
	BoundedBuffer queue = new BoundedBuffer(3);
	
	public Bank(double money) {
		super();
		this.money = money;
	}

	public void takeLoan(double amount) throws Exception {
		moneyMutex.acquire();
		queue.put(amount);
		
		
	}
	
	public double getMoney() {
		return money;
	}
	
	
}
