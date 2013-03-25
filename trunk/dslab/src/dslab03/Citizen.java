package dslab03;

import java.util.Random;

public class Citizen extends Thread implements Comparable<Citizen> {
	
	private Office office;
	private boolean isMale;
	private int age = 30;
	
    public Citizen(Office office, boolean isMale, int age) {
		this.office = office;
		this.isMale = isMale;
		this.age = age;
	}

	public void run() {
    	System.out.println(this + " arrived!");
    	try {
			Thread.sleep((new Random()).nextInt(1000));
			office.enterRoom(this);
			Thread.sleep((new Random()).nextInt(2000) + 1000);
			office.leaveRoom(this);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }

	@Override
	public String toString() {
		return age + " year old " + (isMale ? "male" : "female") + " citizen with id " + getId();
	}

	@Override
	public int compareTo(Citizen o) {
		if(age < o.age) {
			return -1;
		} 
		if(age > o.age) {
			return 1;
		}
		return 0;
	}
	
	
    
}
