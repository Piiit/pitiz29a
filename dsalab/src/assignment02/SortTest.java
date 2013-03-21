package assignment02;

import java.math.BigInteger;
import java.util.Arrays;

import tools.ArrayUtility;

public class SortTest {
	public static void main(String args[]) throws Exception {
		int[] array = ArrayUtility.createRandomArray(5, 0, 100);
		InsertionSort.sort(array);
		System.out.println(Arrays.toString(array));
		
		BigInteger[] array2 = ArrayUtility.createRandomArrayBigInt(5, 1024);
		InsertionSort.sort(array2);
		System.out.println(Arrays.toString(array2));
		
		int[] array3 = ArrayUtility.createRandomArray(5, 0, 100);
		MergeSort.sort(array3);
		System.out.println(Arrays.toString(array3));

		BigInteger[] array4 = {
				new BigInteger("5"), 
				new BigInteger("2"), 
				new BigInteger("7"), 
				new BigInteger("12"), 
				new BigInteger("0")
				};
		MergeSort.sort(array4);
		System.out.println(Arrays.toString(array4));
	}
}
