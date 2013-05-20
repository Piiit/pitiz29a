package assignment06.hashing;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class Main {
	
	public static String readFile(String filename) {
	   String content = null;
	   File file = new File(filename);
	   try {
	       FileReader reader = new FileReader(file);
	       char[] chars = new char[(int) file.length()];
	       reader.read(chars);
	       content = new String(chars);
	       reader.close();
	   } catch (IOException e) {
	       e.printStackTrace();
	   }
	   System.out.println("Reading file: " + filename);
	   return content;
	}
	
	public static String cleanText(String text) {
		return text.replaceAll("[<>'=\\(\\)\\{\\}\\!\\?\\;\\-\\+\\.\\^\\:\\,0123456789\\/]", " ");
	}
	
	public static void testFile(String filename) {
		HashTable t1 = new HashTable();
		HashTable t11 = new HashTable();
		HashTable t111 = new HashTable();
		
		String text = readFile(filename);
		text = cleanText(text);
		
		StringTokenizer st = new StringTokenizer(text);
	     while (st.hasMoreTokens()) {
	    	 String token = st.nextToken().toLowerCase();
	         t1.insert(token.hashCode(), token);
	         if(token.length() >= 5) {
	        	 t11.insert(token.substring(0,4).hashCode(), token);
	         } else {
	        	 t11.insert(token.hashCode(), token);
	         }
	         t111.insert(checksum(token), token);
	     }
		
		System.out.println("a) " + t1);
		System.out.println("b) " + t11);
		System.out.println("c) " + t111);
		System.out.println();
	}
	
	//This algorithm is called Fletcher's checksum (Source: wikipedia.org)
	public static int checksum(String word) {
		int sum1 = 0;
		int sum2 = 0;
		for(int i = 0; i < word.length(); i++) {
			sum1 = (sum1 + word.charAt(i)) % 255;
			sum2 = (sum2 + sum1) % 255;
		}
		return (sum2 << 8) | sum1;
	}
	
	public static void main(String[] args) {
		
		System.out.println("Keys:\na) String.hashCode()\nb) String.substring(0,4).hashCode()\nc) Fletcher's checksum algorithm\n");
		
		testFile("src/assignment06/hashing/Genesis.txt");
		testFile("src/assignment06/hashing/RomeoJuliet.txt");
		testFile("src/assignment06/hashing/BananaQuality.txt");
		testFile("src/assignment06/hashing/test.txt");	

	}

}
