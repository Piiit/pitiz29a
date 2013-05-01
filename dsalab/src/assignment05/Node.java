package assignment05;

public class Node {
	int value;
	Node suc;
	
	public Node(int value) {
		this.value = value;
		this.suc = null;
	}

	@Override
	public String toString() {
		return "[Node: value=" + value + 
			   "; suc=" + (suc == null ? "NULL" : suc.value) +
			   "]";
	}
	
	
}
