package assignment7.climbing;

public class Climbing {

	private Wall wall;

	public Climbing(Wall wall) {
		this.wall = wall;
	}
	
	private int min(int ... values) {
		
		if(values.length < 2) {
			throw new IllegalArgumentException("min needs at least 2 numbers to compare!");
		}
		
		int result = values[0];
		
		for(int v : values) {
			if(v < result) {
				result = v;
			}
		}
		
		return result;
	}

	/* RECURSIVE SOLUTION */
	private int searchRec(int height, int width) {
		if(height == wall.getMaxHeight()) {
			return wall.getOrInfinite(height, width);
		}
		
		int value1 = wall.getOrInfinite(height, width) + searchRec(height+1, width);
		int value2 = wall.getOrInfinite(height, width) + searchRec(height+1, width-1);
		int value3 = wall.getOrInfinite(height, width) + searchRec(height+1, width+1);
		
		int min = min(value1, value2, value3);
		
		ClimbingNode curNode = wall.getNode(height, width);
		if (curNode != null) {
			if (min == value1) {
				curNode.ancestor = wall.getNode(height+1, width);
			} else if (min == value2) {
				curNode.ancestor = wall.getNode(height+1, width-1);
			} else {
				curNode.ancestor = wall.getNode(height+1, width+1);
			}
		}
		
		return min;
	}
	
	public ClimbingNode getBestRoute() {
		int min = searchRec(1, 1);
		int mini = 1;
		ClimbingNode[] paths = new ClimbingNode[wall.getMaxWidth()];
		paths[0] = wall.copyPath(wall.getNode(1, 1));
		for(int i = 2; i <= wall.getMaxWidth(); i++) {
			int cost = searchRec(1, i);
			paths[i-1] = wall.copyPath(wall.getNode(1, i));
			mini = cost < min ? i : mini;
			min = cost < min ? cost : min;
		}
		
		return paths[mini];
	}
	
	/* DYNAMIC PROGRAMMING SOLUTION */
	
	//Build a digraph out of the climbing wall data...
	public ClimbingNode buildGraph() {
		ClimbingNode root = new ClimbingNode(0, 0, 0);
		
		// Add all nodes at level 1 to the root...
		for(int w = 1; w <= wall.getMaxWidth(); w++) {
			root.adj.insertFirst(wall.getNode(1, w));
		}
		
		// At higher levels, add only the three upper nodes...
		for(int h = 1; h <= wall.getMaxHeight(); h++) {
			for(int w = 1; w <= wall.getMaxWidth(); w++) {
				System.out.println("Current node: " + wall.getNode(h, w));
				wall.getNode(h, w).adj.insertFirst(wall.getNode(h+1, w-1));
				wall.getNode(h, w).adj.insertFirst(wall.getNode(h+1, w));
				wall.getNode(h, w).adj.insertFirst(wall.getNode(h+1, w+1));
				
				wall.getNode(h, w).printAdjacents(5, 5);
			}
		}
		
		return root;
	}

	
	
	public static void main(String args[]) {
		
		int[][] test = {
				{2,8,9,5,8},
				{4,4,6,2,3},
				{5,7,5,6,1},
				{3,2,5,4,8}
				};
		
		Wall wall = new Wall(test);
		
		Climbing cr = new Climbing(wall);

		System.out.println(cr.searchRec(1, 1));
//		wall.printPath(wall.getNode(1, 1));
		

		int min = cr.searchRec(1, 1);
		int mini = 1;
		ClimbingNode[] paths = new ClimbingNode[wall.getMaxWidth()];
		paths[0] = wall.copyPath(wall.getNode(1, 1));
		for(int i = 2; i <= wall.getMaxWidth(); i++) {
			int cost = cr.searchRec(1, i);
			paths[i-1] = wall.copyPath(wall.getNode(1, i));
			mini = cost < min ? i : mini;
			min = cost < min ? cost : min;
		}
		
		System.out.println(mini);
		System.out.println(paths[mini]);

		wall.getNode(1,mini).printPath();
		
		ClimbingNode root = cr.buildGraph();
		System.out.println(root);
		root.printPath();

//		wall.printPath(wall.getNode(1, 3));
	}
	
	
}
