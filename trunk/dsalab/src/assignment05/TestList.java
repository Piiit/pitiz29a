package assignment05;

public class TestList {

	public static void main(String[] args) {
		HTList list = new HTList();
		list.insertLast(1);
		list.insertFirst(3);
		list.insertFirst(5);
		list.insertLast(10);
		list.insertLast(77);
		list.insertLast(12);
		list.insertLast(13);
		list.insertLast(11);
		list.insertLast(0);
		list.insertLast(2);
		
		System.out.println("Print...");
		list.print();

		System.out.println("PrintRec...");
		list.printRec();
		
		System.out.println("Sorted... ");
		QuickSort.sort(list);
		list.printRec();
		
		System.out.println(list.searchRec(10));
		
		list.deleteRec(10);
		list.printRec();
		list.deleteRec(5);
		list.printRec();
		list.deleteRec(3);
		list.printRec();
		list.deleteRec(1);
		list.printRec();
		
		
		
	}

}
