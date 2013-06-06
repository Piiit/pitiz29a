package assignment7.digraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Graph {

	private NodeList nodes = new NodeList();
	
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
				if(numbers.length != 2) {
					throw new Exception("Invalid graph file");
				}
				try {
					addEdge(Integer.valueOf(numbers[0]), Integer.valueOf(numbers[1]), tmp);
				} catch (Exception e) {
					e.printStackTrace();
					throw new Exception("Invalid graph file");
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
		HTListNode<Node> n = nodes.getRoot();
		String out = "";
		while(n != null) {
			String adjacents = n.getData().getAdjacentsString();
			if(adjacents.length() != 0) {
				out += n.getData().id + ":" + n.getData().getAdjacentsString() + "\n";
			}
			n = n.getNext();
		}
		writeFile(file, out);
	}
	
}
