package dslab03;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Office {
	
	private int maxCapacity;
	private String name;
	private final ArrayList<Citizen> citizen = new ArrayList<Citizen>();
	private Office[] branches = null;
	private final Lock lock = new ReentrantLock(); 
	private final Condition entryPermit = lock.newCondition(); 
	private final Condition leavePermit = lock.newCondition();
	private boolean isPrioritizedQueue = false; 
 
	public Office(int maxCapacity, String name, Office[] branches) throws Exception {
		this.maxCapacity = maxCapacity;
		this.branches = new Office[branches.length];
		for(int i = 0; i < branches.length; i++) {
			this.branches[i] = branches[i];
		}
		this.name = name;
	}

	public Office(int maxCapacity, String name) {
		this.maxCapacity = maxCapacity;
		this.name = name;
	}
	
	public void setPrioritizedQueue(boolean pr) {
		isPrioritizedQueue = pr;
	}

	public int countCitizen() {
		return citizen.size();
	}
	
	private int shortestLineId() {
		if(branches == null) {
			return -1;
		}
		int shortest = 0;
		for(int i = 1; i < branches.length; i++) {
			if(branches[i].countCitizen() < branches[i-1].countCitizen()) {
				shortest = i;
			}
		}
		return shortest;
	}
	
	public boolean hasCitizen(Citizen c) {
		return citizen.contains(c);
	}
	
	public boolean canEnter(Citizen c) {
		if(isPrioritizedQueue ) {
			Collections.sort(citizen);
			if(citizen != null && citizen.size() > 0 && citizen.get(0).compareTo(c) < 0) {
				System.out.println(c + " has the hightest priority in the waiting queue for " + name);
				return true;
			}
		}
		if(countCitizen() < maxCapacity) {
			return true;
		}
		return false;
	}
	
	public void enterRoom(Citizen c) throws InterruptedException { 
	    lock.lock(); 
	    try { 
	    	while (!canEnter(c)) { 
	    		System.out.println(c + " waits to enter " + name);
	    		entryPermit.await(); 
	    	} 
	        citizen.add(c); 
	        System.out.println(c + " entered " + name + "; Count = " + countCitizen());
	        leavePermit.signalAll(); 
	    } finally { 
	    	lock.unlock(); 
	    } 
        if(branches != null) {
        	branches[shortestLineId()].enterRoom(c);
        }
	} 
	
	public void leaveRoom(Citizen c) throws InterruptedException {
		if(branches != null) {
			int i = 0;
			while(i < branches.length && !branches[i].hasCitizen(c)) {
				i++;
			}
			branches[i].leaveRoom(c);
		}

		lock.lock();
		try {
			while(countCitizen() == 0) {
	    		System.out.println(c + " waits to leave " + name);
				leavePermit.await();
			}
			citizen.remove(c);
	        System.out.println(c + " left " + name + "; Count = " + countCitizen());
			entryPermit.signalAll();
		} finally {
			lock.unlock();
		}
	}
}
