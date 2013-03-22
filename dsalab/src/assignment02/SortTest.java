package assignment02;

import java.math.BigInteger;
import java.util.Arrays;

import tools.ArrayUtility;

public class SortTest {
	
	private final static int REPEATS = 10;

	public static void main(String args[]) throws Exception {
		int[] array = ArrayUtility.createRandomArray(5, 0, 100);
		InsertionSort.sort(array);
		System.out.println(Arrays.toString(array));
		
		BigInteger[] array2 = ArrayUtility.createRandomArrayBigInt(5, 1024);
		InsertionSort.sort(array2);
		System.out.println(Arrays.toString(array2));
		
		int[] array3 = ArrayUtility.createRandomArray(5, 0, 100);
		MergeSort.sort(array3);
		System.out.println(Arrays.toString(array3));

		BigInteger[] array4 = {
				new BigInteger("5"), 
				new BigInteger("2"), 
				new BigInteger("7"), 
				new BigInteger("12"), 
				new BigInteger("0")
				};
		MergeSort.sort(array4);
		System.out.println(Arrays.toString(array4));
		
		
//		int[] A;
		double[] estimatesInsertion = new double[REPEATS];
		double[] estimatesMerge = new double[REPEATS];
		int i = 0;
		double estimatedTimeInsertionSort = 0;
		double estimatedTimeMergeSort = 0;
		long startTime = 0;
		
//		try {
//			String formatTitle = "%-15s|%-11s|%-22s|%-22s|%-24s|%s\n";
//			String format = "%15s|%11s|%22.2f|%22.2f|%24.2f|%s\n";
//			System.out.printf(formatTitle, "No. of Elements", "No. of Runs", "Insertion Sort Median", "Merge Sort Median", "Winner is x times faster", "Winner");
//			while(estimatedTimeInsertionSort < 5000000000L && estimatedTimeMergeSort < 5000000000L) {
//				i++;
//				for(int times = 0; times < REPEATS; times++) {
//					A = ArrayUtility.createRandomArray((int)Math.pow(2, i), 0, 1000);
//					
//					startTime = System.nanoTime();
//					InsertionSort.sort(A);
//				    estimatesInsertion[times] = System.nanoTime() - startTime;
//
//				    startTime = System.nanoTime();
//					MergeSort.sort(A);
//				    estimatesMerge[times] = System.nanoTime() - startTime;
//				}
//				estimatedTimeInsertionSort = ArrayUtility.median(estimatesInsertion);
//				estimatedTimeMergeSort = ArrayUtility.median(estimatesMerge);
//				System.out.printf(format, 
//						(int)Math.pow(2, i), 
//						REPEATS, 
//						estimatedTimeInsertionSort, 
//						estimatedTimeMergeSort, 
//						(estimatedTimeInsertionSort > estimatedTimeMergeSort ? estimatedTimeInsertionSort/estimatedTimeMergeSort: estimatedTimeMergeSort/estimatedTimeInsertionSort),
//						(estimatedTimeInsertionSort > estimatedTimeMergeSort ? "MergeSort" : "InsertionSort") + " wins!");
//			}
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}	
		
		BigInteger[] A;
		try {
			String formatTitle = "%-15s|%-11s|%-22s|%-22s|%-24s|%s\n";
			String format = "%15s|%11s|%22.2f|%22.2f|%24.2f|%s\n";
			System.out.printf(formatTitle, "No. of Elements", "No. of Runs", "Insertion Sort Median", "Merge Sort Median", "Winner is x times faster", "Winner");
			while(estimatedTimeInsertionSort < 5000000000L && estimatedTimeMergeSort < 5000000000L) {
				i++;
				for(int times = 0; times < REPEATS; times++) {
					A = ArrayUtility.createRandomArrayBigInt((int)Math.pow(2, i), 2048);
					
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
	}
}
