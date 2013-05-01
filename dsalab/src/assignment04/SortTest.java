package assignment04;

import java.util.ArrayList;

import tools.Algorithm;
import tools.AlgorithmComparison;
import tools.ArrayUtility;

public class SortTest {
	
	private final static int REPEATS = 5;
	private final static int MAXELEMENTS = 100000;

	public static void main(String args[]) throws Exception {
		
		int[] A;
		
		for(int k = 1; k <= 25; k++) {
			ArrayList<Algorithm> algorithms = new ArrayList<Algorithm>();
			algorithms.add(new QuickSort());
			algorithms.add(new HybridQuickSort(k));
	
			AlgorithmComparison algComparison = new AlgorithmComparison(algorithms);
			algComparison.printHeader();
			
			try {
				boolean done = false;
				int elements = k*10;
				while(!done) {
					elements = elements * 2;
					if (elements > MAXELEMENTS) {
						elements = MAXELEMENTS;
						done = true;
					}
					A = ArrayUtility.createRandomArray(elements, 0, 100000);
					algComparison.run(A, REPEATS);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}	
		}
	}
}
