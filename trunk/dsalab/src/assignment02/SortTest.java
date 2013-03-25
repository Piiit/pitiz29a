package assignment02;

import java.math.BigInteger;

import tools.ArrayUtility;

public class SortTest {
	
	private final static int REPEATS = 10;
	private final static long MAX_NANOSECONDS = 100000000L; //0.1 seconds

	public static void main(String args[]) throws Exception {
		
		int[] A;
		double[] estimatesInsertion = new double[REPEATS];
		double[] estimatesMerge = new double[REPEATS];
		int i = 3;
		double estimatedTimeInsertionSort = 0;
		double estimatedTimeMergeSort = 0;
		long startTime = 0;
		
		try {
			String formatTitle = "%-15s|%-11s|%-22s|%-22s|%-24s|%s\n";
			String format = "%15s|%11s|%22.2f|%22.2f|%24.2f|%s\n";
			System.out.printf(formatTitle, "No. of Elements", "No. of Runs", "Insertion Sort Median", "Merge Sort Median", "Winner is x times faster", "Winner");
			while(estimatedTimeInsertionSort < MAX_NANOSECONDS && estimatedTimeMergeSort < MAX_NANOSECONDS) {
				i++;
				for(int times = 0; times < REPEATS; times++) {
					A = ArrayUtility.createRandomArray((int)Math.pow(2, i), 0, 1000);
					
					startTime = System.nanoTime();
					InsertionSort.sort(A);
				    estimatesInsertion[times] = System.nanoTime() - startTime;

				    startTime = System.nanoTime();
					MergeSort.sort(A);
				    estimatesMerge[times] = System.nanoTime() - startTime;
				}
				estimatedTimeInsertionSort = ArrayUtility.median(estimatesInsertion);
				estimatedTimeMergeSort = ArrayUtility.median(estimatesMerge);
				System.out.printf(format, 
						(int)Math.pow(2, i), 
						REPEATS, 
						estimatedTimeInsertionSort, 
						estimatedTimeMergeSort, 
						(estimatedTimeInsertionSort > estimatedTimeMergeSort ? estimatedTimeInsertionSort/estimatedTimeMergeSort: estimatedTimeMergeSort/estimatedTimeInsertionSort),
						(estimatedTimeInsertionSort > estimatedTimeMergeSort ? "MergeSort" : "InsertionSort"));
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}	
		
		i = 3;
		estimatedTimeInsertionSort = 0;
		estimatedTimeMergeSort = 0;
		startTime = 0;
		BigInteger[] B;
		try {
			String formatTitle = "%-15s|%-11s|%-22s|%-22s|%-24s|%s\n";
			String format = "%15s|%11s|%22.2f|%22.2f|%24.2f|%s\n";
			System.out.printf(formatTitle, "No. of Elements", "No. of Runs", "Insertion Sort Median", "Merge Sort Median", "Winner is x times faster", "Winner");
			while(estimatedTimeInsertionSort < MAX_NANOSECONDS && estimatedTimeMergeSort < MAX_NANOSECONDS) {
				i++;
				for(int times = 0; times < REPEATS; times++) {
					B = ArrayUtility.createRandomArrayBigInt((int)Math.pow(2, i), 1024);
					
					startTime = System.nanoTime();
					InsertionSort.sort(B);
				    estimatesInsertion[times] = System.nanoTime() - startTime;

				    startTime = System.nanoTime();
					MergeSort.sort(B);
				    estimatesMerge[times] = System.nanoTime() - startTime;
				}
				estimatedTimeInsertionSort = ArrayUtility.median(estimatesInsertion);
				estimatedTimeMergeSort = ArrayUtility.median(estimatesMerge);
				System.out.printf(format, 
						(int)Math.pow(2, i), 
						REPEATS, 
						estimatedTimeInsertionSort, 
						estimatedTimeMergeSort, 
						(estimatedTimeInsertionSort > estimatedTimeMergeSort ? estimatedTimeInsertionSort/estimatedTimeMergeSort: estimatedTimeMergeSort/estimatedTimeInsertionSort),
						(estimatedTimeInsertionSort > estimatedTimeMergeSort ? "MergeSort" : "InsertionSort"));
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}	
		
		i = 3;
		estimatedTimeInsertionSort = 0;
		estimatedTimeMergeSort = 0;
		startTime = 0;
		B = null;
		try {
			String formatTitle = "%-15s|%-11s|%-22s|%-22s|%-24s|%s\n";
			String format = "%15s|%11s|%22.2f|%22.2f|%24.2f|%s\n";
			System.out.printf(formatTitle, "No. of Elements", "No. of Runs", "Insertion Sort Median", "Merge Sort Median", "Winner is x times faster", "Winner");
			while(estimatedTimeInsertionSort < MAX_NANOSECONDS && estimatedTimeMergeSort < MAX_NANOSECONDS) {
				i++;
				for(int times = 0; times < REPEATS; times++) {
					B = ArrayUtility.createRandomArrayBigInt((int)Math.pow(2, i), 2048);
					
					startTime = System.nanoTime();
					InsertionSort.sort(B);
				    estimatesInsertion[times] = System.nanoTime() - startTime;

				    startTime = System.nanoTime();
					MergeSort.sort(B);
				    estimatesMerge[times] = System.nanoTime() - startTime;
				}
				estimatedTimeInsertionSort = ArrayUtility.median(estimatesInsertion);
				estimatedTimeMergeSort = ArrayUtility.median(estimatesMerge);
				System.out.printf(format, 
						(int)Math.pow(2, i), 
						REPEATS, 
						estimatedTimeInsertionSort, 
						estimatedTimeMergeSort, 
						(estimatedTimeInsertionSort > estimatedTimeMergeSort ? estimatedTimeInsertionSort/estimatedTimeMergeSort: estimatedTimeMergeSort/estimatedTimeInsertionSort),
						(estimatedTimeInsertionSort > estimatedTimeMergeSort ? "MergeSort" : "InsertionSort"));
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}	
	}
}
