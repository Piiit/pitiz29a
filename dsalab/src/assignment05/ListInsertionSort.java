package assignment05;

import tools.TestableAlgorithm;

public class ListInsertionSort implements TestableAlgorithm {

	private HTDLList testList;
	private int[] testArray;
	
	public static void sort(HTDLList list) {
		
		if(list.isEmpty()) {
			return;
		}
		
		DLLNode current = list.head;
		DLLNode inside = null;
		
		while(current.next != null) {
			current = current.next;
			inside = current.prev;
			int curValue = current.value;
			while(inside != null && inside.value > curValue) {
				HTDLList.exchangeValues(inside, inside.next);
				inside = inside.prev;
			}
		}
	}

	@Override
	public void execute() throws Exception {
		sort(testList);
	}

	@Override
	public String getName() {
		return "InsertionSort(List)";
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
