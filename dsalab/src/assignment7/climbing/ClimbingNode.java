package assignment7.climbing;

public class ClimbingNode {
	
	static final int INF = 1000000;
	
	@Override
	public String toString() {
		return height + ":" + width + " V=" + value + (ancestor == null ? "" : " A=" + ancestor.height + ":" + ancestor.width);
	}

	int height;
	int width;
	int value;
	ClimbingNode ancestor;
	ClimbingNodeList adj = new ClimbingNodeList();
	ClimbingNode next;
	
	public ClimbingNode(int height, int width, int value) {
		this.height = height;
		this.width = width;
		this.value = value;
		this.ancestor = null;
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
		for(int h = 1; h <= maxHeight; h++) {
			for(int w = 1; w <= maxWidth; w++) {
				ClimbingNode node = adj.get(h, w);
				if(node != null) {
					System.out.print(node.height + ":" + node.width + " ");
				} else {
					System.out.print("    ");
				}
			}
			System.out.println();
		}
	}

}
