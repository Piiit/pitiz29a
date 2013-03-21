package assignment02;

import java.math.BigInteger;

public class InsertionSort {

	public static void sort(int[] array) {
		for(int i = 1; i < array.length; i++) {
			int j = i - 1;
			int current = array[i];
			while(j >= 0 && array[j] > current) {
				array[j+1] = array[j];
				j--;
			}
			array[j+1] = current;
		}
	}
	
	public static void sort(BigInteger[] array) {
		for(int i = 1; i < array.length; i++) {
			int j = i - 1;
			BigInteger current = array[i];
			while(j >= 0 && array[j].compareTo(current) == -1) {
				array[j+1] = array[j];
				j--;
			}
			array[j+1] = current;
		}
	}
}
