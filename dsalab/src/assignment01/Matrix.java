package assignment01;

public class Matrix {
	
	private static float[] A = {7, 5, 8, 2};

	private static float average(float[] A, int from, int to) {
		if(A.length == 0 || from > to) {
			return 0;
		}
		float sum = 0;
		for(int i = from; i <= to; i++) {
			sum += A[i];
		}
		return sum / (to-from+1);
	}
	
	private static float[][] matrix(float[] A) throws Exception {
		if(A.length == 0) {
			throw new Exception("Use a valid matrix!");
		}
		float[][] M = new float[A.length][A.length];
		
		for(int i = 0; i < A.length; i++) {
			for(int j = 0; j < A.length; j++) {
				if(i > j) {
					M[i][j] = 0;
				} else {
					M[i][j] = average(A, i, j);
				}
			}
		}
		return M;
	}
	
	public static void main(String args[]) {
		try {
			float[][] B = matrix(A);
			for(int i = 0; i < A.length; i++) {
				for(int j = 0; j < A.length; j++) {
					System.out.print(Float.toString(B[i][j]) + "/t");
				}
				System.out.println();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
