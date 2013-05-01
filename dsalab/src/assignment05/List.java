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
		n.suc = root;
		root = n;
	}
	
	//TODO
	public void insertLast(int i) {
		Node n = new Node(i);
		if(root == null) {
			root = n;
		} 
	}
	
	//TODO
	public void insertLastRec(int i) {
	}
	
	public void print() {
		Node n = root;
		int i = 0;
		while(n != null) {
			System.out.println(i + ":" + n.value);
			n = n.suc;
			i++;
		}
		System.out.println("Head=" + root);
	}
	
	public void printRec() {
		printRec(root, 0);
		System.out.println("Head=" + root);
	}
	
	private void printRec(Node n, int i) {
		if(n == null) {
			return;
		}
		System.out.println(i + ":" + n.value);
		printRec(n.suc, ++i);
	}
	
	public Node search(int i) {
		Node n = root;
		while(n != null) {
			if(n.value == i) {
				System.out.println("Found: " + n + "; root=" + root);
				return n;
			}
			n = n.suc;
		}
		return null;
	}
	
	public Node searchRec(int i) {
		return searchRec(root, i);
	}
	
	public void deleteRec(int i) {
		deleteRec(root, i);
	}
	
	private Node deleteRec(Node n, int i) {
		if(n == null) {
			return null;
		}
		
		if (n.value == i) {
			Node tmp = n.suc;
			if(n == root) {
				System.out.println("Setting new root: " + n.suc);
				root = n.suc;
			}
			n = null;
			return tmp;
		}
		
		n.suc = deleteRec(n.suc, i);
		return n;
	}
	
	private Node searchRec(Node n, int i) {
		if(n == null) {
			return null;
		}
		if(n.value == i) {
			System.out.println("Found (recursive): " + n + "; root=" + root);
			return n;
		}
		return searchRec(n.suc, i);
	}
	
	public void delete(int i) {
		Node n = root;
		Node pre = null;
		
		while(n != null && n.value != i) {
			pre = n;
			n = n.suc;
		}

		if (n == null) {
			return;
		}
		
		System.out.println("Found: " + n + "; root=" + root);
		if(pre != null) {
			pre.suc = n.suc;
		}			
		if(n == root) {
			root = n.suc;
		}
		n = null;
	}
	
	public void deleteFirst() {
		if(root == null) {
			return;
		}
		Node toDelete = root;
		root = toDelete.suc;
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
