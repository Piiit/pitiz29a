package assignment7.climbing;

public class ClimbingNode {
	
	public final static char BLACK = 'b';
	public final static char WHITE = 'w';
	public final static char GRAY = 'g';
	public final static int INF = 1000000;
	
	int height;
	int width;
	int value;
	int distance;
	char color;
	ClimbingNode ancestor;
	ClimbingNodeList adj = new ClimbingNodeList();
	
	public ClimbingNode(int height, int width, int value) {
		this.height = height;
		this.width = width;
		this.value = value;
		this.ancestor = null;
		color = WHITE;
		distance = 0;
	}
	
	public ClimbingNode copy() {
		ClimbingNode n = new ClimbingNode(height, width, value);
		return n;
	}

	public void printPath() {
		int i = 0;
		ClimbingNode tmp = this;
		while(tmp != null) {
			i++;
			System.out.println("STEP " + i + " at Node " + tmp);
			tmp = tmp.ancestor;
		}
	}
	
	public void printAdjacents(int maxHeight, int maxWidth) {
		System.out.println(adj.toString());
	}
	
	@Override
	public String toString() {
		return height + ":" + width;
	}

}
