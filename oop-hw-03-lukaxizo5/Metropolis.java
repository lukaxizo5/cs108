/**
 * Represents a Metropolis with a name, its continent, and population.
 * <p>
 * This class serves as a simple data container (POJO) for information about a metropolis.
 * It is used to store and retrieve data about different cities from the database.
 * </p>
 *
 * Example usage:
 * <pre>
 *     Metropolis m = new Metropolis("Tokyo", "Asia", 37400068);
 *     String city = m.getMetropolis(); // returns "Tokyo"
 * </pre>
 */
public class Metropolis {
    private String metropolis;
    private String continent;
    private int population;

    /**
     * Constructs a new Metropolis with the given name, continent, and population.
     *
     * @param metropolis the name of the city
     * @param continent  the continent the city belongs to
     * @param population the population of the city
     */
    public Metropolis(String metropolis, String continent, int population) {
        this.metropolis = metropolis;
        this.continent = continent;
        this.population = population;
    }

    /**
     * @return the metropolis name
     */
    public String getMetropolis() {
        return metropolis;
    }

    /**
     * @return the continent name
     */
    public String getContinent() {
        return continent;
    }

    /**
     * @return the population value
     */
    public int getPopulation() {
        return population;
    }
}
