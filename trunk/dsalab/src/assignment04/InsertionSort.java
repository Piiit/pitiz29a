package assignment04;

import tools.TestableAlgorithm;

public class InsertionSort implements TestableAlgorithm {
	
	private int[] array;

	public void sort(int from, int to) throws Exception {
//		if(from > to || from < 0 || to >= array.length) {
//			throw new Exception("Out of bounds!");
//		}

		sort(this.array, from, to);
	}

	public InsertionSort(int[] array) {
		this.array = array;
	}
	
	public static void sort(int[] array, int from, int to) {
		for(int i = from+1; i < to; i++) {
			int j = i - 1;
			int current = array[i];
			while(j >= from && array[j] > current) {
				array[j+1] = array[j];
				j--;
			}
			array[j+1] = current;
		}	
	}

	@Override
	public void execute() throws Exception {
		sort(0, array.length-1);
	}

	@Override
	public String getName() {
		return "InsertionSort";
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
