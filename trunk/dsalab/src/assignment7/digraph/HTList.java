package assignment7.digraph;

public class HTList<T> {
	
	HTListNode<T> head;
	HTListNode<T> tail;
	
	public HTList() {
		head = null;
		tail = null;
	}
	
	public boolean isEmpty() {
		return (head == null);
	}
	
	public void deleteFirst() {
		if(head == null) {
			return;
		}
		HTListNode<T> toDelete = head;
		head = toDelete.getNext();
		toDelete = null;
	}

	public void deleteAll() {
		if(head == null) {
			return;
		}
		HTListNode<T> n = head;
		while(n != null) {
			deleteFirst();
		}
	}
	
	public void insertFirst(T i) {
		HTListNode<T> n = new HTListNode<T>();
		n.setData(i);
		n.setNext(head);
		if(head == null) {
			tail = n;
		} 
		head = n;
	}
	
	public void insertLast(T i) {
		HTListNode<T> n = new HTListNode<T>();
		n.setData(i);
		if(head == null) {
			head = n;
		} else {
			tail.setNext(n);
		}
		tail = n;
	}
	
	public void print() {
		HTListNode<T> n = head;
		int i = 0;
		while(n != null) {
			System.out.println(i + ":" + n);
			n = n.getNext();
			i++;
		}
		System.out.println("Head=" + head + "; tail=" + tail);
	}
	
	public void delete(HTListNode<T> node) {
		HTListNode<T> n = head;
		HTListNode<T> pre = null;
		
		while(n != null && n.getData() != node.getData()) {
			pre = n;
			n = n.getNext();
		}

		if (n == null) {
			return;
		}
		
		System.out.println("Found: " + n + "; head=" + head + "; tail=" + tail);
		if(pre != null) {
			pre.setNext(n.getNext());
		}			
		if(n == head) {
			head = n.getNext();
		}
		n = null;
	}
	
}
