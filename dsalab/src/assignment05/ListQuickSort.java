package assignment05;

import tools.TestableAlgorithm;

public class ListQuickSort implements TestableAlgorithm {
	
	private HTList testList = new HTList();
	private int[] testArray;
	
	public static void sort(HTList list) {
		quickSort(list.head, list.tail);
	}
	
	private static void quickSort(Node left, Node right) {
		if (left != right) {
			Node pointer1_pre = left;
			Node pointer1 = left;
			Node pointer2 = left;
			
			int pivot = left.value;
			
			do {
				pointer2 = pointer2.next;
				if(pointer2.value < pivot) {
					pointer1_pre = pointer1;
					pointer1 = pointer1.next;
					HTList.exchangeValues(pointer1, pointer2);
				}
			} while (pointer2 != right);
			HTList.exchangeValues(left, pointer1);
			if(pointer1 != right) {
				pointer1 = pointer1.next;
			}
			quickSort(left, pointer1_pre);
			quickSort(pointer1, right);
		}
	}

	@Override
	public void execute() throws Exception {
		sort(testList);
	}

	@Override
	public String getName() {
		return "QuickSort(List)";
	}

	@Override
	public int[] getArray() {
		return testArray;
	}

	@Override
	public void setArray(int[] array) {
		testArray = array;
		testList.fromArray(array);
	}

	@Override
	public int getElementCount() {
		return testArray.length;
	}

}
