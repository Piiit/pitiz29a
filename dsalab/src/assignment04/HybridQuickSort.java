package assignment04;

import tools.Algorithm;

public class HybridQuickSort implements Algorithm {
	
	private int[] array;
	private int k = 0;
	
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
	
	private void hybridQuickSort(int l, int r) throws Exception {
		if (l < r) {
			if (r - l < k) {
				InsertionSort iSort = new InsertionSort(array);
				iSort.sort(l, r);
			} else {
				int m = partition(l, r);
				hybridQuickSort(l, m-1);
				hybridQuickSort(m, r);
			}
		}
	}
	
	public void sort() throws Exception {
		hybridQuickSort(0, array.length-1);
	}
	

	public HybridQuickSort(int[] a, int k) {
		array = a;
		this.k = k;
	}
	
	public HybridQuickSort(int k) {
		this.k = k;
	}

	@Override
	public void execute() throws Exception {
		sort();
	}

	@Override
	public String getName() {
		return "HybridQuickSort(k=" + k + ")";
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
	
	public void setK(int k) {
		this.k = k;
	}

}
