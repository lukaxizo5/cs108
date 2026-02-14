// Board.java

import java.util.Arrays;

/**
 CS108 Tetris Board.
 Represents a Tetris board -- essentially a 2-d grid
 of booleans. Supports tetris pieces and row clearing.
 Has an "undo" feature that allows clients to add and remove pieces efficiently.
 Does not do any drawing or have any idea of pixels. Instead,
 just represents the abstract 2-d board.
*/
public class Board	{
	// Some ivars are stubbed out for you:
	private int width;
	private int height;
	private boolean[][] grid;
	private boolean DEBUG = true;
	boolean committed;
	private int[] widths;
	private int[] heights;
	private int[] backUpWidths;
	private int[] backUpHeights;
	private boolean[][] backUpGrid;
	private int maxHeight;
	private int backUpMaxHeight;

	// Here a few trivial methods are provided:

	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	*/
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		grid = new boolean[width][height];
		committed = true;
		widths = new int[height];
		heights = new int[width];
		backUpWidths = new int[height];
		backUpHeights = new int[width];
		backUpGrid = new boolean[width][height];
		maxHeight = 0;
		backUpMaxHeight = 0;
	}


	/**
	 Returns the width of the board in blocks.
	*/
	public int getWidth() {
		return width;
	}


	/**
	 Returns the height of the board in blocks.
	*/
	public int getHeight() {
		return height;
	}


	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	*/
	public int getMaxHeight() {
		return maxHeight;
	}


	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	*/
	public void sanityCheck() {
		if (DEBUG) {
			int correctMaxHeight = 0;
			int[] correctWidths = new int[height];
			int[] correctHeights = new int[width];
			for (int j = 0; j < height; j++) {
				for (int i = 0; i < width; i++) {
					if (grid[i][j]) {
						correctWidths[j]++;
						if (j + 1 > correctHeights[i]) correctHeights[i] = j + 1;
						correctMaxHeight = Math.max(correctMaxHeight, correctHeights[i]);
					}
				}
			}
			if (!Arrays.equals(heights, correctHeights) || !Arrays.equals(widths, correctWidths) || !(maxHeight == correctMaxHeight))
				throw new RuntimeException("Your board is inconsistent!");
		}
	}

	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.

	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length).
	*/
	public int dropHeight(Piece piece, int x) {
		int[] skirt = piece.getSkirt();
		int y = 0;
		for (int i = 0; i < piece.getWidth(); i++) {
			if (x + i >= width) break;
			if (x + i < 0) continue;
			y = Math.max(y, heights[x + i] - skirt[i]);
		}
		return y;
	}


	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	*/
	public int getColumnHeight(int x) {
		return heights[x];
	}


	/**
	 Returns the number of filled blocks in
	 the given row.
	*/
	public int getRowWidth(int y) {
		return widths[y];
	}


	/**
	 Returns true if the given block is filled in the board.
	 Blocks outside of the valid width/height area
	 always return true.
	*/
	public boolean getGrid(int x, int y) {
		return x < 0 || x >= width || y < 0 || y >= height || grid[x][y];
	}


	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;

	private boolean outOfBounds(int x, int y) {
		return  x < 0 || x >= width || y < 0 || y >= height;
	}

	/**
	 Attempts to add the body of a piece to the board.
	 Copies the piece blocks into the board grid.
	 Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
	 for a regular placement that causes at least one row to be filled.

	 <p>Error cases:
	 A placement may fail in two ways. First, if part of the piece may falls out
	 of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 Or the placement may collide with existing blocks in the grid
	 in which case PLACE_BAD is returned.
	 In both error cases, the board may be left in an invalid
	 state. The client can use undo(), to recover the valid, pre-place state.
	*/
	public int place(Piece piece, int x, int y) {
		// flag !committed problem
		if (!committed) throw new RuntimeException("place commit problem");

        if (outOfBounds(x, y) || outOfBounds(x + piece.getWidth() - 1, y + piece.getHeight() - 1)) {
			return PLACE_OUT_BOUNDS;
		}

		TPoint[] body = piece.getBody();
        for (TPoint p : body) {
            if (grid[p.x + x][p.y + y]) {
                return PLACE_BAD;
            }
        }
		commit();
		// Now we can safely place the piece
		for (TPoint p : body) {
			int currX = p.x + x;
			int currY = p.y + y;
			grid[currX][currY] = true;
			widths[currY]++;
			heights[currX] = Math.max(heights[currX], currY + 1);
			maxHeight = Math.max(maxHeight, heights[currX]);
		}
		committed = false;
		for (TPoint p : body) {
			int currX = p.x + x;
			int currY = p.y + y;
			if (widths[currY] == width) {
				return PLACE_ROW_FILLED;
			}
		}
		sanityCheck();
		return PLACE_OK;
	}

	/**
	 * Returns the index of the first full row from down.
	 */
	private int getFullRowIndex() {
		for (int i = 0; i < height; i++) {
			if (widths[i] == width) return i;
		}
		return -1;
	}

	/**
	 * Clears a row.
	 * @param y
	 */
	private void clearRow(int y) {
		for (int i = 0; i < width; i++) {
			grid[i][y] = false;
		}
	}

	/**
	 * Updates the heights array and maxHeight after row clears.
	 */
	private void calculateNewHeights() {
		maxHeight = 0;
		for(int i = 0; i < width; i++) {
			int curr = height - 1;
			while(!grid[i][curr] && curr > 0) {
				curr--;
			}
			if(grid[i][curr]) {
				heights[i] = curr + 1;
			}
			// empty column
			else {
				heights[i] = curr;
			}
			maxHeight = Math.max(maxHeight, heights[i]);
		}
	}

	/**
	 * Updates the widths array after row clears.
	 */
	private void calculateNewWidths() {
		for (int j = 0; j < height; j++) {
			int count = 0;
			for (int i = 0; i < width; i++) {
				if (grid[i][j]) {
					count++;
				}
			}
			widths[j] = count;
		}
	}

	/**
	 * Shifts every row down that is above the y-row.
	 * @param y
	 */
	private void shiftDown(int y) {
		for (int j = y; j < height - 1; j++) {
			for (int i = 0; i < width; i++) {
				grid[i][j] = grid[i][j + 1];
			}
		}
		// Clear last row
		for (int i = 0; i < width; i++) {
			grid[i][height - 1] = false;
		}
	}

	/**
	 Deletes rows that are filled all the way across, moving
	 things above down. Returns the number of rows cleared.
	*/
	public int clearRows() {
		int rowsCleared = 0;
		while (true) {
			int rowToClear = getFullRowIndex();
			if (rowToClear == -1) break;
			rowsCleared++;
			clearRow(rowToClear);
			shiftDown(rowToClear);
			calculateNewHeights();
			calculateNewWidths();
		}
		sanityCheck();
		committed = false;
		return rowsCleared;
	}


	/**
	 Reverts the board to its state before up to one place
	 and one clearRows();
	 If the conditions for undo() are not met, such as
	 calling undo() twice in a row, then the second undo() does nothing.
	 See the overview docs.
	*/
	public void undo() {
		if (!committed) {
			int[] temp = widths;
			widths = backUpWidths;
			backUpWidths = temp;
			temp = heights;
			heights = backUpHeights;
			backUpHeights = temp;
			boolean[][] tempGrid = grid;
			grid = backUpGrid;
			backUpGrid = tempGrid;
			int tmpMaxHeight = maxHeight;
			maxHeight = backUpMaxHeight;
			backUpMaxHeight = tmpMaxHeight;
			commit();
			sanityCheck();
		}
	}


	/**
	 Puts the board in the committed state.
	*/
	public void commit() {
		committed = true;
		System.arraycopy(widths, 0, backUpWidths, 0, widths.length);
		System.arraycopy(heights, 0, backUpHeights, 0, heights.length);
		for (int i = 0; i < grid.length; i++) {
			System.arraycopy(grid[i], 0, backUpGrid[i], 0, grid[i].length);
		}
		backUpMaxHeight = maxHeight;
	}



	/*
	 Renders the board state as a big String, suitable for printing.
	 This is the sort of print-obj-state utility that can help see complex
	 state change over time.
	 (provided debugging utility)
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height-1; y>=0; y--) {
			buff.append('|');
			for (int x=0; x<width; x++) {
				if (getGrid(x,y)) buff.append('+');
				else buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x=0; x<width+2; x++) buff.append('-');
		return(buff.toString());
	}
}


