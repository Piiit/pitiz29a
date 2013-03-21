package dslab03;

import java.util.Random;

public class Citizen extends Thread {
	
	private Office office;
	
    public Citizen(Office office) {
		this.office = office;
	}

	public void run() {
    	System.out.println(this + " arrived!");
    	try {
			Thread.sleep((new Random()).nextInt(1000));
			office.enterRoom(this);
			Thread.sleep((new Random()).nextInt(5000));
			office.leaveRoom(this);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }

	@Override
	public String toString() {
		return "Citizen " + getId();
	}
	
	
    
}
