package dslab02.mybank;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Bank {
	double money;
	Semaphore moneyMutex = new Semaphore(1);
	ArrayList<Request> queue = new ArrayList<Request>();
	Semaphore queueMutex = new Semaphore(1);
	
	public Bank(double money) {
		this.money = money;
	}

	public void takeLoan(double amount) throws InterruptedException {
		moneyMutex.acquire();
		if(amount <= money) {
			money -= amount;
			System.out.println(" > takeLoan: " + amount + "; money = " + money);
			moneyMutex.release();
		} else {
			System.out.println(" > takeLoan: " + amount + " (not enough money = " + money + ")");
			moneyMutex.release();

			Request r = new Request(amount);
			queue.add(r);
			r.acquire();

			moneyMutex.acquire();
			money -= amount;
			moneyMutex.release();
			
			System.out.println(" > takeLoan: " + amount + "; money = " + money);
		}
		
		
	}
	
	public void payBack(double amount) throws InterruptedException {
		moneyMutex.acquire();
		
		money += amount;
		System.out.println(" > payBack:  " + amount + "; money = " + money);
		
		processQueue();
		
		moneyMutex.release();
	}
	
	public void processQueue() throws InterruptedException {
		for(Request r : queue) {
			if(r.amount <= money) {
				System.out.println("Releasing a waiting thread...");
				queue.remove(r);
				r.release();
				return;
			} 
		}
	}
	
	public double getMoney() {
		return money;
	}
	
	
}
