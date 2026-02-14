// StringCodeTest
// Some test code is provided for the early HW1 problems,
// and much is left for you to add.
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StringCodeTest {
	//
	// blowup
	//
	@Test
	public void testBlowup1() {
		// basic cases
		assertEquals("xxaaaabb", StringCode.blowup("xx3abb"));
		assertEquals("xxxZZZZ", StringCode.blowup("2x3Z"));
	}

	@Test
	public void testBlowup2() {
		// things with digits
		
		// digit at end
		assertEquals("axxx", StringCode.blowup("a2x3"));
		
		// digits next to each other
		assertEquals("a33111", StringCode.blowup("a231"));
		
		// try a 0
		assertEquals("aabb", StringCode.blowup("aa0bb"));
	}

	@Test
	public void testBlowup3() {
		// weird chars, empty string
		assertEquals("AB&&,- ab", StringCode.blowup("AB&&,- ab"));
		assertEquals("", StringCode.blowup(""));
		
		// string with only digits
		assertEquals("", StringCode.blowup("2"));
		assertEquals("33", StringCode.blowup("23"));
	}

	@Test
	public void testBlowup4() {
		// repeating empty characters
		assertEquals("a     bc", StringCode.blowup("a4 bc2"));
		assertEquals("zzz   def", StringCode.blowup("zzz1  def"));
	}
	
	//
	// maxRun
	//
	@Test
	public void testRun1() {
		assertEquals(2, StringCode.maxRun("hoopla"));
		assertEquals(3, StringCode.maxRun("hoopllla"));
	}

	@Test
	public void testRun2() {
		assertEquals(3, StringCode.maxRun("abbcccddbbbxx"));
		assertEquals(0, StringCode.maxRun(""));
		assertEquals(3, StringCode.maxRun("hhhooppoo"));
	}

	@Test
	public void testRun3() {
		// "evolve" technique -- make a series of test cases
		// where each is change from the one above.
		assertEquals(1, StringCode.maxRun("123"));
		assertEquals(2, StringCode.maxRun("1223"));
		assertEquals(2, StringCode.maxRun("112233"));
		assertEquals(3, StringCode.maxRun("1112233"));
	}

	@Test
	public void testRun4() {
		// maxRun at the end of the string
		assertEquals(5, StringCode.maxRun("aaabbcccddddd"));
		assertEquals(7, StringCode.maxRun("aabbcedjfhakqaajdsjkkkkooooooo"));
	}

	//
 	// stringIntersect
 	//
	@Test
	public void testIntersect1() {
		// full string substring
		assertTrue(StringCode.stringIntersect("bdsbdh", "bdsbdh", 6));
		// less length
		assertFalse(StringCode.stringIntersect("kdaks", "bpwr", 6));
		assertFalse(StringCode.stringIntersect("pldsaodpok", "pldps", 6));
		assertFalse(StringCode.stringIntersect("dskds", "dkdspew", 6));
	}

	@Test
	public void testIntersect2() {
		// true on exact match, false on more
		assertTrue(StringCode.stringIntersect("abcdef", "cdefghi", 4));
		assertFalse(StringCode.stringIntersect("abcdef", "cdefghi", 5));
		assertTrue(StringCode.stringIntersect("pdsakpoe", "pdaspoe", 3));
		assertFalse(StringCode.stringIntersect("pdsakpoe", "pdaspoe", 4));
	}

	@Test
	public void testIntersect3() {
		// len = 1
		assertTrue(StringCode.stringIntersect("abcdef", "fghi", 1));
		assertFalse(StringCode.stringIntersect("abcdef", "ghijkl", 1));
		assertTrue(StringCode.stringIntersect("ndskjdbk", "kaoipqa", 1));
		assertFalse(StringCode.stringIntersect("ndskjdbk", "aoipqa", 1));
	}

	@Test
	public void testIntersect4() {
		// random big
		assertTrue(StringCode.stringIntersect("njdasknqepdiadnjkbkjx", "nkdbasdbadqwonjkxand", 2));
		assertFalse(StringCode.stringIntersect("dakopwqejdkjbakdabd", "dakolsdsajbekqbe", 5));
		assertTrue(StringCode.stringIntersect("pookiebearprincess", "pekkapookiebear", 10));
		assertFalse(StringCode.stringIntersect("daughterofthevoid", "defenderoftomorrow", 6));
	}
}
