import java.util.HashMap;
import java.util.Map;

public class AccountManager {
    private Map<String, String> userPasswords;

    public AccountManager() {
        userPasswords = new HashMap<>();
        userPasswords.put("Patrick", "1234");
        userPasswords.put("Molly", "FloPup");
    }

    public boolean accountExists(String username) {
        return userPasswords.containsKey(username);
    }

    public boolean isPasswordCorrect(String username, String password) {
        return accountExists(username) && password.equals(userPasswords.get(username));
    }

    public void createNewAccount(String username, String password) {
        userPasswords.put(username, password);
    }
}
