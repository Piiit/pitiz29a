package assignment04;

import tools.AlgorithmComparison;
import tools.ArrayUtility;

public class SortTest {
	
	private final static int REPEATS = 3;
	private final static int MAXELEMENTS = 10000;

	public static void main(String args[]) throws Exception {
		
		int[] A;
		
		QuickSort quickSort = new QuickSort();
		HybridQuickSort hybridQuickSort = new HybridQuickSort(3);

		AlgorithmComparison algComparison = new AlgorithmComparison(quickSort, hybridQuickSort);
		algComparison.printHeader();
		
		try {
			boolean done = false;
			int elements = 5;
			while(!done) {
				elements = elements * 2;
				if (elements > MAXELEMENTS) {
					elements = MAXELEMENTS;
					done = true;
				}
				A = ArrayUtility.createRandomArray(elements, 0, 100000);
				for(int k = 1; k <= 25; k++) {
					quickSort.setArray(ArrayUtility.copyArray(A));
					hybridQuickSort.setArray(ArrayUtility.copyArray(A));
					hybridQuickSort.setK(k);
					algComparison.run(REPEATS);
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}	
	}
}
