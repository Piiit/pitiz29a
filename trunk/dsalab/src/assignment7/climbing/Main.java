package assignment7.climbing;

public class Main {

	public static void main(String[] args) {
		
		System.out.println("This is the recursive solution...");
		Wall wall1 = new Wall();
		Climbing cr1 = new Climbing(wall1);
		long startTime = System.nanoTime();
		ClimbingNode root1 = cr1.getBestRouteRec();
		long runningTime = (System.nanoTime() - startTime);
		System.out.println("Running time = " + runningTime);
		root1.printPath();
		
		System.out.println();
		System.out.println("...and this is the dynamic programming solution... (ordering reversed)");
		Wall wall2 = new Wall();
		Climbing cr2 = new Climbing(wall2);
		long startTime2 = System.nanoTime();
		ClimbingNode root2 = cr2.getBestRouteDP();
		long runningTime2 = (System.nanoTime() - startTime2);
		System.out.println("Running time = " + runningTime2);
		root2.printPath();
		
		System.out.println();
		if(runningTime < runningTime2) {
			System.out.println("The recursive algorithm was " + (float)runningTime/runningTime2+ " times faster than the DP-algorithm.");
		} else {
			System.out.println("The DP-algorithm was " + (float)runningTime/runningTime2 + " times faster than the recursive algorithm.");
			
		}
	}

}
