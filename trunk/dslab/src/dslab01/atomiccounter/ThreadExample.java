package dslab01.atomiccounter;

import java.util.Random;

public class ThreadExample {

	public static void main(String[] args) {

		Thread t = null;
		
		AtomicCounter ac = new AtomicCounter();
		
		for(int i = 0; i < 20; i++) {
			try {
				t = new SimpleThread(ac);
				t.start();
				Thread.sleep((new Random()).nextInt(500));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(t.toString());
		}
	}

}
