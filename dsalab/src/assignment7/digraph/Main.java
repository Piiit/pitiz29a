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
		
		g.setDescriptions("shirt", "shorts", "socks", "tie", "trousers", "shoes", "jacket", "belt", "watch");
		
		Graph g2 = g.itDSF();
		g2.printTopologicalSort();

		Graph g3 = g.recDSF();
		g3.printTopologicalSort();
		
		Graph g4 = g.BSF();
		System.out.println(g4);

	}

}
