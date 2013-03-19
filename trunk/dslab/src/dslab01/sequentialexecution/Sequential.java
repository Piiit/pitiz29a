package dslab01.sequentialexecution;

public class Sequential {

	public static void main(String[] args) {
		Count c1 = new Count(0, 10);
		Count c2 = new Count(100, 110);
		
		Thread t1 = new Thread(c1);
		Thread t2 = new Thread(c2);
		Thread t3 = new Thread(new Count(11, 20));
		
		t1.start();
		try {
			/*
			 * The calling thread (in this case the main-thread) waits until t1 has finished...
			 */
			t1.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		t2.start();
		try {
			t2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		t3.start();
		try {
			t3.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Last line...");
	}

}
