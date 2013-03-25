package dslab03;

import java.util.Random;

public class Citizen extends Thread implements Comparable<Citizen> {
	
	private Office office;
	private boolean isMale;
	private int age;
	
    public Citizen(Office office, boolean isMale, int age) {
		this.office = office;
		this.isMale = isMale;
		this.age = age;
	}

	public void run() {
    	System.out.println(this + " arrived!");
    	try {
			Thread.sleep((new Random()).nextInt(100));
			office.enterRoom(this);
			Thread.sleep((new Random()).nextInt(100));
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
		if(isMale) {
			if(o.isMale) {
				if(age > 60 && o.age <= 60) {
					return 1;
				} 
				if(age <= 60 && o.age > 60) {
					return -1;
				}
			} else {
				if(age > 60 && o.age <= 50) {
					return 1;
				} 
				if(o.age > 50) {
					return -1;
				}
			}
		} else {
			if(o.isMale) {
				if(age > 50) {
					return 1;
				} 
				if(age <= 50 && o.age > 60) {
					return -1;
				}
			} else {
				if(age > 50 && o.age <= 50) {
					return 1;
				} 
				if(age <= 50 && o.age > 50) {
					return -1;
				}
			}
		}
		return 0;
	}
	
	
    
}
