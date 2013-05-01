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
			head.prev = n;
		}
		head = n;
		n.prev = head;
	}
	
	public void insertLast(int i) {
		DLLNode n = new DLLNode(i, tail, null);
		if(head == null) {
			head = n;
		} else {
			tail.next = n;
		}
		tail = n;
	}
	
	// This is the same as insertLast, because with 
	// a head/tail-list we can directly jump to the end
	// without any recursive call	
	public void insertLastRec(int i) {
		insertLast(i);
	}
	
	public void print() {
		DLLNode n = head;
		int i = 0;
		while(n != null) {
			System.out.println(i + ":" + n.value);
			n = n.next;
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
		printRec(n.next, ++i);
	}
	
	public DLLNode search(int i) {
		DLLNode n = head;
		while(n != null) {
			if(n.value == i) {
				System.out.println("Found: " + n + "; root=" + head + "; tail=" + tail);
				return n;
			}
			n = n.next;
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
		if(n.prev != null) {
			n.prev.next = n.next;
		}
		if(n.next != null) {
			n.next.prev = n.prev;
		}
		if(n == head) {
			head = n.next;
		}
		if(n == tail) {
			tail = n.prev;
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
		if(n == head) {
			head = n.next;
		}
		if(n == tail) {
			tail = n.prev;
		}
		n = null;
	}
	
	public void deleteFirst() {
		if(head == null) {
			return;
		}
		DLLNode toDelete = head;
		if(toDelete.next != null) {
			toDelete.next.prev = null;
		}
		head = toDelete.next;
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
