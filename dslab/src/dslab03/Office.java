package dslab03;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Office {
	
	private int maxCapacity;
	private final ArrayList<Citizen> personsInRoom = new ArrayList<Citizen>(); 
	private final Lock lock = new ReentrantLock(); 
	private final Condition entryPermit = lock.newCondition(); 
	private final Condition leavePermit = lock.newCondition(); 
 
	public Office(int maxCapacity) {
		this.maxCapacity = maxCapacity;
	}

	public void enterRoom(Citizen newCitizen) throws InterruptedException { 
	    lock.lock(); 
	    try { 
	    	while (personsInRoom.size() == maxCapacity) { 
	    		System.out.println(newCitizen + " waits to enter the room!");
	    		entryPermit.await(); 
	    	} 
	        personsInRoom.add(newCitizen); 
	        System.out.println(newCitizen + " entered the room! Count = " + personsInRoom.size());
	        leavePermit.signal(); 
	    } finally { 
	    	lock.unlock(); 
	    } 
	} 
	
	public void leaveRoom(Citizen newCitizen) throws InterruptedException {
		lock.lock();
		try {
			while(personsInRoom.size() == 0) {
	    		System.out.println(newCitizen + " waits to leave the room!");
				leavePermit.await();
			}
			personsInRoom.remove(newCitizen);
	        System.out.println(newCitizen + " left the room! Count = " + personsInRoom.size());
			entryPermit.signal();
		} finally {
			lock.unlock();
		}
	}
}
