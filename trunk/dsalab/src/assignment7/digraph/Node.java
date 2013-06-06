package assignment7.digraph;

public class Node {
	
	public final static char BLACK = 'b';
	public final static char WHITE = 'w';
	public final static char GRAY = 'g';

	int id;
	char color = WHITE;
	Node pred = null;
	int distance = 0;
	private NodeList adj = new NodeList();
	
	public Node(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "Node[id=" + id + ", color=" + color + ", pred=" + pred + ", distance=" + distance + "]";
	}
	
	public void addAdjacent(Node n) {
		adj.add(n);
	}

	public String getAdjacentListAsString() {
		return adj.toString();
	}
	
	public NodeList getAdjacentList() {
		return adj;
	}
	
}
