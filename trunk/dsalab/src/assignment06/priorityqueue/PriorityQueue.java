package assignment06.priorityqueue;

public class PriorityQueue {
	
	private Node root;
	
	private void heapify(Node node) {
		Node l = node.left;
		Node r = node.right;
		Node nodeMax = null;
		
		if(l != null && l.key > node.key) {
			nodeMax = l;
		} else {
			nodeMax = node;
		}
		if(r != null && r.key > nodeMax.key) {
			nodeMax = r;
		}
		if(nodeMax != node) {
			int tmp = node.key;
			node.key = nodeMax.key;
			nodeMax.key = tmp;
			heapify(nodeMax);
		}
	}
	
	private void updateCountersOnDelete(Node deletedNode) {
		if(deletedNode == null) {
			return;
		}
		Node node = deletedNode;
		while(node.parent != null) {
			if(node.parent.left == node) {
				node.parent.lcount--;
			} else {
				node.parent.rcount--;
			}
			node = node.parent;
		}
	}
	
	private void updateCountersOnInsert(Node insertedNode) {
		if(insertedNode == null) {
			return;
		}
		Node node = insertedNode;
		while(node.parent != null) {
			if(node.parent.left == node) {
				node.parent.lcount++;
			} else {
				node.parent.rcount++;
			}
			node = node.parent;
		}
	}
	
	// Extracts one leaf at height 0
	// ! Keeps the root node, also if this is the only node...
	private Node extractLeaf() {
		Node testNode = root;
		Node node = null;
		while(testNode != null) {
			node = testNode;
			if(testNode.lcount > testNode.rcount) {
				testNode = testNode.left;
			} else {
				testNode = testNode.right;
			}
		}		
		
		if(node.parent != null) {
			updateCountersOnDelete(node);
			if(node.parent.left == node) {
				node.parent.left = null;
			} else {
				node.parent.right = null;
			}
			node.parent = null;
		}
		
		return node;
	}
	
	public boolean isEmpty() {
		return (root == null);
	}
	
	public void insert(int value) {

		if(root == null) {
			root = new Node(value, null);
			return;
		}
		
		Node testNode = root;
		Node node = null;
		while(testNode != null) {
			node = testNode;
			if(testNode.lcount <= testNode.rcount) {
				testNode = testNode.left;
			} else {
				testNode = testNode.right;
			}
		}
		
		if(node.left == null) {
			node.left = new Node(value, node);
			node = node.left;
		} else if(node.right == null) {
			node.right = new Node(value, node);
			node = node.right;
		}
		updateCountersOnInsert(node);
		
		while(node.parent != null && node.parent.key < value) {
			node.key = node.parent.key;
			node = node.parent;
		}
		
		node.key = value;
	}	
	
	public int extractMax() throws Exception {
		
		if(isEmpty()) {
			throw new Exception("Priority queue is empty. Can not extract a maximum!");
		}
		
		int max = root.key;
		Node leaf = extractLeaf();
		root.key = leaf.key;
		heapify(root);
		if(leaf == root) {
			root = null;
		}
		return max;
	}
	
}
