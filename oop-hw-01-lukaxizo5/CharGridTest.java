import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Test cases for CharGrid -- a few basic tests are provided.
public class CharGridTest {

	@Test
	public void testCharArea1() {
		char[][] grid = new char[][] {
				{'a', 'y', ' '},
				{'x', 'a', 'z'},
			};
		
		
		CharGrid cg = new CharGrid(grid);
				
		assertEquals(4, cg.charArea('a'));
		assertEquals(1, cg.charArea('z'));
	}
	
	@Test
	public void testCharArea2() {
		char[][] grid = new char[][] {
				{'c', 'a', ' '},
				{'b', ' ', 'b'},
				{' ', ' ', 'a'}
			};
		
		CharGrid cg = new CharGrid(grid);
		
		assertEquals(6, cg.charArea('a'));
		assertEquals(3, cg.charArea('b'));
		assertEquals(1, cg.charArea('c'));
	}

	@Test
	public void testCharArea3() {
		// empty grid
		char[][] grid = new char[][] { };
		CharGrid cg = new CharGrid(grid);
		assertEquals(0, cg.charArea('a'));
		assertEquals(0, cg.charArea('b'));
		assertEquals(0, cg.charArea('c'));
	}

	@Test
	public void testCharArea4() {
		// character not found
		char[][] grid = new char[][] {
				{'a', 'y', ' '},
				{'x', 'a', 'z'},
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(0, cg.charArea('b'));
		assertEquals(0, cg.charArea('c'));
	}

	@Test
	public void testCountPlus1() {
		char[][] grid = new char[][] {
				{'e', 'a', 'd'},
				{'a', 'a', 'a'},
				{'b', 'a', 'c'}
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(1, cg.countPlus());
	}

	@Test
	public void testCountPlus2() {
		char[][] grid = new char[][] {
				{' ', 'a', ' ', ' ', 'e', 'e', 'e', 'e', ' ', ' '},
				{'a', 'a', 'a', ' ', ' ', ' ', ' ', 'd', ' ', ' '},
				{' ', 'a', 'b', 'b', ' ', ' ', 'd', 'd', 'd', ' '},
				{' ', 'b', 'b', ' ', ' ', 'f', ' ', 'd', ' ', ' '},
				{' ', ' ', 'c', 'c', 'f', 'f', 'f', ' ', ' ', ' '},
				{'c', 'c', 'c', 'c', 'c', 'f', 'h', 'h', 'h', ' '},
				{' ', ' ', 'c', 'x', ' ', 'g', ' ', ' ', ' ', ' '},
				{' ', ' ', 'c', 'x', ' ', 'f', ' ', ' ', ' ', ' '}
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(3, cg.countPlus());
	}

	@Test
	public void testCountPlus3() {
		char[][] grid = new char[][] {
				{'l', 'f', 'l', 'f'},
				{'f', 'l', 'l', 'l'},
				{'a', 'b', 'l', 'b'},
				{'a', 'a', 'b', 'b'}
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(1, cg.countPlus());
	}

	@Test
	public void testCountPlus4() {
		char[][] grid = new char[][] {
				{' ', 'f', ' '},
				{'f', 'f', 'l'},
				{' ', 'f', ' '}
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(0, cg.countPlus());
	}

	@Test
	public void testCountPlus5() {
		// empty grid
		char[][] grid = new char[][] {
				{' ', ' ', ' '},
				{' ', ' ', ' '},
				{' ', ' ', ' '}
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(0, cg.countPlus());
	}
	@Test
	public void testCountPlus6() {
		// small grid
		char[][] grid1 = new char[][] {
				{'a', 'a'},
				{'a', 'a'}
		};
		CharGrid cg1 = new CharGrid(grid1);
		assertEquals(0, cg1.countPlus());

		char[][] grid2 = new char[][] {
				{'b'}
		};
		CharGrid cg2 = new CharGrid(grid2);
		assertEquals(0, cg2.countPlus());

		char[][] grid3 = new char[][] {
		};
		CharGrid cg3 = new CharGrid(grid3);
		assertEquals(0, cg1.countPlus());
	}

	@Test
	public void testCountPlus7() {
		char[][] grid = new char[][] {
				{' ', 'd', ' '},
				{'c', 'd', 'd'},
				{' ', 'd', ' '}
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(0, cg.countPlus());
	}
}
