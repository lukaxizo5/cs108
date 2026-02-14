import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/*
  Unit test for Piece class -- starter shell.
 */
public class PieceTest{
	// You can create data to be used in the your
	// test cases like this. For each run of a test method,
	// a new PieceTest object is created and setUp() is called
	// automatically by JUnit.
	// For example, the code below sets up some
	// pyramid and s pieces in instance variables
	// that can be used in tests.
	private Piece pyr1, pyr2, pyr3, pyr4;
	private Piece s1, s2, s3, s4;
	private Piece l1, l2, l3, l4;
	private Piece st1, st2, st3, st4;
	private Piece sq1, sq2;

	@BeforeEach
	public void setUp() throws Exception {
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		s1 = new Piece(Piece.S1_STR);
		s2 = s1.computeNextRotation();
		s3 = s2.computeNextRotation();
		s4 = s3.computeNextRotation();
		l1 = new Piece(Piece.L1_STR);
		l2 = l1.computeNextRotation();
		l3 = l2.computeNextRotation();
		l4 = l3.computeNextRotation();
		st1 = new Piece(Piece.STICK_STR);
		st2 = st1.computeNextRotation();
		st3 = st2.computeNextRotation();
		st4 = st3.computeNextRotation();
		sq1 = new Piece(Piece.SQUARE_STR);
		sq2 = sq1.computeNextRotation();
	}

	@Test
	public void testWrongInput() {
		String wrongInput = "0 1 0 2 1 2 a b";
		assertThrows(RuntimeException.class, () -> new Piece(wrongInput));
	}
	
	// Here are some sample tests to get you started
	@Test
	public void testSampleSize() {
		// Check size of pyr piece
		assertEquals(3, pyr1.getWidth());
		assertEquals(2, pyr1.getHeight());
		
		// Now try after rotation
		// Effectively we're testing size and rotation code here
		assertEquals(2, pyr2.getWidth());
		assertEquals(3, pyr2.getHeight());
		
		// Now try with some other piece, made a different way
		assertEquals(1, st1.getWidth());
		assertEquals(4, st1.getHeight());
	}

	@Test
	public void testBody() {
		Piece pyr = new Piece(Piece.PYRAMID_STR);
		TPoint[] points = pyr.getBody();
		assertEquals(4, points.length);
		assertEquals(new TPoint(0, 0), points[0]);
		assertEquals(new TPoint(1, 0), points[1]);
		assertEquals(new TPoint(1, 1), points[2]);
		assertEquals(new TPoint(2, 0), points[3]);
	}

	@Test
	public void testEquals() {
		Piece pyr = new Piece("1 1  1 0  2 0  0 0");
		assertTrue(pyr.equals(pyr1));

		Piece l = new Piece("0 2  1 0  0 0  0 1");
		assertTrue(l.equals(l1));

		Piece sq = new Piece("1 1  1 0  0 0  0 1");
		assertTrue(sq.equals(sq1));
		assertTrue(sq.equals(sq));

		Piece notSq = new Piece("0 1  1 0  0 0  2 2");
		assertFalse(notSq.equals(sq));

		TPoint p1 = new TPoint(-1, -1);
		assertFalse(sq.equals(p1));

		Piece newPiece = new Piece("1 1  1 0  0 0");
		assertFalse(sq.equals(newPiece));
	}
	
	// Test the skirt returned by a few pieces
	@Test
	public void testSampleSkirt() {
		// Note must use assertTrue(Arrays.equals(... as plain .equals does not work
		// right for arrays.
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, pyr1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0, 1}, pyr3.getSkirt()));

		assertTrue(Arrays.equals(new int[] {0, 0, 1}, s1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0}, s2.getSkirt()));
	}

	@Test
	public void testStick() {
		assertTrue(Arrays.equals(new int[] {0}, st1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 0, 0, 0}, st2.getSkirt()));
		assertEquals(1, st2.getHeight());
		assertEquals(4, st2.getWidth());
		assertTrue(st1.equals(st3));
		assertTrue(st2.equals(st4));
	}

	@Test
	public void testL1() {
		assertTrue(Arrays.equals(new int[] {0, 0}, l1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, l2.getSkirt()));
		assertTrue(Arrays.equals(new int[] {2, 0}, l3.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 1, 1}, l4.getSkirt()));
		assertEquals(3, l1.getHeight());
		assertEquals(2, l1.getWidth());
		assertEquals(3, l3.getHeight());
		assertEquals(2, l3.getWidth());
		assertEquals(2, l2.getHeight());
		assertEquals(3, l2.getWidth());
		assertEquals(2, l4.getHeight());
		assertEquals(3, l4.getWidth());
		assertTrue(l1.equals(l4.computeNextRotation()));
	}

	@Test
	public void testS1() {
		assertTrue(Arrays.equals(new int[] {0, 0, 1}, s1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0}, s2.getSkirt()));
		assertEquals(2, s1.getHeight());
		assertEquals(3, s1.getWidth());
		assertEquals(3, s2.getHeight());
		assertEquals(2, s2.getWidth());
		assertTrue(s1.equals(s3));
		assertTrue(s2.equals(s4));
	}

	@Test
	public void testSquare() {
		assertTrue(Arrays.equals(new int[] {0, 0}, sq1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 0}, sq2.getSkirt()));
		assertEquals(2, sq1.getHeight());
		assertEquals(2, sq1.getWidth());
		assertTrue(sq1.equals(sq2));
	}

	@Test
	public void testPyramid() {
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, pyr1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0}, pyr2.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0, 1}, pyr3.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 1}, pyr4.getSkirt()));
		assertEquals(2, pyr1.getHeight());
		assertEquals(3, pyr1.getWidth());
		assertEquals(3, pyr2.getHeight());
		assertEquals(2, pyr2.getWidth());
		assertEquals(2, pyr3.getHeight());
		assertEquals(3, pyr3.getWidth());
		assertEquals(3, pyr4.getHeight());
		assertEquals(2, pyr4.getWidth());
		assertTrue(pyr1.equals(pyr4.computeNextRotation()));
	}

	@Test
	public void testFastRotations() {
		Piece[] pieces = Piece.getPieces();
		assertTrue(pieces[0].fastRotation().equals(st2));
		// It shouldn't change the result
		Piece[] pieces2 = Piece.getPieces();
		assertTrue(pieces[0].fastRotation().equals(st2));
	}
	
}
