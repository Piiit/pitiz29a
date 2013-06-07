package assignment7.digraph;

public class HTList<T> {
	
	private HTListNode<T> head;
	private HTListNode<T> tail;
	
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
		while(head != null) {
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
	
	@Override
	public String toString() {
		if(head == null) {
			return "";
		}
		HTListNode<T> n = head;
		String out = "";
		while(n != null) {
			out += n + ",";
			n = n.getNext();
		}
		return out.substring(0,out.length() - 1);
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
		
//		System.out.println("Found: " + n + "; head=" + head + "; tail=" + tail);
		if(pre != null) {
			pre.setNext(n.getNext());
		}			
		if(n == head) {
			head = n.getNext();
		}
		n = null;
	}
	
	public HTListNode<T> getHead() {
		return head;
	}

	public HTListNode<T> getTail() {
		return tail;
	}
	
	public void push(HTListNode<T> node) {
		insertFirst(node.getData());
	}
	
	public HTListNode<T> pop() {
		HTListNode<T> tmp = head;
		deleteFirst();
		return tmp;
	}

	public void enqueue(T node) {
		insertLast(node);
	}
	
	public HTListNode<T> dequeue() {
		return pop();
	}


	
}
