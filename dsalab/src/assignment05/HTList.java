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
		n.suc = head;
		if(head == null) {
			tail = n.suc;
		} 
		head = n;
	}
	
	public void insertLast(int i) {
		Node n = new Node(i);
		if(head == null) {
			head = n;
		} else {
			tail.suc = n;
		}
		tail = n;
	}
	
	//TODO insertLastRec
	public void insertLastRec(int i) {
	}
	
	public void print() {
		Node n = head;
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
	
	private void printRec(Node n, int i) {
		if(n == null) {
			return;
		}
		System.out.println(i + ":" + n.value);
		printRec(n.suc, ++i);
	}
	
	public Node search(int i) {
		Node n = head;
		while(n != null) {
			if(n.value == i) {
				System.out.println("Found: " + n + "; root=" + head + "; tail=" + tail);
				return n;
			}
			n = n.suc;
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
			Node tmp = n.suc;
			if(n == head) {
				System.out.println("Setting new head: " + n.suc);
				head = n.suc;
			}
			n = null;
			return tmp;
		}
		
		n.suc = deleteRec(n.suc, i);
		if(n.suc == null) {
			System.out.println("Setting new tail: " + n.suc);
			tail = n.suc;
		}
		return n;
	}
	
	private Node searchRec(Node n, int i) {
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
		Node n = head;
		Node pre = null;
		
		while(n != null && n.value != i) {
			pre = n;
			n = n.suc;
		}

		if (n == null) {
			return;
		}
		
		System.out.println("Found: " + n + "; root=" + head + "; tail=" + tail);
		if(pre != null) {
			pre.suc = n.suc;
		}			
		if(n == head) {
			head = n.suc;
		}
		n = null;
	}
	
	public void deleteFirst() {
		if(head == null) {
			return;
		}
		Node toDelete = head;
		head = toDelete.suc;
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
}
