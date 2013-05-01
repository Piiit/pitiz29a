package assignment05;

public class List {
	Node root;
	
	public List() {
		root = null;
	}
	
	public boolean isEmpty() {
		return (root == null);
	}
	
	public void insertFirst(int i) {
		Node n = new Node(i);
		n.suc = root.suc;
		root = n;
	}
	
	public void insertLast(int i) {
		Node n = new Node(i);
		if(root == null) {
			root = n;
		} 
	}
	
	public void insertLastRec(int i) {
		
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
	

}
