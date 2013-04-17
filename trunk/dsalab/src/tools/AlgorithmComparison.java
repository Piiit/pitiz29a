package tools;

public class AlgorithmComparison {
	
	private Algorithm alg1;
	private Algorithm alg2;
	private String format = "%15s|%6s|%22.2f|%22.2f|%8.3f|%s\n";
	
	public AlgorithmComparison(Algorithm alg1, Algorithm alg2) {
		this.alg1 = alg1;
		this.alg2 = alg2;
	}
	
	public void printHeader() {
		String formatTitle = "%15s|%6s|%22s|%22s|%8s|%s\n";
		System.out.printf(formatTitle, 
				"Elements ", 
				"Runs ", 
				alg1.getName() + " ", 
				alg2.getName() + " ", 
				"Ratio ", 
				"Winner");
	}
	
	public void run(int repeats) {
		long startTime = 0;
		double[] estimatesAlg1 = new double[repeats];
		double[] estimatesAlg2 = new double[repeats];
		double estimatedTimeAlg1 = 0;
		double estimatedTimeAlg2 = 0;

		try {
			for(int times = 0; times < repeats; times++) {
				startTime = System.nanoTime();
				try {
					alg1.execute();
				} catch (Exception e) {
					System.err.println("Algorithm " + alg1.getName() + " failed!");
					e.printStackTrace();
				}
			    estimatesAlg1[times] = System.nanoTime() - startTime;
			    
			    startTime = System.nanoTime();
				try {
					alg2.execute();
				} catch (Exception e) {
					System.err.println("Algorithm " + alg2.getName() + " failed!");
					e.printStackTrace();
				}
				estimatesAlg2[times] = System.nanoTime() - startTime;
			}
			estimatedTimeAlg1 = ArrayUtility.median(estimatesAlg1);
			estimatedTimeAlg2 = ArrayUtility.median(estimatesAlg2);
			double ratio = 1;
			String winner = "n/a";
			if (Math.abs(estimatedTimeAlg1 - estimatedTimeAlg2) < 0.001) { //< minimal difference, considered equal
				winner = "n/a";
			} else if (estimatedTimeAlg1 > estimatedTimeAlg2) {
				ratio = estimatedTimeAlg1/estimatedTimeAlg2;
				winner = alg2.getName();
			} else {
				ratio = estimatedTimeAlg2/estimatedTimeAlg1;
				winner = alg1.getName();
			}
			System.out.printf(format, 
					alg1.getElementCount() + "/" + alg2.getElementCount(), 
					repeats, 
					estimatedTimeAlg1, 
					estimatedTimeAlg2, 
					ratio,
					winner);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
