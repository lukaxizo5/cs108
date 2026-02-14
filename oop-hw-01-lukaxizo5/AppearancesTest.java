import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AppearancesTest {
	// utility -- converts a string to a list with one
	// elem for each char.
	private List<String> stringToList(String s) {
		List<String> list = new ArrayList<String>();
		for (int i=0; i<s.length(); i++) {
			list.add(String.valueOf(s.charAt(i)));
			// note: String.valueOf() converts lots of things to string form
		}
		return list;
	}

	@Test
	public void testSameCount1() {
		List<String> a = stringToList("abbccc");
		List<String> b = stringToList("cccbba");
		assertEquals(3, Appearances.sameCount(a, b));
	}

	@Test
	public void testSameCount2() {
		// basic List<Integer> cases
		List<Integer> a = Arrays.asList(1, 2, 3, 1, 2, 3, 5);
		assertEquals(1, Appearances.sameCount(a, Arrays.asList(1, 9, 9, 1)));
		assertEquals(2, Appearances.sameCount(a, Arrays.asList(1, 3, 3, 1)));
		assertEquals(1, Appearances.sameCount(a, Arrays.asList(1, 3, 3, 1, 1)));
	}

	@Test
	public void testSameCount3() {
		// booleans
		List<Boolean>  a = Arrays.asList(true, false, false, true, true, false);
		List<Boolean> b = Arrays.asList(true, true, true, true, false, false, false);
		assertEquals(1, Appearances.sameCount(a, b));
		assertEquals(1, Appearances.sameCount(b, a));
	}

	@Test
	public void testSameCount4() {
		// characters
		List<Character> a  = Arrays.asList('a', 'b', 'c', 'a', 'b', 'c', 'c', 'c');
		List<Character> b =  Arrays.asList('a', 'b', 'c', 'c', 'c', 'a', 'b');
		assertEquals(2, Appearances.sameCount(a, b));
		assertEquals(2, Appearances.sameCount(b, a));
	}

	@Test
	public void testSameCount5() {
		// full
		List<Integer> a = Arrays.asList(1, 2, 3, 4, 5, 6);
		assertEquals(6, Appearances.sameCount(a, Arrays.asList(1, 2, 3, 4, 5, 6)));
		assertEquals(5, Appearances.sameCount(a, Arrays.asList(1, 1, 2, 3, 4, 5, 6)));
		assertEquals(4, Appearances.sameCount(a, Arrays.asList(1, 1, 2, 2, 3, 4, 5, 6)));
	}

	@Test
	public void testSameCount6() {
		// empty
		List<Integer> a = new ArrayList<Integer>();
		List<Integer> b = Arrays.asList(1, 2);
		assertEquals(0, Appearances.sameCount(a, b));
		assertEquals(0, Appearances.sameCount(b, a));
	}
}
