import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class AccountManagerTest {
    private AccountManager accountManager;

    @BeforeEach
    public void setup() {
        accountManager = new AccountManager();
    }

    @Test
    public void accountExistsTest() {
        assertTrue(accountManager.accountExists("Patrick"));
        assertTrue(accountManager.accountExists("Molly"));
        assertFalse(accountManager.accountExists("Luka"));
    }

    @Test
    public void isPasswordCorrectTest() {
        assertTrue(accountManager.isPasswordCorrect("Patrick", "1234"));
        assertTrue(accountManager.isPasswordCorrect("Molly", "FloPup"));
        assertFalse(accountManager.isPasswordCorrect("Patrick", "1234 "));
        assertFalse(accountManager.isPasswordCorrect("Molly", "flopUp"));
        assertFalse(accountManager.isPasswordCorrect("Luka", "lkhiz23"));
    }

    @Test
    public void createNewAccountTest() {
        assertFalse(accountManager.accountExists("Luka"));
        accountManager.createNewAccount("Luka", "Xizusha");
        assertTrue(accountManager.accountExists("Luka"));
        assertTrue(accountManager.isPasswordCorrect("Luka", "Xizusha"));
        assertFalse(accountManager.isPasswordCorrect("Luka", "lkhiz23"));
    }

    @Test
    public void overwriteAccountTest() {
        accountManager.createNewAccount("Patrick", "papa");
        assertTrue(accountManager.accountExists("Patrick"));
        assertTrue(accountManager.isPasswordCorrect("Patrick", "papa"));
        assertFalse(accountManager.isPasswordCorrect("Patrick", "1234"));
    }

}
