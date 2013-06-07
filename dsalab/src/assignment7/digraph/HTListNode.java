package assignment7.digraph;

public class HTListNode<T> {

	private HTListNode<T> next;
	private T data;
	
	public HTListNode<T> getNext() {
		return next;
	}
	
	public void setNext(HTListNode<T> next) {
		this.next = next;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return data.toString();
	}
	
}
