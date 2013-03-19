package dslab01.atomiccounter;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicCounter {
	private AtomicInteger test = new AtomicInteger(0);

	public void inc() {
		test.getAndIncrement();
	}
	
	public int get() {
		return test.get();
	}
}
