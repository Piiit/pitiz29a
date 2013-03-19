package dslab02;

import java.util.Random;

public class Customer implements Runnable {
	
	int id = 0;
	double money = 0;
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
    	
    }

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public int getId() {
		return id;
	}

	public Customer(int id, double money, Bank bank) {
		super();
		this.id = id;
		this.money = money;
		this.bank = bank;
	} 
    
    
}
