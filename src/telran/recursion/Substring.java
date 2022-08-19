//package telran.recursion;
//
//public class Substring {
//	/**  *   * @param str  
//	/ * * @param substr  
//	/* @return true if a given 'substr' is indeed the substring of a given string  
//	/ * * Challenges: 1. To apply only following methods of the class String:   
//	/ * charAt(int ind); String substring(int ind); int length(); 
//	/ * 2. No cycles;    */ 
//	public static boolean isSubstring(String str, String substr) {
//		int i = 0;
//		int j = 0;
//		char c  = substr.charAt(j);
//		isSubstringNew(str, c, i);
//		return false; 
//	}
//
//	private static void isSubstringNew(String str, String c, int i) {
//		if(i == str.length()) {
//			return false;
//		} else {
//			if(str.charAt(i) == c) {
//				String str2 = str.substring(i);
//				substringRecur(str2);
//			} else {
//				isSubstringNew(++i);
//			}
//		}
//		
//	}
//}
