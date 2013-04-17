package assignment04;

import tools.Algorithm;

public class QuickSort implements Algorithm {
	
	private int[] array;
	
	public QuickSort(int[] array) {
		this.array = array;
	}
	
	public QuickSort() {
		
	}

	public void sort() throws Exception {
		HybridQuickSort hqSort = new HybridQuickSort(array, -1); 
		hqSort.sort();
	}

	@Override
	public void execute() throws Exception {
		sort();
	}

	@Override
	public String getName() {
		return "QuickSort";
	}

	@Override
	public int[] getArray() {
		return array;
	}

	@Override
	public int getElementCount() {
		return array.length;
	}

	@Override
	public void setArray(int[] array) {
		this.array = array;
	}

}
