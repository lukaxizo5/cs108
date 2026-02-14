// JCount.java

/*
 Basic GUI/Threading exercise.
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JCount extends JPanel {
	private JTextField textField;
	private JLabel label;
	private JButton startButton;
	private JButton stopButton;
	private Worker worker;

	public JCount() {
		// Set the JCount to use Box layout
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		createAndShowGUI();
	}

	private void createAndShowGUI() {
		textField = new JTextField("100000000", 10);
		label = new JLabel("0");
		startButton = new JButton("Start");
		stopButton = new JButton("Stop");
		add(textField);
		add(label);
		add(startButton);
		add(stopButton);
		add(Box.createRigidArea(new Dimension(0, 40)));
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (worker != null) {
					worker.interrupt();
				}
				label.setText("0");
				worker = new Worker(Integer.parseInt(textField.getText()));
				worker.start();
			}
		});
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (worker != null) {
					worker.interrupt();
					worker = null;
				}
			}
		});
	}

	private class Worker extends Thread {
		int count;
		private static final int GAP = 10000;

		public Worker(int count) {
			this.count = count;
		}

		@Override
		public void run() {
			for (int i = 0; i <= count; i++) {
				if (i % GAP == 0 || i == count) {
					if (isInterrupted()) {
						break;
					}
					int finalI = i;
					SwingUtilities.invokeLater(() -> {
						label.setText(String.valueOf(finalI));
					});
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
			}
			// Marks worker as finished so it can't be "stopped".
			SwingUtilities.invokeLater(() -> {
				worker = null;
			});

		}
	}
	
	static public void main(String[] args)  {
		// Creates a frame with 4 JCounts in it.
		// (provided)
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("The Count");
			frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

			frame.add(new JCount());
			frame.add(new JCount());
			frame.add(new JCount());
			frame.add(new JCount());

			frame.pack();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		});
	}
}

