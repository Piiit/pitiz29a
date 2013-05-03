package assignment05;

public class HTList {
	Node head;
	Node tail;
	
	public HTList() {
		head = null;
		tail = null;
	}
	
	public boolean isEmpty() {
		return (head == null);
	}
	
	public void insertFirst(int i) {
		Node n = new Node(i);
		n.next = head;
		if(head == null) {
			tail = n.next;
		} 
		head = n;
	}
	
	public void insertLast(int i) {
		Node n = new Node(i);
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
		Node n = head;
		int i = 0;
		while(n != null) {
			System.out.println(i + ":" + n.value);
			n = n.next;
			i++;
		}
		System.out.println("Head=" + head + "; tail=" + tail);
	}
	
	public void printRec() {
		printRec(head, 0);
		System.out.println("Head=" + head + "; tail=" + tail);
	}
	
	private void printRec(Node n, int i) {
		if(n == null) {
			return;
		}
		System.out.println(i + ":" + n.value);
		printRec(n.next, ++i);
	}
	
	public Node search(int i) {
		Node n = head;
		while(n != null) {
			if(n.value == i) {
				System.out.println("Found: " + n + "; head=" + head + "; tail=" + tail);
				return n;
			}
			n = n.next;
		}
		return null;
	}
	
	public Node searchRec(int i) {
		return searchRec(head, i);
	}
	
	public void deleteRec(int i) {
		deleteRec(head, i);
	}
	
	private Node deleteRec(Node n, int i) {
		if(n == null) {
			return null;
		}
		
		if (n.value == i) {
			Node tmp = n.next;
			if(n == head) {
				System.out.println("Setting new head: " + n.next);
				head = n.next;
			}
			if(n == tail) {
				tail = null;
			}
			n = null;
			return tmp;
		}
		
		n.next = deleteRec(n.next, i);
		if(n.next == null) {
			System.out.println("Setting new tail: " + n);
			tail = n;
		}
		return n;
	}
	
	private Node searchRec(Node n, int i) {
		if(n == null) {
			return null;
		}
		if(n.value == i) {
			System.out.println("Found (recursive): " + n + "; head=" + head + "; tail=" + tail);
			return n;
		}
		return searchRec(n.next, i);
	}
	
	public void delete(int i) {
		Node n = head;
		Node pre = null;
		
		while(n != null && n.value != i) {
			pre = n;
			n = n.next;
		}

		if (n == null) {
			return;
		}
		
		System.out.println("Found: " + n + "; head=" + head + "; tail=" + tail);
		if(pre != null) {
			pre.next = n.next;
		}			
		if(n == head) {
			head = n.next;
		}
		n = null;
	}
	
	public void deleteFirst() {
		if(head == null) {
			return;
		}
		Node toDelete = head;
		head = toDelete.next;
		toDelete = null;
	}
	
	public static void exchangeValues(Node n1, Node n2) {
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
