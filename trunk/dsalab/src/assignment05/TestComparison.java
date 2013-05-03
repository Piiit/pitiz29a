package assignment05;

import java.util.ArrayList;

import tools.TestableAlgorithm;
import tools.AlgorithmComparison;
import tools.ArrayUtility;

public class TestComparison {
	
	private final static int REPEATS = 5;
	private final static int MAXELEMENTS = 1000000;

	public static void main(String args[]) throws Exception {
		
		int[] A;
		
		for(int k = 1; k <= 500; k+=50) {
			ArrayList<TestableAlgorithm> algorithms = new ArrayList<TestableAlgorithm>();
			algorithms.add(new ListQuickSort());
			algorithms.add(new ListInsertionSort());
	
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
