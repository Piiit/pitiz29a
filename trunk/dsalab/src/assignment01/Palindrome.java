package assignment01;

public class Palindrome {
	
	/**
	 * Assuming an empty string is a palindrome!
	 */
	
	private static String palindrome1 = "qwegewq";
	private static String palindrome2 = "qweewq";
	private static String palindrome3 = "";
	private static String palindrome4 = null;
	private static String palindrome5 = "notapalindrome";

	
	public static boolean checkPalindrome(String text) {
		
		if(text == null) {
			return false;
		}
		
		if(text.length() == 1 || text.length() == 0) {
			return true;
		}
		
		if (text.charAt(0) == text.charAt(text.length()-1)) {
			return checkPalindrome(text.substring(1, text.length()-1));
		} else {
			return false;
		}
		
	}
	
	public static void main(String args[]) {
		System.out.println(checkPalindrome(palindrome1));
		System.out.println(checkPalindrome(palindrome2));
		System.out.println(checkPalindrome(palindrome3));
		System.out.println(checkPalindrome(palindrome4));
		System.out.println(checkPalindrome(palindrome5));
	}

}
