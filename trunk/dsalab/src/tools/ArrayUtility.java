package tools;

import java.math.BigInteger;
import java.util.Random;

public class ArrayUtility {
    
    public static int findMax(int[] A) throws Exception {
        return A[findMaxPos(A)];
    }
    
    public static int findMaxPos(int[] A) throws Exception {
         if(A == null || A.length == 0) {
            throw new Exception("No elements in array.");
        }
        int max = A[0];
        int pos = 0;
        for(int i = 1; i < A.length; i++) {
            if(max < A[i]) {
                max = A[i];
                pos = i;
            }
        }
        return pos;
    }
    
    public static int findMaxPosBounded(int[] A, int from, int to) throws Exception {
        if(A == null || A.length == 0) {
           throw new Exception("No elements in array.");
       }
       int max = A[from];
       int pos = from;
       for(int i = from+1; i < to; i++) {
           if(max < A[i]) {
               max = A[i];
               pos = i;
           }
       }
       return pos;
   }
    
    public static int findMinPos(int[] A) throws Exception {
        if(A == null || A.length == 0) {
            throw new Exception("No elements in array.");
        }
        int min = A[0];
        int pos = 0;
        for(int i = 1; i < A.length; i++) {
            if(min > A[i]) {
                min = A[i];
                pos = i;
            }
        }
        return pos;
    }
    
    public static int findMin(int[] A) throws Exception {
        return A[findMinPos(A)];
    }
    
    public static void swap(int[] A, int i, int j) throws Exception {
        if(A == null || A.length == 0) {
            throw new Exception("No elements in array.");
        }
        int temp = A[i];
        A[i] = A[j];
        A[j] = temp;
    }
    
    public static void print(int[] A) {
        for(int i = 0; i < A.length; i++) {
            System.out.print(A[i] + " ");
        }
        System.out.println();
    }
    
    public static void shiftRight(int[] A, int i, int j) throws Exception {
        if(A == null || A.length == 0) {
            throw new Exception("No elements in array.");
        }
        if(i > j) {
            throw new Exception("Lower index must be smaller or equal than upper index!");
        }
        for(int idx = j; idx > i; idx--) {
            A[idx] = A[idx-1]; 
        }
        A[i] = 0;
    }
    
    public static void shiftLeft(int[] A, int i, int j) throws Exception {
        if(A == null || A.length == 0) {
            throw new Exception("No elements in array.");
        }
        if(i > j) {
            throw new Exception("Lower index must be smaller or equal than upper index!");
        }
        for(int idx = i; idx < j; idx++) {
            A[idx] = A[idx+1]; 
        }
        A[j] = 0;
    }
    
    public static int[] createRandomArray(int size, int min, int max) throws Exception {
    	if(size < 1) {
    		throw new Exception("Size must be >= 1");
    	}
    	if(min >= max) {
            throw new Exception("Lower bound must be smaller than upper bound!");
        }
    	
    	int[] array = new int[size];
    	
    	for(int i = 0; i < size; i++) {
    		array[i] = getRandomInt(min, max);
    	}
    	
    	return array;
    }
    
    public static BigInteger[] createRandomArrayBigInt(int size, int maxBits) throws Exception {
    	if(size < 1) {
    		throw new Exception("Size must be >= 1");
    	}
    	
    	BigInteger[] array = new BigInteger[size];
    	
    	for(int i = 0; i < size; i++) {
    		array[i] = getRandomBigInt(maxBits);
    	}
    	
    	return array;
    }
    
    public static int getRandomInt(int min, int max) {
    	return (int)(Math.random() * (max - min + 1) + min);
    }
    
    public static BigInteger getRandomBigInt(int bits) {
    	return new BigInteger(bits, new Random());
    }
    
    public static int[][] createRandomMatrix(int rows, int cols, int min, int max) throws Exception {
    	if(rows < 1 || cols < 1) {
    		throw new Exception("Rows and cols must be >= 1");
    	}
    	if(min >= max) {
            throw new Exception("Lower bound must be smaller than upper bound!");
        }
    	
    	int[][] matrix = new int[cols][rows];
    	
    	for(int i = 0; i < cols; i++) {
    		for(int j = 0; j < rows; j++) {
    			matrix[i][j] = getRandomInt(min, max);
    		}
    	}
    	
    	return matrix;
    }
    
    public static int[] copyArray(int[] A) {
    	int[] B = new int[A.length];

    	for(int i = 0; i < A.length; i++) {
    		B[i] = A[i];
    	}
    	
    	return B;
    }
    
    public static int[][] copyMatrix(int[][] A) throws Exception {
    	if(A.length == 0 || A[0].length == 0) {
    		throw new Exception("No elements in array.");
    	}
    	int[][] B = new int[A.length][A[0].length];

    	for(int i = 0; i < A.length; i++) {
    		for(int j = 0; j < A[0].length; j++) {
    			B[i][j] = A[i][j];
    		}
    	}
    	return B;
    }
    
    public static int findElement(int[] A, int n) {
    	for(int i = 0; i < A.length; i++) {
    		if(n == A[i]) {
    			return i;
    		}
    	}
    	return -1;
    }

    public static int binarySearch(int[] A, int n) {
    	int left = 0;
    	int right = A.length;
    	
    	do {
        	int pos = (left+right) / 2;
        	if(A[pos] == n) {
        		return pos;
        	}
        	if(A[pos] > n) {
        		right = pos-1;
        	} else {
        		left = pos+1;
        	}
    	} while(left <= right);
    	return -1;
    }
    
    public static void maxSortWithShift(int[] A) throws Exception {
    	for(int i = 0; i < A.length; i++) {
    		int maxElementIndex = findMaxPosBounded(A, i, A.length);
    		int maxElement = A[maxElementIndex];
    		shiftRight(A, i, maxElementIndex);
    		A[i] = maxElement;
    	}
    }
    
    public static void maxSortWithSwap(int[] A) throws Exception {
    	for(int i = 0; i < A.length; i++) {
    		int maxElementIndex = findMaxPosBounded(A, i, A.length);
    		swap(A, i, maxElementIndex);
    	}
    }
    
    public static double median(double[] m) {
    	java.util.Arrays.sort(m);
        int middle = m.length / 2;
        if (m.length % 2 == 1) {
            return m[middle];
        } else {
            return (m[middle-1] + m[middle]) / 2.0;
        }
    }
}
