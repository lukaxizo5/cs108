import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MetropolisesTableModelTest {
    private static Connection connection;
    private MetropolisesDAO metropolisesDAO;
    private MetropolisesTableModel metropolisesTableModel;

    @BeforeAll
    public static void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        Statement statement = connection.createStatement();
        statement.executeUpdate("CREATE TABLE metropolises (" +
                "metropolis CHAR(64), " +
                "continent CHAR(64), " +
                "population BIGINT)");
        statement.close();
    }

    @BeforeEach
    public void setUpEach() throws SQLException {
        metropolisesDAO = new MetropolisesDAO(connection);
        metropolisesTableModel = new MetropolisesTableModel(metropolisesDAO);
    }

    @AfterEach
    public void deleteFromTable() throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM metropolises");
        statement.close();
    }

    @Test
    public void addTest() throws SQLException {
        metropolisesTableModel.add(new Metropolis("Tbilisi", "Europe", 1300000));
        assertEquals(1, metropolisesTableModel.getRowCount());
        metropolisesTableModel.add(new Metropolis("Tokyo", "Asia", 37468000));
        assertEquals(2, metropolisesTableModel.getRowCount());
        metropolisesTableModel.add(new Metropolis("Sao Paulo", "South America", 21650000));
        assertEquals(3, metropolisesTableModel.getRowCount());

        assertTrue(metropolisesTableModel.getValueAt(0, 0).equals("Tbilisi"));
        assertTrue(metropolisesTableModel.getValueAt(0, 1).equals("Europe"));
        assertEquals(1300000, metropolisesTableModel.getValueAt(0, 2));

        assertTrue(metropolisesTableModel.getValueAt(1, 0).equals("Tokyo"));
        assertTrue(metropolisesTableModel.getValueAt(1, 1).equals("Asia"));
        assertEquals(37468000, metropolisesTableModel.getValueAt(1, 2));

        assertTrue(metropolisesTableModel.getValueAt(2, 0).equals("Sao Paulo"));
        assertTrue(metropolisesTableModel.getValueAt(2, 1).equals("South America"));
        assertEquals(21650000, metropolisesTableModel.getValueAt(2, 2));

        assertEquals(null, metropolisesTableModel.getValueAt(-1, 0));
        assertEquals(null, metropolisesTableModel.getValueAt(3, 0));
        assertEquals(null, metropolisesTableModel.getValueAt(0, 4));
    }

    @Test
    public void searchTest() throws SQLException {
        metropolisesTableModel.add(new Metropolis("Tbilisi", "Europe", 1300000));
        metropolisesTableModel.add(new Metropolis("Tokyo", "Asia", 37468000));
        metropolisesTableModel.add(new Metropolis("Sao Paulo", "South America", 21650000));
        metropolisesTableModel.add(new Metropolis("New York", "North America", 21295000));
        metropolisesTableModel.add(new Metropolis("San Francisco", "North America", 5780000));
        metropolisesTableModel.search(new Metropolis(null, "North", 5000000), true, false);
        assertEquals(2, metropolisesTableModel.getRowCount());
        metropolisesTableModel.search(new Metropolis(null, "North", 6000000), true, false);
        assertEquals(1, metropolisesTableModel.getRowCount());
    }

    @Test
    public void getColumnsTest() {
        assertEquals("Metropolis", metropolisesTableModel.getColumnName(0));
        assertEquals("Continent", metropolisesTableModel.getColumnName(1));
        assertEquals("Population", metropolisesTableModel.getColumnName(2));
        assertThrows(IllegalStateException.class, () -> metropolisesTableModel.getColumnName(3));

        assertEquals(3, metropolisesTableModel.getColumnCount());
    }
}
