import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class SudokuFrame extends JFrame {
	private JTextArea sourceTextArea;
	private JTextArea solutionTextArea;
	private Box box;
	private JButton checkButton;
	private JCheckBox autoCheckBox;

	public SudokuFrame() {
		super("Sudoku Solver");
		setLayout(new BorderLayout(4, 4));
		sourceTextArea = new JTextArea(15, 20);
		sourceTextArea.setBorder(new TitledBorder("Puzzle"));
		add(sourceTextArea, BorderLayout.WEST);
		solutionTextArea = new JTextArea(15, 20);
		solutionTextArea.setEditable(false);
		solutionTextArea.setBorder(new TitledBorder("Solution"));
		add(solutionTextArea, BorderLayout.EAST);
		box = Box.createHorizontalBox();
		checkButton = new JButton("Check");
		autoCheckBox = new JCheckBox("Auto Check");
		autoCheckBox.setSelected(true);
		box.add(checkButton);
		box.add(autoCheckBox);
		add(box, BorderLayout.SOUTH);

		checkButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				solvePuzzle();
			}
		});

		sourceTextArea.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				if (autoCheckBox.isSelected()) {
					solvePuzzle();
				}
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				if (autoCheckBox.isSelected()) {
					solvePuzzle();
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				if (autoCheckBox.isSelected()) {
					solvePuzzle();
				}
			}
		});

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	private void solvePuzzle() {
        Sudoku sudoku = null;
        try {
            sudoku = new Sudoku(Sudoku.textToGrid(sourceTextArea.getText()));
        } catch (Exception e) {
            solutionTextArea.setText("Parsing Problem");
        }
        int solutions = sudoku.solve();
		solutionTextArea.setText(sudoku.getSolutionText() + "\nsolutions:" + solutions + "\nelapsed:" + sudoku.getElapsed() + "ms");
    }
	
	
	public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
		
		SudokuFrame frame = new SudokuFrame();
	}

}
