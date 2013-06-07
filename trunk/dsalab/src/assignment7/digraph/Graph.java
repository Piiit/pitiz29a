package assignment7.digraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Graph {

	private NodeList nodes = new NodeList();
	private NodeList newNodes = new NodeList();
	private int time = 0;
	
	public Graph(NodeList nodes, int time) {
		this.nodes = nodes;
		this.time = time;
	}

	public Graph() {
	}

	private static String readFile(String filename) throws IOException {
		Scanner scanner = new Scanner(new File(filename));
		String content = scanner.useDelimiter("\\A").next();
		scanner.close();
		return content;
    }
	
	private static void writeFile(String filename, String content) throws FileNotFoundException {
		PrintWriter out = new PrintWriter(filename);
		out.println(content);
		out.close();
	}
	
	private void addEdge(int i, int j, NodeList list) {
		Node iNode = list.findOrMake(i);
		Node jNode = list.findOrMake(j);
		iNode.addAdjacent(jNode);
	}
	
	public void addEdge(int i, int j) {
		addEdge(i, j, nodes);
	}

	public void readFromFile(String file) throws Exception {
		File f = new File(file);
		if (!f.exists() || !f.canRead() || !f.isFile()) {
			throw new IOException("Can not read file " + f.getCanonicalPath());
		}
		
		String content = readFile(file);
		Scanner scanner = null;
		
		NodeList tmp = new NodeList();
		try {
			scanner = new Scanner(content);
			while(scanner.hasNextLine()) {
				String[] numbers = scanner.nextLine().trim().split(",");
				try {
					if(numbers.length == 1) {
						tmp.findOrMake(Integer.valueOf(numbers[0]));
					} else if(numbers.length == 2) {
						addEdge(Integer.valueOf(numbers[0]), Integer.valueOf(numbers[1]), tmp);
					} else {
						throw new Exception("Invalid graph file");
					}
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				}
			}
		} finally {
			if(scanner != null) {
				scanner.close();
			}
		}
		
		nodes.deleteAll();
		nodes = tmp;
	}
	
	public void printOnFile(String file) throws FileNotFoundException {
		writeFile(file, this.toString());
	}
	
	
	public Graph BSF() {
		resetGraph();
		HTListNode<Node> u = nodes.getRoot();
		while(u != null) {
			if(u.getData().getColor() == Node.WHITE) {
				BSF(u.getData());
			}
			u = u.getNext();
		}
		return new Graph(newNodes, time);
	}
	
	private void BSF(Node s) {
		s.setColor(Node.GRAY);
		s.setDistance(0);
		
		HTList<Node> queue = new HTList<Node>();
		queue.enqueue(s);
		newNodes.add(s);
		
		while(!queue.isEmpty()) {
			HTListNode<Node> u = queue.getHead();
			
			HTListNode<Node> v = u.getData().getAdjacentList().getRoot();
			while(v != null) {
				if(v.getData().getColor() == Node.WHITE) {
					v.getData().setColor(Node.GRAY);
					v.getData().setDistance(u.getData().getDistance() + 1);
					v.getData().setPred(u.getData());
					queue.enqueue(v.getData());
					newNodes.add(v.getData());
				}
				v = v.getNext();
			}
			
			queue.dequeue();
			u.getData().setColor(Node.BLACK);
			System.out.println(u.getData().getDescription() + u.getData().getDistance());
		}
	}
	
	
	private HTListNode<Node> nextWhiteNode(HTListNode<Node> u) {
		HTListNode<Node> n = u.getData().getAdjacentList().getRoot();
		while(n != null) {
			if(n.getData().getColor() == Node.WHITE) {
				return n;
			} 
			n = n.getNext();
		}
		return null;
	}
	
	public Graph itDSF() {
		resetGraph();
		HTList<Node> stack = new HTList<Node>();
		
		HTListNode<Node> n = nodes.getRoot();
		while(n != null) {
			if(n.getData().getColor() == Node.WHITE) {
				HTListNode<Node> u = n;
				while(u != null) {
					if(u.getData().getColor() == Node.WHITE) {
						time++;
						u.getData().setColor(Node.GRAY);
						u.getData().setStarttime(time);
						stack.push(u);
						
						while(!stack.isEmpty()) {
							time++;
							HTListNode<Node> v = nextWhiteNode(u);
							if(v != null) {
								v.getData().setColor(Node.GRAY);
								v.getData().setStarttime(time);
								v.getData().setPred(u.getData());
								if(nextWhiteNode(v) != null) {
									stack.push(v);
								}
								u = v;
							} else {
								if(u.getData().getColor() != Node.BLACK) {
									newNodes.add(u.getData());
									u.getData().setColor(Node.BLACK);
									u.getData().setEndtime(time);
								}
								if(!stack.isEmpty()) {
									u = stack.pop();
								}
								if(u.getData().getColor() == Node.GRAY) {
									stack.push(u);
								}
							}
						}
					}
					u = u.getNext();
				}
			}
			n = n.getNext();
		}
		return new Graph(newNodes, time);
	}
	
	public Graph recDSF() {
		DFS_all();
		return new Graph(newNodes, time);
	}
	
	private void DFS(Node u) {
		u.setColor(Node.GRAY);
		time++;
		u.setStarttime(time);

		newNodes.add(u);
		
		HTListNode<Node> v = u.getAdjacentList().getRoot();
		while(v != null) {
			if(v.getData().getColor() == Node.WHITE) {
				v.getData().setPred(u);
				DFS(v.getData());
			}
			v = v.getNext();
		}
		
		u.setColor(Node.BLACK);
		time++;
		u.setEndtime(time);
	}
	
	private void resetGraph() {
		HTListNode<Node> u = nodes.getRoot();
		newNodes.deleteAll();
		newNodes = new NodeList();
		while(u != null) {
			u.getData().setColor(Node.WHITE);
			u.getData().setPred(null);
			u.getData().setDistance(Node.INF);
			u = u.getNext();
		}
		time = 0;
		System.out.println("Resetting graph... DONE!");
	}
	
	private void DFS_all() {
		resetGraph();
		
		HTListNode<Node> u = nodes.getRoot();
		while(u != null) {
			if(u.getData().getColor() == Node.WHITE) {
				DFS(u.getData());
			}
			u = u.getNext();
		}
		
	}

	public void setDescriptions(String ... descriptions) {
		HTListNode<Node> u = nodes.getRoot();
		while(u != null) {
			try {
				u.getData().setDescription(descriptions[u.getData().getId()-1]);
			} catch (ArrayIndexOutOfBoundsException e) {
			}
			u = u.getNext();
		}
		
	}

	public void printTopologicalSort() {
		int x = 0;
		for(int i = time; i > 0; i--) {
			Node u = nodes.searchByEndtime(i);
			if(u != null) {
				x++;
				System.out.format("%2d: ID=%d, DESC=%s\n", x, u.getId(), u.getDescription());
			}
		}
		
	}

	@Override
	public String toString() {
		HTListNode<Node> n = nodes.getRoot();
		String out = "";
		while(n != null) {
			String adjacents = n.getData().getAdjacentListAsString();
			if(adjacents.length() != 0) {
				out += n.getData().getId() + ":" + n.getData().getAdjacentListAsString() + "\n";
			}
			n = n.getNext();
		}
		return out;
	}
	
}
