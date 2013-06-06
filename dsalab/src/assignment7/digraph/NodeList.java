package assignment7.digraph;

public class NodeList {

	HTList<Node> nodes = new HTList<Node>();
	
	public Node search(int id) {
		HTListNode<Node> n = nodes.head;
		while(n != null) {
			Node curData = n.getData();
			if(curData.id == id) {
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
		HTListNode<Node> n = nodes.head;
		while(n != null) {
			out += n.getData().id + ",";
			n = n.getNext();
		}
		return out.substring(0, out.length() - 1);
	}

	public void deleteAll() {
		nodes.deleteAll();
	}
	
	public HTListNode<Node> getRoot() {
		return nodes.head;
	}
	
	public void add(Node n) {
		nodes.insertLast(n);
	}

}
