package dslab03;

import java.util.Random;

public class Main {
	
	private static final int MAXOFFICECAPACITY = 15;
	private static final int BRANCHCAPACITY = 5;
	private static final int EMPLOYEECAPACITY = 1;
	private static final int NUMBEROFCLIENTS = 100;
	

	public static void main(String[] args) throws Exception {

		Office[] employee1 = {new Office(EMPLOYEECAPACITY, "Employee1")};
		Office[] employee2 = {new Office(EMPLOYEECAPACITY, "Employee2")};
		Office[] employee3 = {new Office(EMPLOYEECAPACITY, "Employee3")};
	 	
		Office[] branches = {
				new Office(BRANCHCAPACITY, "Branch1 - Queue", employee1),
				new Office(BRANCHCAPACITY, "Branch2 - Queue", employee2),
				new Office(BRANCHCAPACITY, "Branch3 - Queue", employee3)};
	 	
		Office registryOffice = new Office(MAXOFFICECAPACITY, "Registry Office", branches);
		registryOffice.setPrioritizedQueue(true);
		
		for(int i = 0; i < NUMBEROFCLIENTS; i++) {
			Thread t = new Citizen(registryOffice, i % 2 == 0 ? true : false, (new Random()).nextInt(50) + 18);
			t.start();
		}
	}
}
