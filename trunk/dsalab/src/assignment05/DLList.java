package assignment05;

public class DLList {
	DLLNode root;
	
	public DLList() {
		root = null;
	}
	
	public boolean isEmpty() {
		return (root == null);
	}
	
	public void insertFirst(int i) {
		DLLNode n = new DLLNode(i, null, root);
		if(root != null) {
			root.prev = n;
		}
		root = n;
		n.prev = root;
	}
	
	public void insertLast(int i) {
		if(root == null) {
			root = new DLLNode(i, null, null);
			return;
		} 
		
		DLLNode n = root;
		while(n.next != null) {
			n = n.next;
		}
		n.next = new DLLNode(i, n, null); 
	}
	
	public void insertLastRec(int i) {
		insertLastRec(root, i);
	}
	
	private void insertLastRec(DLLNode n, int i) {
		if(n == null) {
			n = new DLLNode(i, null, null);
		} else if(n.next == null) {
			n.next = new DLLNode(i, n, null);
		} else {
			insertLastRec(n.next, i);
		}
	}
	
	public void print() {
		DLLNode n = root;
		int i = 0;
		while(n != null) {
			System.out.println(i + ":" + n.value);
			n = n.next;
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
		printRec(n.next, ++i);
	}
	
	public DLLNode search(int i) {
		DLLNode n = root;
		while(n != null) {
			if(n.value == i) {
				System.out.println("Found: " + n + "; root=" + root);
				return n;
			}
			n = n.next;
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
		if(n.prev != null) {
			n.prev.next = n.next;
		}
		if(n.next != null) {
			n.next.prev = n.prev;
		}
		if(n == root) {
			root = n.next;
		}
		n = null;
	}
	
	private DLLNode searchRec(DLLNode n, int i) {
		if(n == null) {
			return null;
		}
		if(n.value == i) {
			System.out.println("Found (recursive): " + n + "; root=" + root);
			return n;
		}
		return searchRec(n.next, i);
	}
	
	public void delete(int i) {
		DLLNode n = search(i);
		if(n == null) {
			return;
		}
		if(n.prev != null) {
			n.prev.next = n.next;
		}
		if(n.next != null) {
			n.next.prev = n.prev;
		}
		if(n == root) {
			root = n.next;
		}
		n = null;
	}
	
	public void deleteFirst() {
		if(root == null) {
			return;
		}
		DLLNode toDelete = root;
		if(toDelete.next != null) {
			toDelete.next.prev = null;
		}
		root = toDelete.next;
		toDelete = null;
	}
	
	public static void exchangeValues(DLLNode n1, DLLNode n2) {
		if(n1 == null || n2 == null) {
			throw new IllegalArgumentException();
		}
		int temp = n1.value;
		n1.value = n2.value;
		n2.value = temp;
	}
	
	public void fromArray(int[] array) {
		for(int i = array.length-1; i >= 0; i--) {
			insertFirst(array[i]);
		}
	}
	

}
