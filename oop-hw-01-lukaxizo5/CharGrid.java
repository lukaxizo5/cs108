// HW1 2-d array Problems
// CharGrid encapsulates a 2-d grid of chars and supports
// a few operations on the grid.

public class CharGrid {
	private char[][] grid;

	/**
	 * Constructs a new CharGrid with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public CharGrid(char[][] grid) {
		this.grid = grid;
	}
	
	/**
	 * Returns the area for the given char in the grid. (see handout).
	 * @param ch char to look for
	 * @return area for given char
	 */
	public int charArea(char ch) {
		if (grid.length == 0) {
			return 0;
		}
		int mn1 = grid.length + 1;
		int mn2 = grid.length + 1;
		int mx1 = -1;
		int mx2 = -1;
		boolean found = false;
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if (grid[i][j] == ch) {
					found = true;
					mn1 = Math.min(mn1, i);
					mn2 = Math.min(mn2, j);
					mx1 = Math.max(mx1, i);
					mx2 = Math.max(mx2, j);
				}
			}
		}
		if (!found) {
			return 0;
		}
		return (mx1 - mn1 + 1) * (mx2 - mn2 + 1);
	}
	
	/**
	 * Returns the count of '+' figures in the grid (see handout).
	 * @return number of + in grid
	 */
	public int countPlus() {
		int ans = 0;
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if (plusMiddle(i, j)) {
					ans++;
				}
			}
		}
		return ans;
	}

	private boolean plusMiddle(int i, int j) {
		char c = grid[i][j];
		if (c == ' ') {
			return false;
		}
		int ii = i, jj = j, cnt1 = 0, cnt2 = 0, cnt3 = 0, cnt4 = 0;
		while (inGrid(ii, jj) && grid[ii][jj] == c) {
			cnt1++;
			ii--;
		}
		ii = i;
		while (inGrid(ii, jj) && grid[ii][jj] == c) {
			cnt2++;
			ii++;
		}
		ii = i;
		while (inGrid(ii, jj) && grid[ii][jj] == c) {
			cnt3++;
			jj--;
		}
		jj = j;
		while (inGrid(ii, jj) && grid[ii][jj] == c) {
			cnt4++;
			jj++;
		}
		return cnt1 >= 2 && cnt1 == cnt2 && cnt2 == cnt3 && cnt3 == cnt4;
	}

	private boolean inGrid(int i, int j) {
		return i >= 0 && i < grid.length && j >= 0 && j < grid[i].length;
	}
}
