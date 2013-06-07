package assignment7.digraph;

public class NodeList {

	HTList<Node> nodes = null;
	
	public NodeList() {
		nodes = new HTList<Node>();
	}

	public Node search(int id) {
		HTListNode<Node> n = nodes.getHead();
		while(n != null) {
			Node curData = n.getData();
			if(curData.getId() == id) {
				return curData;
			}
			n = n.getNext();
		}
		return null;
	}
	
	public Node findOrMake(int id) {
		Node n = search(id);
		if(n == null) {
			n = new Node(id);
			nodes.insertLast(n);
		}
		return n;
	}
	
	@Override
	public String toString() {
		if(nodes.isEmpty()) {
			return "";
		}
		
		String out = "";
		HTListNode<Node> n = nodes.getHead();
		while(n != null) {
			out += n.getData().getId() + ",";
			n = n.getNext();
		}
		return out.substring(0, out.length() - 1);
	}

	public void deleteAll() {
		nodes.deleteAll();
	}
	
	public HTListNode<Node> getRoot() {
		return nodes.getHead();
	}
	
	public void add(Node n) {
		nodes.insertLast(n);
	}

	public Node searchByEndtime(int endtime) {
		HTListNode<Node> n = nodes.getHead();
		while(n != null) {
			Node curData = n.getData();
			if(curData.getEndtime() == endtime) {
				return curData;
			}
			n = n.getNext();
		}
		return null;
	}
	
	public void push(Node n) {
		nodes.insertFirst(n);
	}
	
	public Node pop() {
		Node first = nodes.getHead().getData();
		nodes.deleteFirst();
		return first;
	}
	
	public boolean isEmpty() {
		return nodes.isEmpty();
	}
	
}
