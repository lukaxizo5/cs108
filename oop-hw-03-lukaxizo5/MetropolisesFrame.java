import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * The MetropolisesFrame class represents the main UI window for managing and searching a database
 * of metropolises. It includes fields for adding new entries and a table to display the current results.
 */
public class MetropolisesFrame extends JFrame {
    private static final String POPULATION_LARGER  = "Population Larger Than";
    private static final String POPULATION_SMALLER = "Population Smaller Than";
    private static final String EXACT_MATCH = "Exact Match";
    private static final String PARTIAL_MATCH = "Partial Match";

    JTextField metropolisTextField;
    JTextField continentTextField;
    JTextField populationTextField;
    JButton addButton;
    JButton searchButton;
    JComboBox populationComboBox;
    JComboBox exactMatchComboBox;
    MetropolisesTableModel metropolisesTableModel;

    /**
     * Constructs the main application window and sets up the UI components.
     */
    public MetropolisesFrame() throws SQLException {
        super("Metropolis Viewer");
        setLayout(new BorderLayout(4, 4));

        MetropolisesDAO metropolisesDAO = new MetropolisesDAO(DBConnector.getConnection());
        metropolisesTableModel = new MetropolisesTableModel(metropolisesDAO);
        addNorthPanel();
        addEastPanel();
        addCenterPanel();

        addButton.addActionListener(new  ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String metropolis = metropolisTextField.getText();
                String continent = continentTextField.getText();
                String populationString = populationTextField.getText();
                if (!metropolis.isEmpty() && !continent.isEmpty() && !populationString.isEmpty()) {
                    int population = Integer.parseInt(populationString);
                    if (population > 0) {
                        try {
                            metropolisesTableModel.add(new Metropolis(metropolis, continent, population));
                            metropolisTextField.setText("");
                            continentTextField.setText("");
                            populationTextField.setText("");
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });

        searchButton.addActionListener(new  ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String metropolis = metropolisTextField.getText();
                String continent = continentTextField.getText();
                String populationString = populationTextField.getText();
                boolean largerThan = populationComboBox.getSelectedItem().equals(POPULATION_LARGER);
                boolean exactMatch = exactMatchComboBox.getSelectedItem().equals(EXACT_MATCH);
                int population = 0;
                if (metropolis.contains("%") || continent.contains("%")) exactMatch = false;
                if (metropolis.isEmpty()) metropolis = null;
                if (continent.isEmpty()) continent = null;
                if (populationString.isEmpty()) population = 0;
                else population = Integer.parseInt(populationString);
                try {
                    metropolisesTableModel.search(new Metropolis(metropolis, continent, population), largerThan, exactMatch);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Adds the table to the center of the frame.
     */
    private void addCenterPanel() {
        JTable table = new JTable(metropolisesTableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Adds the right-side panel with buttons and search options comboBoxes.
     */
    private void addEastPanel() {
        JPanel eastPanel = new JPanel();
        eastPanel.setPreferredSize(new Dimension(230, 600));
        eastPanel.setLayout(new GridLayout(6, 1));

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(2, 1));
        addButton = new JButton("Add");
        searchButton = new JButton("Search");
        buttonsPanel.add(addButton);
        buttonsPanel.add(searchButton);
        eastPanel.add(buttonsPanel);

        JPanel searchOptionsPanel = new JPanel();
        searchOptionsPanel.setLayout(new BoxLayout(searchOptionsPanel, BoxLayout.Y_AXIS));
        searchOptionsPanel.setBorder(new TitledBorder("Search Options"));
        populationComboBox = new JComboBox(new String[]{POPULATION_LARGER, POPULATION_SMALLER});
        exactMatchComboBox = new JComboBox(new String[]{EXACT_MATCH, PARTIAL_MATCH});
        searchOptionsPanel.add(populationComboBox);
        searchOptionsPanel.add(exactMatchComboBox);
        eastPanel.add(searchOptionsPanel);

        add(eastPanel, BorderLayout.EAST);
    }

    /**
     * Adds the top panel with text input fields.
     */
    private void addNorthPanel() {
        JPanel northPanel = new JPanel();
        JLabel metropolisLabel = new JLabel("Metropolis: ");
        JPanel metropolisPanel = new JPanel();
        metropolisPanel.setLayout(new BoxLayout(metropolisPanel, BoxLayout.X_AXIS));
        metropolisTextField = new JTextField(10);
        metropolisPanel.add(metropolisLabel);
        metropolisPanel.add(metropolisTextField);
        northPanel.add(metropolisPanel);

        JLabel continentLabel = new JLabel("Continent: ");
        JPanel continentPanel = new JPanel();
        continentPanel.setLayout(new BoxLayout(continentPanel, BoxLayout.X_AXIS));
        continentTextField = new JTextField(10);
        continentPanel.add(continentLabel);
        continentPanel.add(continentTextField);
        northPanel.add(continentPanel);

        JLabel populationLabel = new JLabel("Population: ");
        JPanel populationPanel = new JPanel();
        populationPanel.setLayout(new BoxLayout(populationPanel, BoxLayout.X_AXIS));
        populationTextField = new JTextField(10);
        populationPanel.add(populationLabel);
        populationPanel.add(populationTextField);
        northPanel.add(populationPanel);

        add(northPanel, BorderLayout.NORTH);
    }


    /**
     * Main method to launch the MetropolisesFrame GUI.
     */
    public static void main(String[] args) throws SQLException {
        // GUI Look And Feel
        // Do this incantation at the start of main() to tell Swing
        // to use the GUI LookAndFeel of the native platform. It's ok
        // to ignore the exception.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }
        MetropolisesFrame frame = new MetropolisesFrame();
    }
}
