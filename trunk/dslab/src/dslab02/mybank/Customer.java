package dslab02.mybank;

import java.util.Random;

public class Customer implements Runnable {
	
	Bank bank = null;
	
    public void run() {
    	try {
			Thread.sleep((new Random()).nextInt(500));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
    	try {
			bank.takeLoan(100);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	try {
			Thread.sleep((new Random()).nextInt(1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
    	try {
			bank.payBack(100);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    }

	public Customer(Bank bank) {
		this.bank = bank;
	} 
    
    
}
