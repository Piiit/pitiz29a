package assignment05;

public class HTDLList {
	DLLNode head;
	DLLNode tail;
	
	public HTDLList() {
		head = null;
		tail = null;
	}
	
	public boolean isEmpty() {
		return (head == null);
	}
	
	public void insertFirst(int i) {
		DLLNode n = new DLLNode(i, null, head);
		if(head == null) {
			tail = n;
		} else {
			head.pre = n;
		}
		head = n;
		n.pre = head;
	}
	
	public void insertLast(int i) {
		DLLNode n = new DLLNode(i, tail, null);
		if(head == null) {
			head = n;
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
		DLLNode n = head;
		int i = 0;
		while(n != null) {
			System.out.println(i + ":" + n.value);
			n = n.suc;
			i++;
		}
		System.out.println();
	}
	
	public void printRec() {
		printRec(head, 0);
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
		DLLNode n = head;
		while(n != null) {
			if(n.value == i) {
				System.out.println("Found: " + n + "; root=" + head + "; tail=" + tail);
				return n;
			}
			n = n.suc;
		}
		return null;
	}
	
	public DLLNode searchRec(int i) {
		return searchRec(head, i);
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
		if(n == head) {
			head = n.suc;
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
			System.out.println("Found (recursive): " + n + "; root=" + head + "; tail=" + tail);
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
		if(n == head) {
			head = n.suc;
		}
		if(n == tail) {
			tail = n.pre;
		}
		n = null;
	}
	
	public void deleteFirst() {
		if(head == null) {
			return;
		}
		DLLNode toDelete = head;
		if(toDelete.suc != null) {
			toDelete.suc.pre = null;
		}
		head = toDelete.suc;
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
	

}
