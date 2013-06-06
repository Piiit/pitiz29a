package assignment7.digraph;

public class Main {

	public static void main(String[] args) {
		Graph g = new Graph();
		try {
			g.readFromFile("src/assignment7/digraph/graph.txt");
			g.printOnFile("src/assignment7/digraph/graph_output.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

}
