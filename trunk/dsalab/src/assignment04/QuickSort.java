package assignment04;

import tools.TestableAlgorithm;

public class QuickSort implements TestableAlgorithm {
	
	private int[] array;
	
	public QuickSort(int[] array) {
		this.array = array;
	}
	
	public QuickSort() {
		
	}

	public void sort() throws Exception {
		sort(0, array.length-1);
	}
	
	private void sort(int l, int r) {
		if (l < r) {
			int m = partition(l, r);
			sort(l, m-1);
			sort(m, r);
		}		
	}
	
	private int partition(int l, int r) {
		int pivot = array[r];
		int i = l - 1;
		int j = r + 1;
		while (i < j) {
			do {
				j--;
			} while (array[j] > pivot);
			do {
				i++;
			} while (array[i] < pivot);
			if (i < j) {
				swap(i, j);
			}
		}
		return i;
	}
	
	private void swap(int i, int j) {
		int tmp = array[i];
		array[i] = array[j];
		array[j] = tmp;
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
