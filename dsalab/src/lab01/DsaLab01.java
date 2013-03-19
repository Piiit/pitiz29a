package lab01;

import tools.ArrayUtility;

public class DsaLab01 {

    public static void main(String[] args) {
        int[] A = {1, 3, 2, 5, 4};
        ArrayUtility.print(A);
        try {
            System.out.println(ArrayUtility.findMax(A));
            System.out.println(ArrayUtility.findMaxPos(A));
            System.out.println(ArrayUtility.findMin(A));
            System.out.println(ArrayUtility.findMinPos(A));
            
            ArrayUtility.swap(A, 1, 3);
            ArrayUtility.print(A);
            
            ArrayUtility.shiftRight(A, 0, 3);
            ArrayUtility.print(A);
            
            ArrayUtility.shiftLeft(A, 1, 3);
            ArrayUtility.print(A);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
