package assignment7.climbing;

public class Main {

	public static void main(String[] args) {

		Wall wall = new Wall();
		
		Climbing cr = new Climbing(wall);
		ClimbingNode root = cr.getBestRoute();
		
		root.printPath();
		
		root = cr.buildGraph();
		root.adj.get(1,1).adj.print();
		root.printAdjacents(5, 5);
		root = root.adj.get(1, 1);
		root.printAdjacents(5, 5);
	}

}
