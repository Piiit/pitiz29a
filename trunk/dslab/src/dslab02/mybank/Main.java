package dslab02.mybank;

import java.util.LinkedList;

public class Main {

	private static LinkedList<Customer> clients = new LinkedList<Customer>();
	final static int CLIENT_COUNT = 10;
	static Bank bank = new Bank(1000000);
	
	public static void main(String[] args) {
		
		for(int i = 0; i < CLIENT_COUNT; i++) {
			Customer c = new Customer(i, 1000, bank);
			clients.add(c);
			Thread t = new Thread(c);
			t.start();
		}
		
		
	}
}
