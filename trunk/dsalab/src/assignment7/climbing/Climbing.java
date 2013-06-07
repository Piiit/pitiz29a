package assignment7.climbing;

import assignment7.digraph.HTList;
import assignment7.digraph.HTListNode;

public class Climbing {

	private Wall wall;

	public Climbing(Wall wall) {
		this.wall = wall;
	}
	
	private int min(int ... values) {
		
		if(values.length < 2) {
			throw new IllegalArgumentException("min needs at least 2 numbers to compare!");
		}
		
		int result = values[0];
		
		for(int v : values) {
			if(v < result) {
				result = v;
			}
		}
		
		return result;
	}

	/* RECURSIVE SOLUTION */
	private int searchRec(int height, int width) {
		if(height == wall.getMaxHeight()) {
			return wall.getOrInfinite(height, width);
		}
		
		int value1 = wall.getOrInfinite(height, width) + searchRec(height+1, width);
		int value2 = wall.getOrInfinite(height, width) + searchRec(height+1, width-1);
		int value3 = wall.getOrInfinite(height, width) + searchRec(height+1, width+1);
		
		int min = min(value1, value2, value3);
		
		ClimbingNode curNode = wall.getNode(height, width);
		if (curNode != null) {
			if (min == value1) {
				curNode.ancestor = wall.getNode(height+1, width);
			} else if (min == value2) {
				curNode.ancestor = wall.getNode(height+1, width-1);
			} else {
				curNode.ancestor = wall.getNode(height+1, width+1);
			}
		}
		
		return min;
	}
	
	public ClimbingNode getBestRoute() {
		int min = searchRec(1, 1);
		int mini = 1;
		ClimbingNode[] paths = new ClimbingNode[wall.getMaxWidth()];
		paths[0] = wall.copyPath(wall.getNode(1, 1));
		for(int i = 2; i <= wall.getMaxWidth(); i++) {
			int cost = searchRec(1, i);
			paths[i-1] = wall.copyPath(wall.getNode(1, i));
			mini = cost < min ? i : mini;
			min = cost < min ? cost : min;
		}
		
		return paths[mini];
	}
	
	/* DYNAMIC PROGRAMMING SOLUTION */
	
	//Build a digraph out of the climbing wall data...
	public ClimbingNode buildGraph() {
		ClimbingNode root = new ClimbingNode(0, 0, 0);
		
		// Add all nodes at level 1 to the root...
		for(int w = 1; w <= wall.getMaxWidth(); w++) {
			root.adj.add(wall.getNode(1, w));
		}
		
		// At higher levels, add only the three upper nodes...
		for(int h = 1; h <= wall.getMaxHeight(); h++) {
			for(int w = 1; w <= wall.getMaxWidth(); w++) {
				wall.getNode(h, w).adj.add(wall.getNode(h+1, w-1));
				wall.getNode(h, w).adj.add(wall.getNode(h+1, w));
				wall.getNode(h, w).adj.add(wall.getNode(h+1, w+1));
			}
		}
		
		return root;
	}

	public ClimbingNode searchBFS() {
		ClimbingNode root = buildGraph();
		ClimbingNode top = BSF(root);

		//Remove helper node from base nodes...
		for(int w = 1; w <= wall.getMaxWidth(); w++) {
			wall.getNode(1, w).ancestor = null;
		}
		
		return top;
	}
	
	private ClimbingNode BSF(ClimbingNode s) {
		s.color = ClimbingNode.GRAY;
		s.distance = 0;
		
		HTList<ClimbingNode> queue = new HTList<ClimbingNode>();
		queue.enqueue(s);
		
		while(!queue.isEmpty()) {
			HTListNode<ClimbingNode> u = queue.getHead();
			
			HTListNode<ClimbingNode> v = u.getData().adj.nodes.getHead();
			while(v != null) {
				if(v.getData().color == ClimbingNode.WHITE || v.getData().distance > u.getData().distance + v.getData().value) {
					v.getData().color = ClimbingNode.GRAY;
					v.getData().distance = u.getData().distance + v.getData().value;
//					System.out.println(v + " distance = " + v.getData().distance);
					v.getData().ancestor = u.getData();
					queue.enqueue(v.getData());
				}
				v = v.getNext();
			}
			
			queue.dequeue();
			u.getData().color = ClimbingNode.BLACK;
		}
		
		int min = wall.getNode(wall.getMaxHeight(), 1).distance;
		int mini = 1;
		for(int i = 2; i <= wall.getMaxWidth(); i++) {
			ClimbingNode n = wall.getNode(wall.getMaxHeight(), i);
			if(min > n.distance) {
				min = n.distance;
				mini = i;
			}
		}
		return wall.getNode(wall.getMaxHeight(), mini);
	}
	
}
