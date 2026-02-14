import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Data Access Object (DAO) class for interacting with the "metropolises_db" database table.
 * <p>
 * This class provides methods to add, search, and retrieve all metropolises from the database.
 * It uses JDBC to execute SQL queries securely using prepared statements.
 * </p>
 */
public class MetropolisesDAO {
    private final Connection connection;

    /**
     * Constructs a new MetropolisesDAO with the specified database connection.
     *
     * @param connection the active JDBC connection to the database
     */
    public MetropolisesDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Adds a new metropolis entry to the database.
     *
     * @param metropolis the {@link Metropolis} object to add
     * @throws SQLException if a database access error occurs
     */
    public void addMetropolis(Metropolis metropolis) throws SQLException {
        String query = "INSERT INTO metropolises VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, metropolis.getMetropolis());
        preparedStatement.setString(2, metropolis.getContinent());
        preparedStatement.setInt(3, metropolis.getPopulation());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    /**
     * Searches for metropolises based on the given criteria.
     *
     * @param metropolis the search criteria object (fields can be null or 0 (population) if not used)
     * @param largerThan if {@code true}, filters for population greater than the given value;
     *                   if {@code false}, filters for less than
     * @param exactMatch if {@code true}, performs exact match on strings; if {@code false}, uses SQL LIKE for partial match
     * @return a list of metropolises matching the search criteria
     * @throws SQLException if a database access error occurs
     */
    public ArrayList<Metropolis> searchMetropolis(Metropolis metropolis, boolean largerThan, boolean exactMatch) throws SQLException {
        StringBuilder query = new StringBuilder("SELECT * FROM metropolises");
        boolean where = false;
        ArrayList<Object> params = new ArrayList<>();

        if (metropolis.getMetropolis() != null) {
            query.append(" WHERE metropolis ");
            if (exactMatch) {
                query.append("LIKE ?");
                params.add(metropolis.getMetropolis());
            }
            else {
                query.append("LIKE ?");
                params.add("%" + metropolis.getMetropolis() + "%");
            }
            where = true;
        }

        if (metropolis.getContinent() != null) {
            if (where) {
                query.append(" AND continent ");
            }
            else {
                query.append(" WHERE continent ");
            }
            if (exactMatch) {
                query.append("LIKE ?");
                params.add(metropolis.getContinent());
            }
            else {
                query.append("LIKE ?");
                params.add("%" + metropolis.getContinent() + "%");
            }
            where = true;
        }

        if (metropolis.getPopulation() != 0) {
            if (where) {
                query.append(" AND population ");
            }
            else {
                query.append(" WHERE population ");
            }
            if (largerThan) {
                query.append("> ?");
            }
            else {
                query.append("< ?");
            }
            params.add(metropolis.getPopulation());
            where = true;
        }

        PreparedStatement preparedStatement =  connection.prepareStatement(query.toString());
        for (int i = 0; i < params.size(); i++) {
            preparedStatement.setObject(i + 1, params.get(i));
        }

        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Metropolis> metropolises = new ArrayList<>();
        while (resultSet.next()) {
            metropolises.add(new Metropolis(resultSet.getString("metropolis"), resultSet.getString("continent"), resultSet.getInt("population")));
        }
        preparedStatement.close();
        return metropolises;
    }

    /**
     * Retrieves all metropolises from the database.
     *
     * @return a list of all metropolises in the database
     * @throws SQLException if a database access error occurs
     */
    public ArrayList<Metropolis> getAllMetropolises() throws SQLException {
        ArrayList<Metropolis> metropolises = new ArrayList<>();
        String query = "SELECT * FROM metropolises";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            String metropolis = resultSet.getString("metropolis");
            String continent = resultSet.getString("continent");
            int population = resultSet.getInt("population");
            metropolises.add(new Metropolis(metropolis, continent, population));
        }
        return metropolises;
    }

}
