import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest{
	Board b;
	Piece pyr1, pyr2, pyr3, pyr4, s, sRotated;
	Piece sq, st, stRotated;

	// This shows how to build things in setUp() to re-use
	// across tests.
	
	// In this case, setUp() makes shapes,
	// and also a 3X6 board, with pyr placed at the bottom,
	// ready to be used by tests.

	@BeforeEach
	public void setUp() throws Exception {
		b = new Board(3, 6);
		
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		
		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();
		b.place(pyr1, 0, 0);

		sq  = new Piece(Piece.SQUARE_STR);
		st = new Piece(Piece.STICK_STR);
		stRotated = st.computeNextRotation();
	}
	
	// Check the basic width/height/max after the one placement
	@Test
	public void testSample1() {
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(2, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
	}
	
	// Place sRotated into the board, then check some measures
	@Test
	public void testSample2() {
		b.commit();
		int result = b.place(sRotated, 1, 1);
		assertEquals(Board.PLACE_OK, result);
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(4, b.getColumnHeight(1));
		assertEquals(3, b.getColumnHeight(2));
		assertEquals(4, b.getMaxHeight());
	}
	
	// Make  more tests, by putting together longer series of
	// place, clearRows, undo, place ... checking a few col/row/max
	// numbers that the board looks right after the operations.

	@Test
	public void testBasicFunctions() {
		assertEquals(3, b.getWidth());
		assertEquals(6, b.getHeight());

		assertEquals(1, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(1, b.getColumnHeight(2));

		assertEquals(3, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
		assertEquals(0, b.getRowWidth(3));
		assertEquals(0, b.getRowWidth(4));
		assertEquals(0, b.getRowWidth(5));

		assertTrue(b.getGrid(0,0));
		assertTrue(b.getGrid(1,0));
		assertTrue(b.getGrid(2,0));
		assertTrue(b.getGrid(1,1));
		assertFalse(b.getGrid(0,1));
		assertFalse(b.getGrid(2,1));

		// Out of bounds should return true
		assertTrue(b.getGrid(-2, 7));
		assertTrue(b.getGrid(2, -4));
		assertTrue(b.getGrid(7, 1));
		assertTrue(b.getGrid(2, 8));
	}

	@Test
	public void testDropHeight() {
		assertEquals(2, b.dropHeight(pyr1, 0));
		assertEquals(1, b.dropHeight(pyr2, 1));
		assertEquals(1, b.dropHeight(pyr3, 1));
		assertEquals(1, b.dropHeight(sRotated, 1));
		assertEquals(0, b.dropHeight(sRotated, 2));
		assertEquals(2, b.dropHeight(sq, 0));
		assertEquals(2, b.dropHeight(sq, 1));
		assertEquals(1, b.dropHeight(st, 0));
		assertEquals(2, b.dropHeight(st, 1));
		assertEquals(1, b.dropHeight(stRotated, 2));

		// Out of bounds coordinates should be ignored (returning 0), because they are considered empty cells
		assertEquals(0, b.dropHeight(stRotated, 3));
		assertEquals(1, b.dropHeight(stRotated, -3));
		assertEquals(2, b.dropHeight(stRotated, -2));
	}

	@Test
	public void testPlace() {
		b.commit();
		assertEquals(Board.PLACE_OUT_BOUNDS, b.place(pyr1, 1, 1));
		assertEquals(Board.PLACE_OUT_BOUNDS, b.place(sq, -1, 0));
		assertEquals(Board.PLACE_OUT_BOUNDS, b.place(st, 0, -1));
		assertEquals(Board.PLACE_OUT_BOUNDS, b.place(pyr2, 5, 2));
		assertEquals(Board.PLACE_OUT_BOUNDS, b.place(pyr2, 2, 8));

		assertEquals(Board.PLACE_BAD, b.place(pyr1, 0, 0));
		assertEquals(Board.PLACE_BAD, b.place(st, 1, 1));

		assertEquals(Board.PLACE_OK, b.place(st, 2, 2));
		b.commit();
		assertEquals(Board.PLACE_ROW_FILLED, b.place(sq, 0, 2));
	}

	@Test
	public void testRowClears() {
		b.clearRows();
		b.commit();
		assertEquals(1, b.getRowWidth(0));
		assertEquals(0, b.getRowWidth(1));
		assertEquals(0, b.getColumnHeight(0));
		assertEquals(1, b.getColumnHeight(1));
		assertEquals(0, b.getColumnHeight(2));

		b.place(st, 2, 1);
		b.commit();

		b.place(sq, 0, 1);

		assertEquals(3, b.getRowWidth(2));
		assertEquals(1, b.getRowWidth(3));
		assertEquals(3, b.getColumnHeight(0));
		assertEquals(3, b.getColumnHeight(1));
		assertEquals(5, b.getColumnHeight(2));

		b.clearRows();
		b.commit();
		assertEquals(1, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(1, b.getRowWidth(2));
		assertEquals(0, b.getRowWidth(3));
		assertEquals(0, b.getColumnHeight(0));
		assertEquals(1, b.getColumnHeight(1));
		assertEquals(3, b.getColumnHeight(2));
	}

	@Test
	public void testUndo() {
		b.undo();
		assertEquals(0, b.getRowWidth(0));
		assertEquals(0, b.getRowWidth(1));
		assertEquals(0, b.getColumnHeight(0));
		assertEquals(0, b.getColumnHeight(1));
		assertEquals(0, b.getColumnHeight(2));
		b.place(sq, 0, 0);
		b.commit();
		// undo shouldn't change anything if board is commited
		b.undo();
		assertEquals(2, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(2, b.getRowWidth(0));
		assertEquals(2, b.getRowWidth(1));
	}

	@Test
	public void testPlaceCommitProblem() {
		assertThrows(RuntimeException.class, () -> {
			b.place(sq, 0, 2);
		});
	}

	@Test
	public void testToString() {
		assertEquals("|   |\n|   |\n|   |\n|   |\n| + |\n|+++|\n-----", b.toString());
	}

	@Test
	public void testFullPlay() {
		Board b1 = new Board(5, 5);
		b1.place(sq, 0, 0);
		b1.commit();
		b1.place(sq, 2, 0);
		b1.commit();
		b1.place(st, 4, 0);
		assertEquals(4, b1.getMaxHeight());
		b1.clearRows();
		b1.commit();
		assertEquals(1, b1.getRowWidth(0));
		assertEquals(1, b1.getRowWidth(1));
		assertEquals(0, b1.getRowWidth(2));
		assertEquals(0, b1.getRowWidth(3));

		b1.place(pyr2, 0, 0);
		assertEquals(2, b1.getRowWidth(0));
		assertEquals(3, b1.getRowWidth(1));
		assertEquals(1, b1.getRowWidth(2));
		assertEquals(0, b1.getRowWidth(3));

		b1.undo();
		assertEquals(1, b1.getRowWidth(0));
		assertEquals(1, b1.getRowWidth(1));
		assertEquals(0, b1.getRowWidth(2));
		assertEquals(0, b1.getRowWidth(3));
	}
}
