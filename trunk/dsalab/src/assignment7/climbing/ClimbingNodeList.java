package assignment7.climbing;

import assignment7.digraph.HTList;
import assignment7.digraph.HTListNode;

public class ClimbingNodeList {
	
	HTList<ClimbingNode> nodes = new HTList<ClimbingNode>();
	
	public ClimbingNode get(int height, int width) {
		HTListNode<ClimbingNode> n = nodes.getHead();
		while(n != null) {
			ClimbingNode curData = n.getData();
			if(curData.height == height && curData.width == width) {
				return curData;
			}
			n = n.getNext();
		}
		return null;
	}
	
	public void add(ClimbingNode n) {
		if(n == null) {
			return;
		}
		nodes.insertLast(n);
	}
	
	public boolean isEmpty() {
		return nodes.isEmpty();
	}

	@Override
	public String toString() {
		return nodes.toString();
	}
		
}
