//
// TetrisGrid encapsulates a tetris board and has
// a clearRows() capability.

public class TetrisGrid {
	private boolean[][] grid;

	/**
	 * Constructs a new instance with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public TetrisGrid(boolean[][] grid) {
		this.grid = grid;
	}
	
	/**
	 * Does row-clearing on the grid (see handout).
	 */
	public void clearRows() {
		int rowsLeft = 0;
		for (int j = 0; j < grid[0].length; j++) {
			boolean full = true;
			for (int i = 0; i < grid.length; i++) {
				if (!grid[i][j]) {
					full = false;
					break;
				}
			}
			if (!full) {
				for (int  k = 0; k < grid.length; k++) {
					grid[k][rowsLeft] = grid[k][j];
				}
				rowsLeft++;
			}
		}
		for (int j = rowsLeft; j < grid[0].length; j++) {
			for (int i = 0; i < grid.length; i++) {
				grid[i][j] = false;
			}
		}
	}
	
	/**
	 * Returns the internal 2d grid array.
	 * @return 2d grid array
	 */
	boolean[][] getGrid() {
		return grid;
	}
}
