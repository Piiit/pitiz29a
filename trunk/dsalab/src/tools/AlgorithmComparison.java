package tools;

import java.util.ArrayList;

public class AlgorithmComparison {
	
	private ArrayList<TestableAlgorithm> algList = new ArrayList<TestableAlgorithm>();
	private String format = "%15s | %22.3f | %s\n";
	private String formatTitle = "%15s | %22s | %s\n";
	
	public AlgorithmComparison(ArrayList<TestableAlgorithm> algList) {
		this.algList = algList;
	}
	
	public void printHeader() {
		System.out.println("Comparing...");
		for(TestableAlgorithm a : algList) {
			System.out.println(a.getName());
		}
		System.out.println();
		System.out.printf(formatTitle, "Elements", "Speed", "Winner");
	}
	
	public void run(int[] A, int repeats) {
		long startTime = 0;
		double[][] estimates = new double[algList.size()][repeats];
		double[] estimatedTime = new double[algList.size()];

		try {
			int id=0;
			for(int times = 0; times < repeats; times++) {
				id=0;
				for(TestableAlgorithm alg : algList) {
					alg.setArray(ArrayUtility.copyArray(A));
					startTime = System.nanoTime();
					try {
						alg.execute();
					} catch (Exception e) {
						System.err.println("Algorithm " + alg.getName() + " failed!");
						e.printStackTrace();
					}
				    estimates[id][times] = System.nanoTime() - startTime;
				    id++;
				}
			}
			id=0;
			String winner = "n/a";
			double speed = 0;
			for(TestableAlgorithm alg : algList) {
				estimatedTime[id] = ArrayUtility.median(estimates[id]);
				if(estimatedTime[id] < speed || speed == 0) {
					speed = estimatedTime[id];
					winner = alg.getName();
				}
				id++;
			}
			System.out.printf(format, A.length, speed, winner);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
