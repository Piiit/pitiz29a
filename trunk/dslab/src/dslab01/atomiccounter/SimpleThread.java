package dslab01.atomiccounter;

import java.util.Random;

public class SimpleThread extends Thread {
	
	private AtomicCounter test = null;
	
	public SimpleThread(AtomicCounter ac) {
		test = ac;
	}
	
	public void run() {
		test.inc();
		try {
			Thread.sleep((new Random()).nextInt(500));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public String toString() {
		return(this.getId() + "; already executed threads: "+ test.get() + "; state = " + getState() );
	}
	
	
}
