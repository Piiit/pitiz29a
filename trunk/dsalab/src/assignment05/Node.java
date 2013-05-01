package assignment05;

public class Node {
	int value;
	Node next;
	
	public Node(int value) {
		this.value = value;
		this.next = null;
	}

	@Override
	public String toString() {
		return "[Node: value=" + value + 
			   "; suc=" + (next == null ? "NULL" : next.value) +
			   "]";
	}
	
	
}
