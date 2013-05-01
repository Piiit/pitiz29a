package assignment05;

public class TestList {

	public static void main(String[] args) {
		HTDLList list = new HTDLList();
		list.insertLast(1);
		list.insertFirst(3);
		list.insertFirst(5);
		list.insertLast(10);

		list.print();
		
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
