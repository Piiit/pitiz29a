package lab02;

import tools.ArrayUtility;

public class DsaLab02 {

	private final static int REPEATS = 10;
	private final static int POWEROF = 4;
	
	public static void main(String[] args) {
		int[] A;
		double[] estimates = new double[REPEATS];
		try {
			for(int i = 0; i <= POWEROF; i++) {
				System.out.print("maxSortWithShift: No. of elements = " + (int)Math.pow(10, i));
				for(int times = 0; times < REPEATS; times++) {
					A = ArrayUtility.createRandomArray((int)Math.pow(10, i), 0, 1000);
					long startTime = System.nanoTime();
					ArrayUtility.maxSortWithShift(A);
				    long estimatedTime = System.nanoTime() - startTime;
				    estimates[times] = estimatedTime;
				    System.out.print(".");
				}
				System.out.println("done in " + ArrayUtility.median(estimates) + " nanoseconds (median).");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}	

		try {
			for(int i = 0; i <= POWEROF; i++) {
				System.out.print("maxSortWithSwap: No. of elements = " + (int)Math.pow(10, i));
				for(int times = 0; times < REPEATS; times++) {
					A = ArrayUtility.createRandomArray((int)Math.pow(10, i), 0, 1000);
					long startTime = System.nanoTime();
					ArrayUtility.maxSortWithSwap(A);
				    long estimatedTime = System.nanoTime() - startTime;
				    estimates[times] = estimatedTime;
				    System.out.print(".");
				}
				System.out.println("done in " + ArrayUtility.median(estimates) + " nanoseconds (median).");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {
			for(int i = 0; i <= POWEROF+2; i++) {
				System.out.print("java.util.Arrays.sort: No. of elements = " + (int)Math.pow(10, i));
				for(int times = 0; times < REPEATS; times++) {
					A = ArrayUtility.createRandomArray((int)Math.pow(10, i), 0, 1000);
					long startTime = System.nanoTime();
					java.util.Arrays.sort(A);
				    long estimatedTime = System.nanoTime() - startTime;
				    estimates[times] = estimatedTime;
				    System.out.print(".");
				}
				System.out.println("done in " + ArrayUtility.median(estimates) + " nanoseconds (median).");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

}
