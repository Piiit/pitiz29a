package mybox.io;

import piwotools.log.Log;

public abstract class DelayedInfiniteThread extends Thread {
	
	private static final int ERR_BEFORE_RUN = 0x10;
	private static final int ERR_DURING_RUN = 0x20;
	private static final int ERR_AFTER_RUN = 0x40;
	public static final int DEFAULT_DELAY = 10000;

	private int delay = DEFAULT_DELAY;
	private int runCount = 0;
	
	public abstract void beforeRun() throws Exception;
	public abstract void duringRun() throws Exception;
	public abstract void afterRun() throws Exception;
	
	public DelayedInfiniteThread(int delay) {
		super();
		this.delay = delay;
	}

	public DelayedInfiniteThread() {
		this(DEFAULT_DELAY);
	}

	public void setDelay(int delayInMilliseconds) {
		delay = delayInMilliseconds;
	}
	
	public int getDelay() {
		return delay;
	}
	
	public int getRun() {
		return runCount;
	}

	public void run() {
		
		try {
			beforeRun();
		} catch (Exception e) {
			Log.error("DelayedInfiniteThread: BeforeRun routine exited with errors! " + e.getMessage());
			e.printStackTrace();
			System.exit(ERR_BEFORE_RUN);
		}
		
		try {
			while(true) {
				duringRun();
				sleep(delay);
				runCount++;
			}
		} catch (Exception e) {
			Log.error("DelayedInfiniteThread: DuringRun routine exited with errors! " + e.getMessage());
			e.printStackTrace();
			System.exit(ERR_DURING_RUN);
		}
		
		try {
			afterRun();
		} catch (Exception e) {
			Log.error("DelayedInfiniteThread: AfterRun routine exited with errors! " + e.getMessage());
			e.printStackTrace();
			System.exit(ERR_AFTER_RUN);
		}
	}

}
