package dslab01.sequentialexecution;

import java.util.Random;

public class Count implements Runnable {

	private int start = 0;
	private int end = 0;
	
	public Count(int start, int end) {
		this.start = start;
		this.end = end;
	}
	
	@Override
	public void run() {
		for(int i = start; i < end; i++) {
			System.out.println(start + i);
			try {
				Thread.sleep((new Random()).nextInt(200));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
