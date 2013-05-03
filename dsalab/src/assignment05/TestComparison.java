package assignment05;

import java.util.ArrayList;

import tools.TestableAlgorithm;
import tools.AlgorithmComparison;
import tools.ArrayUtility;

public class TestComparison {
	
	private final static int REPEATS = 4;
	private final static int MAXELEMENTS = 15000;

	public static void main(String args[]) throws Exception {
		
		int[] A;
		
		ArrayList<TestableAlgorithm> algorithms = new ArrayList<TestableAlgorithm>();
		algorithms.add(new ListInsertionSort());
		algorithms.add(new ListQuickSort());
//		algorithms.add(new InsertionSort());

		AlgorithmComparison algComparison = new AlgorithmComparison(algorithms);
		algComparison.printHeader();
		
		
		try {
			boolean done = false;
			int elements = 10;
			while(!done) {
				elements = elements * 2;
				if (elements > MAXELEMENTS) {
					elements = MAXELEMENTS;
					done = true;
				}
				for(int i = 0; i < 3; i++) {
					A = ArrayUtility.createRandomArray(elements, 0, 100000);
					algComparison.run(A, REPEATS);
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}	
	}
}
