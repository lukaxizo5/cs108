import java.util.*;

/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class Sudoku {
	// Provided grid data for main/testing
	// The instance variable strategy is up to you.
	
	// Provided easy 1 6 grid
	// (can paste this text into the GUI too)
	public static final int[][] easyGrid = Sudoku.stringsToGrid(
	"1 6 4 0 0 0 0 0 2",
	"2 0 0 4 0 3 9 1 0",
	"0 0 5 0 8 0 4 0 7",
	"0 9 0 0 0 6 5 0 0",
	"5 0 0 1 0 2 0 0 8",
	"0 0 8 9 0 0 0 3 0",
	"8 0 9 0 4 0 2 0 0",
	"0 7 3 5 0 9 0 0 1",
	"4 0 0 0 0 0 6 7 9");
	
	
	// Provided medium 5 3 grid
	public static final int[][] mediumGrid = Sudoku.stringsToGrid(
	 "530070000",
	 "600195000",
	 "098000060",
	 "800060003",
	 "400803001",
	 "700020006",
	 "060000280",
	 "000419005",
	 "000080079");
	
	// Provided hard 3 7 grid
	// 1 solution this way, 6 solutions if the 7 is changed to 0
	public static final int[][] hardGrid = Sudoku.stringsToGrid(
	"3 7 0 0 0 0 0 8 0",
	"0 0 1 0 9 3 0 0 0",
	"0 4 0 7 8 0 0 0 3",
	"0 9 3 8 0 0 0 1 2",
	"0 0 0 0 4 0 0 0 0",
	"5 2 0 0 0 6 7 9 0",
	"6 0 0 0 2 1 0 4 0",
	"0 0 0 5 3 0 9 0 0",
	"0 3 0 0 0 0 0 5 1");

	public static final int[][] hardGridManySolutions = Sudoku.stringsToGrid(
	"3 0 0 0 0 0 0 8 0",
	"0 0 1 0 9 3 0 0 0",
	"0 4 0 7 8 0 0 0 3",
	"0 9 3 8 0 0 0 1 2",
	"0 0 0 0 4 0 0 0 0",
	"5 2 0 0 0 6 7 9 0",
	"6 0 0 0 2 1 0 4 0",
	"0 0 0 5 3 0 9 0 0",
	"0 3 0 0 0 0 0 5 1");

	public static final int[][] zeroGrid = Sudoku.stringsToGrid(
	"0 0 0 0 0 0 0 0 0",
	"0 0 0 0 0 0 0 0 0",
	"0 0 0 0 0 0 0 0 0",
	"0 0 0 0 0 0 0 0 0",
	"0 0 0 0 0 0 0 0 0",
	"0 0 0 0 0 0 0 0 0",
	"0 0 0 0 0 0 0 0 0",
	"0 0 0 0 0 0 0 0 0",
	"0 0 0 0 0 0 0 0 0");
	
	public static final int SIZE = 9;  // size of the whole 9x9 puzzle
	public static final int PART = 3;  // size of each 3x3 part
	public static final int MAX_SOLUTIONS = 100;
	private int[][] grid;
	private ArrayList<Spot> spots;
	private long startTime;
	private long endTime;
	private int solutions;
	private String solution;


	public class Spot implements Comparable<Spot> {
		private int x;
		private int y;
		private int possibleDigitCount;
		private ArrayList<Integer> possibleDigits;

		public Spot(int x, int y) {
			this.x = x;
			this.y = y;
			possibleDigits = new ArrayList<>();
			calculatePossibleDigits();
		}

		private void set(int digit) {
			grid[x][y] = digit;
		}

		private void calculatePossibleDigits() {
			Set<Integer> impossibleDigits = new HashSet<>();
			possibleDigits.clear();
			for (int i = 0; i < SIZE; i++) {
				if (grid[x][i] != 0) {
					impossibleDigits.add(grid[x][i]);
				}
				if (grid[i][y] != 0) {
					impossibleDigits.add(grid[i][y]);
				}
			}
			int squareX = PART * (x / PART); // 0, 1, 2
			int squareY = PART * (y / PART); // 0, 1, 2
			for (int i = squareX; i < squareX + PART; i++) {
				for (int j = squareY; j < squareY + PART; j++) {
					if (grid[i][j] != 0) {
						impossibleDigits.add(grid[i][j]);
					}
				}
			}
			for (int i = 1; i <= 9; i++) {
				if (!impossibleDigits.contains(i)) {
					possibleDigits.add(i);
				}
			}
			possibleDigitCount = possibleDigits.size();
		}
		@Override
		public int compareTo(Spot spot) {
			return this.possibleDigitCount - spot.possibleDigitCount;
		}
	}
	
	// Provided various static utility methods to
	// convert data formats to int[][] grid.
	
	/**
	 * Returns a 2-d grid parsed from strings, one string per row.
	 * The "..." is a Java 5 feature that essentially
	 * makes "rows" a String[] array.
	 * (provided utility)
	 * @param rows array of row strings
	 * @return grid
	 */
	public static int[][] stringsToGrid(String... rows) {
		int[][] result = new int[rows.length][];
		for (int row = 0; row<rows.length; row++) {
			result[row] = stringToInts(rows[row]);
		}
		return result;
	}
	
	
	/**
	 * Given a single string containing 81 numbers, returns a 9x9 grid.
	 * Skips all the non-numbers in the text.
	 * (provided utility)
	 * @param text string of 81 numbers
	 * @return grid
	 */
	public static int[][] textToGrid(String text) {
		int[] nums = stringToInts(text);
		if (nums.length != SIZE*SIZE) {
			throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
		}
		
		int[][] result = new int[SIZE][SIZE];
		int count = 0;
		for (int row = 0; row<SIZE; row++) {
			for (int col=0; col<SIZE; col++) {
				result[row][col] = nums[count];
				count++;
			}
		}
		return result;
	}
	
	
	/**
	 * Given a string containing digits, like "1 23 4",
	 * returns an int[] of those digits {1 2 3 4}.
	 * (provided utility)
	 * @param string string containing ints
	 * @return array of ints
	 */
	public static int[] stringToInts(String string) {
		int[] a = new int[string.length()];
		int found = 0;
		for (int i=0; i<string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				a[found] = Integer.parseInt(string.substring(i, i+1));
				found++;
			}
		}
		int[] result = new int[found];
		System.arraycopy(a, 0, result, 0, found);
		return result;
	}


	// Provided -- the deliverable main().
	// You can edit to do easier cases, but turn in
	// solving hardGrid.
	public static void main(String[] args) {
		Sudoku sudoku;
		sudoku = new Sudoku(hardGrid);
		System.out.println(sudoku); // print the raw problem
		int count = sudoku.solve();
		System.out.println("solutions:" + count);
		System.out.println("elapsed:" + sudoku.getElapsed() + "ms");
		System.out.println(sudoku.getSolutionText());
	}

	/**
	 * Sets up based on the given ints.
	 */
	public Sudoku(int[][] ints) {
		grid = ints;
		spots = new ArrayList<>();
		solutions = 0;
		for (int i = 0; i <  SIZE; i++) {
			for (int j = 0; j <  SIZE; j++) {
				if (grid[i][j] == 0) {
					spots.add(new Spot(i, j));
				}
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				sb.append(grid[i][j]);
				if (j != SIZE - 1) {
					sb.append(" ");
				}
			}
			if (i != SIZE - 1) {
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	
	/**
	 * Solves the puzzle, invoking the underlying recursive search.
	 */
	public int solve() {
		startTime = System.currentTimeMillis();
		Collections.sort(spots);
		sudokuSolver(0);
		endTime = System.currentTimeMillis();
		return solutions;
	}

	private void sudokuSolver(int index) {
		if (index == spots.size()) {
			if (solutions == 0) {
				solution = toString();
			}
			solutions++;
			return;
		}
		Spot curr =  spots.get(index);
		curr.calculatePossibleDigits();
		for (int i = 0; i < curr.possibleDigitCount; i++) {
			if (solutions == MAX_SOLUTIONS) break;
			curr.set(curr.possibleDigits.get(i)); // choose
			sudokuSolver(index + 1); // recursion
			curr.set(0); // backtrack
		}
	}
	
	public String getSolutionText() {
		if (solutions == 0) {
			return "";
		}
		return solution;
	}
	
	public long getElapsed() {
		return endTime - startTime;
	}

}
