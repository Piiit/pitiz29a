package assignment05;

public class TestList {

	public static void main(String[] args) {
		HTList list = new HTList();
		list.addRandom(10000000, 0, 10000000);
		
//		System.out.println("Print...");
//		list.print();
//
//		System.out.println("PrintRec...");
//		list.printRec();
		
		QuickSort.sort(list);
		System.out.println("Sorted... ");
//		list.print();
		
	}

}
