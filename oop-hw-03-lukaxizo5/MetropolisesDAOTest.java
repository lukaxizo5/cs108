import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MetropolisesDAOTest {
    private static Connection connection;
    private MetropolisesDAO metropolisesDAO;

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
    }

    @AfterEach
    public void deleteFromTable() throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM metropolises");
        statement.close();
    }

    @Test
    public void addMetropolisTest() throws SQLException {
        metropolisesDAO.addMetropolis(new Metropolis("Tbilisi", "Europe", 1300000));
        List<Metropolis> results = metropolisesDAO.getAllMetropolises();
        assertEquals(1,  results.size());
        metropolisesDAO.addMetropolis(new Metropolis("Tokyo", "Asia", 37468000));
        results = metropolisesDAO.getAllMetropolises();
        assertEquals(2,  results.size());
        metropolisesDAO.addMetropolis(new Metropolis("Shanghai", "Asia", 25582000));
        results = metropolisesDAO.getAllMetropolises();
        assertEquals(3,  results.size());
        metropolisesDAO.addMetropolis(new Metropolis("Sao Paulo", "South America", 21650000));
        results = metropolisesDAO.getAllMetropolises();
        assertEquals(4,  results.size());
    }

    @Test
    public void searchMetropolisesTest() throws SQLException {
        metropolisesDAO.addMetropolis(new Metropolis("Tbilisi", "Europe", 1300000));
        metropolisesDAO.addMetropolis(new Metropolis("Tokyo", "Asia", 37468000));
        metropolisesDAO.addMetropolis(new Metropolis("Shanghai", "Asia", 25582000));
        metropolisesDAO.addMetropolis(new Metropolis("Sao Paulo", "South America", 21650000));
        metropolisesDAO.addMetropolis(new Metropolis("New York", "North America", 21295000));
        metropolisesDAO.addMetropolis(new Metropolis("London", "Europe", 8580000));
        metropolisesDAO.addMetropolis(new Metropolis("Rome", "Europe", 2715000));
        metropolisesDAO.addMetropolis(new Metropolis("San Francisco", "North America", 5780000));
        List<Metropolis> results = metropolisesDAO.getAllMetropolises();
        assertEquals(8,  results.size());
        results = metropolisesDAO.searchMetropolis(new Metropolis(null, "Europe", 0), true, true);
        assertEquals(3, results.size());
        results = metropolisesDAO.searchMetropolis(new Metropolis(null, "Europe", 1300000), true, true);
        assertEquals(2, results.size());
        results = metropolisesDAO.searchMetropolis(new Metropolis("Sa", null, 1300000), true, false);
        assertEquals(2, results.size());
        results = metropolisesDAO.searchMetropolis(new Metropolis("Sa", null, 20000000), false, false);
        assertEquals(1, results.size());
        results = metropolisesDAO.searchMetropolis(new Metropolis("Sao Paulo", "South America", 20000000), true, true);
        assertEquals(1, results.size());
        results = metropolisesDAO.searchMetropolis(new Metropolis(null, null, 25000000), true, true);
        assertEquals(2, results.size());
        results = metropolisesDAO.searchMetropolis(new Metropolis(null, "As", 0), true, false);
        assertEquals(2, results.size());
        results = metropolisesDAO.searchMetropolis(new Metropolis(null, "No%",  0), true, true);
        assertEquals(2, results.size());
        results = metropolisesDAO.searchMetropolis(new Metropolis("Madrid", null, 1000000), true, false);
        assertEquals(0, results.size());
    }

}
