import java.util.*;

public class Appearances {
	
	/**
	 * Returns the number of elements that appear the same number
	 * of times in both collections. Static method. (see handout).
	 * @return number of same-appearance elements
	 */
	public static <T> int sameCount(Collection<T> a, Collection<T> b) {
		if (a.isEmpty() || b.isEmpty()) {
			return 0;
		}
		HashMap<T, Integer> map1 = new HashMap<>();
		int ans = 0;
		for (T elem : a) {
			if (map1.containsKey(elem)) {
				map1.put(elem, map1.get(elem) + 1);
			} else {
				map1.put(elem, 1);
			}
		}
		HashMap<T, Integer> map2 = new HashMap<>();
		for (T elem : b) {
			if (map2.containsKey(elem)) {
				map2.put(elem, map2.get(elem) + 1);
			} else {
				map2.put(elem, 1);
			}
		}
		for (T elem : map1.keySet()) {
			if (map2.containsKey(elem)) {
				if (map1.get(elem).equals(map2.get(elem))) {
					ans++;
				}
			}
		}
		return ans;
	}
}
