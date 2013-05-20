package assignment06.priorityqueue;

public class Node {

	int key;
	Node left;
	Node right;
	Node parent;
	int lcount; // Number of nodes in the left subtree 
	int rcount; // Number of nodes in the right subtree
	
	public Node(int key, Node parent) {
		super();
		this.key = key;
		this.parent = parent;
		this.lcount = 0;
		this.rcount = 0;
		this.left = null;
		this.right = null;
	}

	@Override
	public String toString() {
		return "Node[key=" + key + ", lcount=" + lcount + ", rcount=" + rcount + "]";
	}
	
	

}
