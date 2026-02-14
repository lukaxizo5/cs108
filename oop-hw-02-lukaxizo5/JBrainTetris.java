import javax.swing.*;
import java.awt.*;
import java.util.random.RandomGenerator;

public class JBrainTetris extends JTetris{
    private JCheckBox brainMode;
    private DefaultBrain brain;
    Brain.Move bestMove;
    int count;
    JSlider adversary;
    JLabel status;
    Piece[] pieces;


    /**
     * Creates a new JTetris where each tetris square
     * is drawn with the given number of pixels.
     *
     * @param pixels
     */
    JBrainTetris(int pixels) {
        super(pixels);
        brain = new DefaultBrain();
        bestMove = new Brain.Move();
        pieces = Piece.getPieces();
    }

    @Override
    public JComponent createControlPanel() {
        JPanel panel = (JPanel) super.createControlPanel();

        JPanel brain = new JPanel();
        brain.setLayout(new BoxLayout(brain, BoxLayout.Y_AXIS));
        brain.add(new JLabel("Brain:"));
        brainMode = new JCheckBox("Brain active");
        brainMode.setSelected(false);
        brain.add(brainMode);

        JPanel adversary = new JPanel();
        adversary.setLayout(new BoxLayout(adversary, BoxLayout.Y_AXIS));
        adversary.add(new JLabel("Adversary:"));
        this.adversary =  new JSlider(0,100,0);
        adversary.add(this.adversary);

        status = new JLabel("ok");
        adversary.add(status);

        panel.add(brain);
        panel.add(adversary);

        return panel;
    }

    @Override
    public void tick(int verb) {
        if (verb == DOWN && brainMode.isSelected()) {
            if (count < super.count) {
                board.undo();
                count = super.count;
                bestMove = brain.bestMove(board, currentPiece, board.getHeight() - TOP_SPACE, null);
            }
            if (bestMove != null) {
                if (!bestMove.piece.equals(currentPiece)) {
                    super.tick(ROTATE);
                }
                if (bestMove.x < currentX) {
                    super.tick(LEFT);
                }
                if (bestMove.x > currentX) {
                    super.tick(RIGHT);
                }
            }
        }
        super.tick(verb);
    }

    @Override
    public Piece pickNextPiece() {
        if (super.random.nextInt(99) + 1 >= adversary.getValue()) {
            status.setText("ok");
            return super.pickNextPiece();
        }

        status.setText("*ok*");
        Piece worstPiece = null;
        double worstScore = Double.NEGATIVE_INFINITY;
        for (Piece p : pieces) {
            Brain.Move move = brain.bestMove(board, p, board.getHeight() - TOP_SPACE, null);
            if (move != null && move.score > worstScore ) {
                worstPiece = p;
                worstScore = move.score;
            }
        }

        if  (worstPiece == null) {
            return super.pickNextPiece();
        }
        return worstPiece;
    }

    @Override
    public void startGame() {
        count = 0;
        super.startGame();
    }

    public static void main(String[] args) {
        // Set GUI Look And Feel Boilerplate.
        // Do this incantation at the start of main() to tell Swing
        // to use the GUI LookAndFeel of the native platform. It's ok
        // to ignore the exception.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }

        JBrainTetris tetris = new JBrainTetris(16);
        JFrame frame = JBrainTetris.createFrame(tetris);
        frame.setVisible(true);
    }

}
