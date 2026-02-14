import javax.swing.table.AbstractTableModel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Table model for displaying and manipulating a list of metropolises in a {@link javax.swing.JTable}.
 * <p>
 * This model interacts with a {@link MetropolisesDAO} to support adding and searching records.
 * It extends {@link AbstractTableModel} and overrides required methods for table rendering.
 * </p>
 */
public class MetropolisesTableModel extends AbstractTableModel {
    private static final int METROPOLIS_ID = 0;
    private static final int CONTINENT_ID = 1;
    private static final int POPULATION_ID = 2;
    private static final int COLUMNS = 3;
    private MetropolisesDAO metropolisesDAO;
    private ArrayList<Metropolis> metropolises;

    /**
     * Constructs a new table model using the specified DAO and loads all metropolises initially.
     *
     * @param metropolisesDAO the DAO to retrieve and update metropolises in the database
     * @throws SQLException if a database access error occurs
     */
    public MetropolisesTableModel(MetropolisesDAO metropolisesDAO) throws SQLException {
        this.metropolisesDAO = metropolisesDAO;
        this.metropolises = metropolisesDAO.getAllMetropolises();
        displayFullTable();
    }

    /**
     * Notifies the table that all data should be refreshed.
     */
    public void displayFullTable() {
        fireTableDataChanged();
    }

    /**
     * Adds a new metropolis to the database and updates the table.
     *
     * @param metropolis the new {@link Metropolis} to add
     * @throws SQLException if a database access error occurs
     */
    public void add(Metropolis metropolis) throws SQLException {
        metropolisesDAO.addMetropolis(metropolis);
        metropolises.add(metropolis);
        fireTableRowsInserted(metropolises.size() - 1, metropolises.size() - 1);
    }

    /**
     * Searches for metropolises based on the given criteria and updates the table model.
     *
     * @param metropolis  the {@link Metropolis} object with search fields
     * @param largerThan  whether to filter for population greater/less than the given value
     * @param exactMatch  whether to perform exact/partial string matching
     * @throws SQLException if a database access error occurs
     */
    public void search(Metropolis metropolis, boolean largerThan, boolean exactMatch) throws SQLException {
        metropolises = metropolisesDAO.searchMetropolis(metropolis, largerThan, exactMatch);
        fireTableDataChanged();
    }

    /**
     * Returns the number of rows in the table, equal to the number of metropolises.
     *
     * @return the row count
     */
    @Override
    public int getRowCount() {
        return metropolises.size();
    }

    /**
     * Returns the number of columns in the table.
     *
     * @return the column count (always 3)
     */
    @Override
    public int getColumnCount() {
        return COLUMNS;
    }

    /**
     * Returns the value at the specified row and column.
     *
     * @param rowIndex    the row index
     * @param columnIndex the column index
     * @return the cell value, or {@code null} if out of bounds
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= metropolises.size()) {
            return null;
        }

        Metropolis metropolis = metropolises.get(rowIndex);
        return switch(columnIndex) {
            case METROPOLIS_ID -> metropolis.getMetropolis();
            case CONTINENT_ID -> metropolis.getContinent();
            case POPULATION_ID -> metropolis.getPopulation();
            default -> null;
        };
    }

    /**
     * Returns the name of the specified column.
     *
     * @param columnIndex the column index
     * @return the column name
     * @throws IllegalStateException if the column index is invalid
     */
    @Override
    public String getColumnName(int columnIndex) {
        return switch(columnIndex) {
            case METROPOLIS_ID -> "Metropolis";
            case CONTINENT_ID -> "Continent";
            case POPULATION_ID -> "Population";
            default -> throw new IllegalStateException("Unexpected value: " + columnIndex);
        };
    }
}
