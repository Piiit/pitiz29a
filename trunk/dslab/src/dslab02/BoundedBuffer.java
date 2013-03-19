package dslab02;

import java.util.concurrent.Semaphore;

public class BoundedBuffer {
	
	private int pos = 0;
	private final Object[] items;
	private final Semaphore putPermits;
	private final Semaphore takePermits;
	
	public BoundedBuffer(int bufferSize) {
		items = new Object[bufferSize];
		putPermits = new Semaphore(bufferSize);
		takePermits = new Semaphore(0);
	}
	
	public void put(Object item) throws InterruptedException {
		putPermits.acquire();
		
		items[pos] = item;
		pos = (pos+1) % items.length;
		
		takePermits.release();
	}
	
	public Object take() throws InterruptedException {
		takePermits.acquire();

		pos = (pos-1) % items.length;
		Object item = items[pos];
		items[pos] = null;
		
		putPermits.release();
		return item;
	}
	
	

}
