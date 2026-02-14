import java.util.HashSet;
import java.util.Set;

// CS108 HW1 -- String static methods

public class StringCode {

	/**
	 * Given a string, returns the length of the largest run.
	 * A a run is a series of adajcent chars that are the same.
	 * @param str
	 * @return max run length
	 */
	public static int maxRun(String str) {
		if (str.isEmpty()) {
			return 0;
		}
		int ans = 1;
		int curr = 1;
		int c = str.charAt(0);
		for (int i = 1; i < str.length(); i++) {
			if (str.charAt(i) == c) {
				curr++;
			}
			else {
				ans = Math.max(ans, curr);
				curr = 1;
			}
			c = str.charAt(i);
		}
		ans =  Math.max(ans, curr);
		return ans;
	}

	
	/**
	 * Given a string, for each digit in the original string,
	 * replaces the digit with that many occurrences of the character
	 * following. So the string "a3tx2z" yields "attttxzzz".
	 * @param str
	 * @return blown up string
	 */
	public static String blowup(String str) {
		String ans = "";
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (Character.isDigit(c)) {
				if (i == str.length() - 1) {
					break;
				}
				else {
					int count = c - '0';
					char next = str.charAt(i + 1);
					for (int k = 0; k < count; k++) {
						ans += next;
					}
				}
			}
			else {
				ans += c;
			}
		}
		return ans; // YOUR CODE HERE
	}
	
	/**
	 * Given 2 strings, consider all the substrings within them
	 * of length len. Returns true if there are any such substrings
	 * which appear in both strings.
	 * Compute this in linear time using a HashSet. Len will be 1 or more.
	 */
	public static boolean stringIntersect(String a, String b, int len) {
		if (a.length() < len || b.length() < len) {
			return false;
		}
		HashSet<String> set = new HashSet<>();
		for (int i = 0; i <= a.length() - len; i++) {
			set.add(a.substring(i, i + len));
		}
		for (int i = 0; i <= b.length() - len; i++) {
			if (set.contains(b.substring(i, i + len))) {
				return true;
			}
		}
		return false;
	}
}
