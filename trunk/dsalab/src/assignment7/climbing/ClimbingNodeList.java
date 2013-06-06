package assignment7.climbing;

public class ClimbingNodeList {
	ClimbingNode root;
	
	public ClimbingNodeList() {
		root = null;
	}
	
	public boolean isEmpty() {
		return (root == null);
	}
	
	public void insertFirst(ClimbingNode n) {
		if(n == null) {
			return;
		}
		System.out.println("Inserting: " + n);
		n.next = root;
		root = n;
	}
	
	public void print() {
		ClimbingNode n = root;
		int i = 0;
		while(n != null) {
			System.out.println(i + ":" + n);
			n = n.next;
			i++;
		}
		System.out.println("Root=" + root);
	}
	
	public ClimbingNode get(int height, int width) {
		ClimbingNode n = root;
		while(n != null) {
			if(n.height == height && n.width == width) {
//				System.out.println("Found: " + n + "; root=" + root);
				return n;
			}
			n = n.next;
		}
		return null;
	}

	
}
