package assignment7.climbing;

public class Main {

	public static void main(String[] args) {
		
		System.out.println("This is the recursive solution...");
		Wall wall1 = new Wall();
		Climbing cr1 = new Climbing(wall1);
		ClimbingNode root1 = cr1.getBestRouteRec();
		root1.printPath();
		
		System.out.println();
		System.out.println("...and this is the dynamic programming solution... (ordering reversed)");
		Wall wall2 = new Wall();
		Climbing cr2 = new Climbing(wall2);
		ClimbingNode root2 = cr2.getBestRouteDP();
		root2.printPath();
	}

}
