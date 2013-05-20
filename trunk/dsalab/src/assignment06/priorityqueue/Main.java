package assignment06.priorityqueue;

public class Main {

	public static void main(String[] args) {

		PriorityQueue q = new PriorityQueue();
		
		q.insert(2);
		q.insert(5);
		q.insert(7);
		q.insert(1);
		q.insert(16);
		q.insert(14);
		q.insert(10);
		
		try {
			System.out.println(q.extractMax());
			System.out.println(q.extractMax());
			System.out.println(q.extractMax());
			System.out.println(q.extractMax());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
