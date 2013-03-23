package dslab03;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.text.html.MinimalHTMLWriter;

public class Office {
	
	private int maxCapacity;
	private final ArrayList<Citizen> branch1 = new ArrayList<Citizen>(); 
	private final ArrayList<Citizen> branch2 = new ArrayList<Citizen>(); 
	private final ArrayList<Citizen> branch3 = new ArrayList<Citizen>(); 
	private final Lock lock = new ReentrantLock(); 
	private final Condition entryPermit = lock.newCondition(); 
	private final Condition leavePermit = lock.newCondition(); 
 
	public Office(int maxCapacity) {
		this.maxCapacity = maxCapacity;
	}
	
	private int personsInOffice() {
		return branch1.size() + branch2.size() + branch3.size();
	}
	
	private ArrayList<Citizen> shortestLine() {
		int[] branches = {branch1.size(), branch2.size(), branch3.size()};
		Arrays.sort(branches);
		int length = branches[0];
		if(length == branch1.size()) {
			return branch1;
		}
		if(length == branch2.size()) {
			return branch2;
		}
		return branch3;
	}
	
	private ArrayList<Citizen> add(Citizen newCitizen) {
		ArrayList<Citizen> branch = shortestLine();
		branch.add(newCitizen);
		return branch;
	}
	
	private void remove(Citizen citizen) {
		try {
			branch1.remove(citizen);
		} catch (Exception e) {
		}
		try {
			branch2.remove(citizen);
		} catch (Exception e) {
		}
		try {
			branch3.remove(citizen);
		} catch (Exception e) {
		}
	}

	public void enterRoom(Citizen newCitizen) throws InterruptedException { 
	    lock.lock(); 
	    try { 
	    	while (personsInOffice() == maxCapacity) { 
	    		System.out.println(newCitizen + " waits to enter the room!");
	    		entryPermit.await(); 
	    	} 
	        add(newCitizen); 
	        System.out.println(newCitizen + " entered the room! Count = " + personsInOffice());
	        leavePermit.signal(); 
	    } finally { 
	    	lock.unlock(); 
	    } 
	} 
	
	public void leaveRoom(Citizen newCitizen) throws InterruptedException {
		lock.lock();
		try {
			while(personsInOffice() == 0) {
	    		System.out.println(newCitizen + " waits to leave the room!");
				leavePermit.await();
			}
			remove(newCitizen);
	        System.out.println(newCitizen + " left the room! Count = " + personsInOffice());
			entryPermit.signal();
		} finally {
			lock.unlock();
		}
	}
}
