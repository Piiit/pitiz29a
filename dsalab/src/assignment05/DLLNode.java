package assignment05;

public class DLLNode {
	int value;
	DLLNode pre;
	DLLNode suc;
	
	public DLLNode(int value, DLLNode pre, DLLNode suc) {
		this.value = value;
		this.pre = pre;
		this.suc = suc;
	}

	@Override
	public String toString() {
		return "[Node: value=" + value + 
			   "; pre=" + (pre == null ? "NULL" : pre.value) + 
			   "; suc=" + (suc == null ? "NULL" : suc.value) +
			   "]";
	}
	
	
}
