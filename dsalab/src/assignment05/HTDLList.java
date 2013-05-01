package assignment05;

public class HTDLList {
	DLLNode root;
	DLLNode tail;
	
	public HTDLList() {
		root = null;
		tail = null;
	}
	
	public boolean isEmpty() {
		return (root == null);
	}
	
	public void insertFirst(int i) {
		DLLNode n = new DLLNode(i, null, root);
		if(root == null) {
			tail = n;
		} else {
			root.pre = n;
		}
		root = n;
		n.pre = root;
	}
	
	public void insertLast(int i) {
		DLLNode n = new DLLNode(i, tail, null);
		if(root == null) {
			root = n;
		} else {
			tail.suc = n;
		}
		tail = n;
	}
	
	// This is the same as insertLast, because with 
	// a head/tail-list we can directly jump to the end
	public void insertLastRec(int i) {
		insertLast(i);
	}
	
	public void print() {
		DLLNode n = root;
		int i = 0;
		while(n != null) {
			System.out.println(i + ":" + n.value);
			n = n.suc;
			i++;
		}
		System.out.println();
	}
	
	public void printRec() {
		printRec(root, 0);
		System.out.println();
	}
	
	private void printRec(DLLNode n, int i) {
		if(n == null) {
			return;
		}
		System.out.println(i + ":" + n.value);
		printRec(n.suc, ++i);
	}
	
	public DLLNode search(int i) {
		DLLNode n = root;
		while(n != null) {
			if(n.value == i) {
				System.out.println("Found: " + n + "; root=" + root + "; tail=" + tail);
				return n;
			}
			n = n.suc;
		}
		return null;
	}
	
	public DLLNode searchRec(int i) {
		return searchRec(root, i);
	}
	
	public void deleteRec(int i) {
		DLLNode n = searchRec(i);
		if(n == null) {
			return;
		}
		if(n.pre != null) {
			n.pre.suc = n.suc;
		}
		if(n.suc != null) {
			n.suc.pre = n.pre;
		}
		if(n == root) {
			root = n.suc;
		}
		if(n == tail) {
			tail = n.pre;
		}
		n = null;
	}
	
	private DLLNode searchRec(DLLNode n, int i) {
		if(n == null) {
			return null;
		}
		if(n.value == i) {
			System.out.println("Found (recursive): " + n + "; root=" + root + "; tail=" + tail);
			return n;
		}
		return searchRec(n.suc, i);
	}
	
	public void delete(int i) {
		DLLNode n = search(i);
		if(n == null) {
			return;
		}
		if(n.pre != null) {
			n.pre.suc = n.suc;
		}
		if(n.suc != null) {
			n.suc.pre = n.pre;
		}
		if(n == root) {
			root = n.suc;
		}
		if(n == tail) {
			tail = n.pre;
		}
		n = null;
	}
	
	public void deleteFirst() {
		if(root == null) {
			return;
		}
		DLLNode toDelete = root;
		if(toDelete.suc != null) {
			toDelete.suc.pre = null;
		}
		root = toDelete.suc;
		toDelete = null;
	}
	
	private void exchange(DLLNode n1, DLLNode n2) {
		if(n1 == null || n2 == null) {
			throw new IllegalArgumentException();
		}
		DLLNode pre = n1.pre;
		
	}
	

}
