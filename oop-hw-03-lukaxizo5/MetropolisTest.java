import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MetropolisTest {
    @Test
    public void SampleTest() {
        Metropolis metropolis = new Metropolis("Tokyo", "Asia", 37468000);
        assertTrue(metropolis.getMetropolis().equals("Tokyo"));
        assertTrue(metropolis.getContinent().equals("Asia"));
        assertEquals(37468000, metropolis.getPopulation());
    }

    @Test
    public void nullsTest() {
        Metropolis metropolis1 = new Metropolis(null, "Asia", 37468000);
        assertEquals(null, metropolis1.getMetropolis());
        assertTrue(metropolis1.getContinent().equals("Asia"));
        assertEquals(37468000, metropolis1.getPopulation());

        Metropolis metropolis2 = new Metropolis("Tokyo", null, 37468000);
        assertTrue(metropolis2.getMetropolis().equals("Tokyo"));
        assertEquals(null, metropolis2.getContinent());
        assertEquals(37468000, metropolis2.getPopulation());

        Metropolis metropolis3 = new Metropolis(null, null, 12500000);
        assertEquals(null, metropolis3.getMetropolis());
        assertEquals(null, metropolis3.getContinent());
        assertEquals(12500000, metropolis3.getPopulation());
    }
}
