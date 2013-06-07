package assignment7.digraph;

public class Node {
	
	public final static char BLACK = 'b';
	public final static char WHITE = 'w';
	public final static char GRAY = 'g';
	public final static int INF = 1000000;

	private int id;
	private String description;
	private Node pred = null;
	private int distance = 0;
	private int starttime = 0;
	private int endtime = 0;
	private NodeList adj = new NodeList();
	private char color = WHITE;
	
	public Node(int id) {
		this.id = id;
	}
	
	public void setColor(char color) {
		System.out.println("Node " + getId() + " (" +description+ ") color is " + color);
		this.color = color;
	}
	
	public char getColor() {
		return color;
	}
	
	@Override
	public String toString() {
		return "Node[id=" + getId() + ", color=" + color + ", pred=" + getPred() + ", distance=" + getDistance() + "]";
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

	public int getStarttime() {
		return starttime;
	}

	public void setStarttime(int starttime) {
		System.out.println("Node " + getId() + " starttime is " + starttime);
		this.starttime = starttime;
	}

	public int getEndtime() {
		return endtime;
	}

	public void setEndtime(int endtime) {
		System.out.println("Node " + getId() + " endtime is " + endtime);
		this.endtime = endtime;
	}

	public Node getPred() {
		return pred;
	}

	public void setPred(Node pred) {
		this.pred = pred;
	}

	public int getId() {
		return id;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
