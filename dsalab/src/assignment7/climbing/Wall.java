package assignment7.climbing;

public class Wall {

	private static final int[][] DEFAULT_WALL = new int[][] {
			{3,4,1,2,1},
			{8,2,3,1,5},
			{0,6,4,9,7},
			{6,5,8,3,4},
			{2,7,1,9,5},
			{6,8,3,5,1}
			};
	
	private int[][] wall;
	private ClimbingNode[][] nodeWall;

	public Wall(int[][] wall) {
		this.wall = wall;
		this.nodeWall = new ClimbingNode[getMaxHeight()][getMaxWidth()];
		for(int h = 0; h < getMaxHeight(); h++) {
			for(int w = 0; w < getMaxWidth(); w++) {
				nodeWall[h][w] = new ClimbingNode(getMaxHeight() - h, w + 1, wall[h][w]);
			}
		}
	}
	
	public Wall() {
		this(DEFAULT_WALL);
	}
	
	private void exceptionIfOutOfBounds(int height, int width) {
		height--;
		width--;
		
		if(height < 0 || width < 0 || height > getMaxHeight() || width > getMaxWidth()) {
			throw new IllegalArgumentException("Coordinates out of bound!");
		}
	}
	
	public int getMaxHeight() {
		return wall.length;
	}
	
	public int getMaxWidth() {
		if (wall.length == 0) {
			return 0;
		}
		return wall[0].length;
	}
	
	// Walls are numbered bottom-up and left-to-right, starting with 1 (not with 0)
	// x is the height and y is the width...
	// Ex. (1,3) -> (0,2) -> (sizeOfX - 1,y) => value = 3
	public int get(int height, int width) {
		exceptionIfOutOfBounds(height, width);
		height = getMaxHeight() - height;
		width = width - 1;
		
		return wall[height][width];
	}
	
	public int getOrInfinite(int height, int width) {
		try {
			return get(height, width);
		} catch (Exception e) {
			return ClimbingNode.INF;
		}
	}
	
	public ClimbingNode getNode(int height, int width) {
		try {
			return nodeWall[getMaxHeight() - height][width - 1];
		} catch (Exception e) {
			return null;
		}
	}
	
	public ClimbingNode copyPath(ClimbingNode root) {
		ClimbingNode n = root.copy();
		ClimbingNode nroot = n;
		ClimbingNode tmp = root;
		while(tmp != null) {
			tmp = tmp.ancestor;
			n.ancestor = (tmp == null ? null : tmp.copy());
			n = n.ancestor;
		}
		return nroot;
	}
	
	public int getUpperLeft(int height, int width) {
		try {
			return get(height + 1, width - 1);
		} catch (Exception e) {
			return ClimbingNode.INF;
		}
	}
	
	public int getUpperRight(int height, int width) {
		try {
			return get(height + 1, width + 1); 
		} catch (Exception e) {
			return ClimbingNode.INF;
		}
	}

	public int getUpperMiddle(int height, int width) {
		try {
			return get(height + 1, width);
		} catch (Exception e) {
			return ClimbingNode.INF;
		}
	}
	
	public void printWall() {
		for(int h = 0; h < getMaxHeight(); h++) {
			for(int w = 0; w < getMaxWidth(); w++) {
				System.out.print(nodeWall[h][w].value + " ");
			}
			System.out.println();
		}
	}

//TODO printwallwithpath
//	private Node reversePath(Node path) {
//		Node r = path;
//		while(path != null) {
////TODO			r
//		}
//	}
//	
//	public void printWall(Node path) {
//		
//		for(int h = 0; h < getMaxHeight(); h++) {
//			Node lastNode = getLastNode(path);
//			for(int w = 0; w < getMaxWidth(); w++) {
//				System.out.print(nodeWall[h][w].value);
//				if(lastNode != null && lastNode.width == w + 1) {
//					System.out.print("*   ");
//					lastNode = null;
//				} else {
//					System.out.print("    ");
//				}
//			}
//			System.out.println();
//		}
//	}
//	
	public static void main(String args[]) {
		Wall w = new Wall();
		
		System.out.println(w.get(1, 3));
		System.out.println(w.getUpperLeft(1, 3));
		System.out.println(w.getUpperMiddle(1, 3));
		System.out.println(w.getUpperRight(1, 3));

		System.out.println(w.get(3, 5));
		System.out.println(w.getUpperLeft(3, 5));
		System.out.println(w.getUpperMiddle(3, 5));
		System.out.println(w.getUpperRight(3, 5));
	}

}
