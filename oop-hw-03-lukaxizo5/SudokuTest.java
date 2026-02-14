import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SudokuTest {

    @Test
    public void testEasyGrid() {
        Sudoku sudoku = new Sudoku(Sudoku.easyGrid);
        assertEquals(1, sudoku.solve());
        assertFalse(sudoku.getSolutionText().equals(sudoku.toString()));
        assertTrue(validateSolution(sudoku.getSolutionText()));
        assertTrue(sudoku.getElapsed() < 100);
    }

    @Test
    public void testMediumGrid() {
        Sudoku sudoku = new Sudoku(Sudoku.mediumGrid);
        assertEquals(1, sudoku.solve());
        assertFalse(sudoku.getSolutionText().equals(sudoku.toString()));
        assertTrue(validateSolution(sudoku.getSolutionText()));
        assertTrue(sudoku.getElapsed() < 100);
    }

    @Test
    public void testHardGrid() {
        Sudoku sudoku = new Sudoku(Sudoku.hardGrid);
        assertEquals(1, sudoku.solve());
        assertFalse(sudoku.getSolutionText().equals(sudoku.toString()));
        assertTrue(validateSolution(sudoku.getSolutionText()));
        assertTrue(sudoku.getElapsed() < 100);
    }

    @Test
    public void testHardGridManySolutions() {
        Sudoku sudoku = new Sudoku(Sudoku.hardGridManySolutions);
        assertEquals(6, sudoku.solve());
        assertFalse(sudoku.getSolutionText().equals(sudoku.toString()));
        assertTrue(validateSolution(sudoku.getSolutionText()));
        assertTrue(sudoku.getElapsed() < 100);
    }

    @Test
    public void testZeroGrid() {
        Sudoku sudoku = new Sudoku(Sudoku.zeroGrid);
        assertEquals(100, sudoku.solve());
        assertFalse(sudoku.getSolutionText().equals(sudoku.toString()));
        assertTrue(validateSolution(sudoku.getSolutionText()));
        assertTrue(sudoku.getElapsed() < 100);
    }

    @Test
    public void testRandomGrid() {
        int[][] grid = Sudoku.textToGrid("001906005300800020056400000700009500000000162030000070407008903680100000900040607");
        Sudoku sudoku = new Sudoku(grid);
        assertEquals(1, sudoku.solve());
        assertFalse(sudoku.getSolutionText().equals(sudoku.toString()));
        assertTrue(validateSolution(sudoku.getSolutionText()));
        assertTrue(sudoku.getElapsed() < 100);
    }

    @Test
    public void testNoSolutions() {
        int[][] grid = Sudoku.textToGrid("003203006000040090102000500700029000040307050000810002001000803020080000900004600");
        Sudoku sudoku = new Sudoku(grid);
        sudoku.solve();
        assertEquals(0, sudoku.getSolutionText().length());
        assertTrue(sudoku.getElapsed() < 100);
    }

    @Test
    public void testBadInput() {
        assertThrows(RuntimeException.class, () -> Sudoku.textToGrid("0032.0006000040090102000500700029....4030705000081........00803020080000900004600"));
    }

    @Test
    public void testRunMain() {
        Sudoku.main(null);
    }

    private boolean validateSolution(String solution) {
        String[] rows = solution.trim().split("\n");
        if (rows.length != 9) return false;
        int[][] grid = new int[9][9];

        for (int i = 0; i < 9; i++) {
            String[] tokens = rows[i].trim().split("\\s+");
            if (tokens.length != 9) return false;
            for (int j = 0; j < 9; j++) {
                if (!tokens[j].matches("[1-9]")) return false;
                grid[i][j] = Integer.parseInt(tokens[j]);
            }
        }

        for (int i = 0; i < 9; i++) {
            boolean[] row = new boolean[10];
            for (int j = 0; j < 9; j++) {
                if (row[grid[i][j]] == true) return false;
                row[grid[i][j]] = true;
            }
        }

        for (int i = 0; i < 9; i++) {
            boolean[] col = new boolean[10];
            for (int j = 0; j < 9; j++) {
                if (col[grid[j][i]] == true) return false;
                col[grid[j][i]] = true;
            }
        }

        for (int i = 0; i < 9; i++) {
            boolean[] box = new boolean[10];
            int rowIndex = i / 3;
            int colIndex = i % 3;
            for (int j = 0; j < 3; j++) {
                for (int k =  0; k < 3; k++) {
                    int toCheck = grid[3 * rowIndex + j][3 * colIndex + k];
                    if (box[toCheck] == true) return false;
                    box[toCheck] = true;
                }
            }
        }
        
        return true;
    }
}
