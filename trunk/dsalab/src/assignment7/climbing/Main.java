package assignment7.climbing;

public class Main {

	public static void main(String[] args) {

		Wall wall = new Wall();
		
		Climbing cr = new Climbing(wall);
//		ClimbingNode root = cr.getBestRoute();
		
		ClimbingNode root = cr.searchBFS();
		root.printPath();
		
//		root = cr.buildGraph();
//		System.out.println(root.adj.get(1,1).adj.toString());
//		root.printAdjacents(5, 5);
//		root = root.adj.get(1, 1);
//		root.printAdjacents(5, 5);
	}

}
