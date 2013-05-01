package assignment05;

public class DLLNode {
	int value;
	DLLNode prev;
	DLLNode next;
	
	public DLLNode(int value, DLLNode prev, DLLNode next) {
		this.value = value;
		this.prev = prev;
		this.next = next;
	}

	@Override
	public String toString() {
		return "[Node: value=" + value + 
			   "; pre=" + (prev == null ? "NULL" : prev.value) + 
			   "; suc=" + (next == null ? "NULL" : next.value) +
			   "]";
	}
	
	
}
