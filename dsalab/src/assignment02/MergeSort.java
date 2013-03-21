package assignment02;

import java.math.BigInteger;

public class MergeSort {
	
	public static void sort(int[] array) {
		if(array == null) {
			throw new NullPointerException("Invalid array");
		}
		mergeSort(array, 0, array.length-1);
	}
	
	public static void sort(BigInteger[] array) {
		if(array == null) {
			throw new NullPointerException("Invalid array");
		}
		mergeSort(array, 0, array.length-1);
	}
	
	private static void mergeSort(BigInteger[] array, int l, int r) {
		if(l < r) {
			int m = (l+r)/2;
			mergeSort(array, l, m);
			mergeSort(array, m+1, r);
			merge(array, l, m, r);
		}
	}
	
	private static void merge(BigInteger[] array, int l, int m, int r) {
		BigInteger[] result = new BigInteger[r - l + 1];
		int i = l;
		int j = m+1;
		int ri = 0;
		while(i <= m && j <= r) {
			if(array[i].compareTo(array[j]) == -1) {
				result[ri] = array[i];
				i++;
			} else {
				result[ri] = array[j];
				j++;
			}
			ri++;
		}
		while(i <= m) {
			result[ri] = array[i];
			i++;
			ri++;
		}
		while(j <= r) {
			result[ri] = array[j];
			j++;
			ri++;
		}
		for(i = 0; i < result.length; i++) {
			array[i + l] = result[i];
		}
		
	}
	
	private static void mergeSort(int[] array, int l, int r) {
		if(l < r) {
			int m = (l+r)/2;
			mergeSort(array, l, m);
			mergeSort(array, m+1, r);
			merge(array, l, m, r);
		}
	}
	
	private static void merge(int[] array, int l, int m, int r) {
		int[] result = new int[r - l + 1];
		int i = l;
		int j = m+1;
		int ri = 0;
		while(i <= m && j <= r) {
			if(array[i] < array[j]) {
				result[ri] = array[i];
				i++;
			} else {
				result[ri] = array[j];
				j++;
			}
			ri++;
		}
		while(i <= m) {
			result[ri] = array[i];
			i++;
			ri++;
		}
		while(j <= r) {
			result[ri] = array[j];
			j++;
			ri++;
		}
		for(i = 0; i < result.length; i++) {
			array[i + l] = result[i];
		}
	}
}
