import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CrackerTest {
    @Test
    public void generateHashTest() throws NoSuchAlgorithmException {
        assertEquals(Cracker.hexToString(Cracker.generateHashValue("molly")), "4181eecbd7a755d19fdf73887c54837cbecf63fd");
        assertEquals(Cracker.hexToString(Cracker.generateHashValue("knock")), "03c065bae9f5b4e26da85dffaae7e5180ce43f85");
        assertEquals(Cracker.hexToString(Cracker.generateHashValue("z!!-z")), "60f3b6ef79964577d8d32d13caea6589c469c1a3");
        assertEquals(Cracker.hexToString(Cracker.generateHashValue("a!")), "34800e15707fae815d7c90d49de44aca97e2d759");
        assertEquals(Cracker.hexToString(Cracker.generateHashValue("xyz")), "66b27417d37e024c46526c2f6d358a754fc552f3");
        assertEquals(Cracker.hexToString(Cracker.generateHashValue("xizo")), "d02699c2e9ca794177d2274d58d30c855ee87625");
    }

    @Test
    public void crackPasswordTest1() throws NoSuchAlgorithmException, InterruptedException {
        Cracker cracker = new Cracker();
        cracker.crackPassword("e0c9035898dd52fc65c41454cec9c4d2611bfb37", 2, 3);
        assertEquals("aa", cracker.getPassword());
        assertTrue(cracker.getCracked());
    }

    @Test
    public void crackPasswordTest2() throws NoSuchAlgorithmException, InterruptedException {
        Cracker cracker = new Cracker();
        cracker.crackPassword("66b27417d37e024c46526c2f6d358a754fc552f3", 3, 4);
        assertEquals("xyz", cracker.getPassword());
        assertTrue(cracker.getCracked());
    }

    @Test
    public void crackPasswordTest3() throws NoSuchAlgorithmException, InterruptedException {
        Cracker cracker = new Cracker();
        cracker.crackPassword("34800e15707fae815d7c90d49de44aca97e2d759", 3, 2);
        assertEquals("a!", cracker.getPassword());
        assertTrue(cracker.getCracked());
    }

    @Test
    public void crackPasswordTest4() throws NoSuchAlgorithmException, InterruptedException {
        Cracker cracker = new Cracker();
        cracker.crackPassword("d02699c2e9ca794177d2274d58d30c855ee87625", 4, 7);
        assertEquals("xizo", cracker.getPassword());
        assertTrue(cracker.getCracked());
    }

    @Test
    public void mainTest1() throws NoSuchAlgorithmException, InterruptedException {
        OutputStream output = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(output);
        System.setOut(printStream);
        Cracker.main(new String[] {"molly"});
        assertEquals("4181eecbd7a755d19fdf73887c54837cbecf63fd\n", output.toString());
    }

    @Test
    public void mainTest2() throws NoSuchAlgorithmException, InterruptedException {
        OutputStream output = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(output);
        System.setOut(printStream);
        Cracker.main(new String[] {"f55438a1640220c3fcf7ab0162061c51c9572fc7", "3", "4"});
        assertEquals("brb\nall done\n", output.toString());
    }

}
