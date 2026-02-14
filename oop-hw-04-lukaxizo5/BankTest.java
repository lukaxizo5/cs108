import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BankTest {
    private static final String SMALL = "small.txt";
    private static final String MEDIUM = "5k.txt";
    private static final String LARGE = "100k.txt";
    private static final String MINE = "mine.txt";

    @Test
    public void smallTest4Workers() throws IOException, InterruptedException {
        Bank bank = new Bank(4);
        ArrayList<Account> ans = calculateAnswers(bank, SMALL);
        bank.processFile(SMALL, 4);
        ArrayList<Account> threadAns = bank.getAccounts();
        for (int i = 0; i < Bank.ACCOUNTS; i++) {
            assertEquals(threadAns.get(i).toString(), ans.get(i).toString());
        }
        assertEquals(10, bank.getTransactionsMade());
        assertTrue(bank.allTransactionsWereMade());
        assertEquals(4, bank.getThreadsStarted());
        assertEquals(4, bank.getThreadsFinished());
    }

    @Test
    public void mediumTest10Workers() throws IOException, InterruptedException {
        Bank bank = new Bank(10);
        ArrayList<Account> ans = calculateAnswers(bank, MEDIUM);
        bank.processFile(MEDIUM, 10);
        assertEquals(10, bank.getThreadsStarted());
        assertEquals(10, bank.getThreadsFinished());
        ArrayList<Account> threadAns = bank.getAccounts();
        for (int i = 0; i < Bank.ACCOUNTS; i++) {
            assertEquals(threadAns.get(i).toString(), ans.get(i).toString());
        }
        assertEquals(5000, bank.getTransactionsMade());
        assertTrue(bank.allTransactionsWereMade());
        assertEquals(10, bank.getThreadsStarted());
        assertEquals(10, bank.getThreadsFinished());
    }

    @Test
    public void largeTest100Workers() throws IOException, InterruptedException {
        Bank bank = new Bank(100);
        ArrayList<Account> ans = calculateAnswers(bank, LARGE);
        bank.processFile(LARGE, 100);
        ArrayList<Account> threadAns = bank.getAccounts();
        for (int i = 0; i < Bank.ACCOUNTS; i++) {
            assertEquals(threadAns.get(i).toString(), ans.get(i).toString());
        }
        assertEquals(100000, bank.getTransactionsMade());
        assertTrue(bank.allTransactionsWereMade());
        assertEquals(100, bank.getThreadsStarted());
        assertEquals(100, bank.getThreadsFinished());
    }

    @Test
    public void customTest() throws IOException, InterruptedException {
        ArrayList<String> precomputedAnswers = new ArrayList<>();
        precomputedAnswers.add("acct:0 bal:996 trans:2");
        precomputedAnswers.add("acct:1 bal:997 trans:4");
        precomputedAnswers.add("acct:2 bal:1005 trans:2");
        precomputedAnswers.add("acct:3 bal:1002 trans:2");
        for (int i = 4; i < 20; i++) {
            precomputedAnswers.add("acct:" + i + " bal:1000 trans:0");
        }
        Bank bank = new Bank(2);
        bank.processFile(MINE, 2);
        ArrayList<Account> threadAns = bank.getAccounts();
        for (int i = 0; i < Bank.ACCOUNTS; i++) {
            assertEquals(threadAns.get(i).toString(), precomputedAnswers.get(i));
        }
        assertEquals(5, bank.getTransactionsMade());
        assertTrue(bank.allTransactionsWereMade());
        assertEquals(2, bank.getThreadsStarted());
        assertEquals(2, bank.getThreadsFinished());
    }

    @Test
    public void mainTest() throws IOException, InterruptedException {
        String expected = "";
        expected += "acct:0 bal:996 trans:2\n";
        expected += "acct:1 bal:997 trans:4\n";
        expected += "acct:2 bal:1005 trans:2\n";
        expected += "acct:3 bal:1002 trans:2\n";
        for (int i = 4; i < 20; i++) {
            expected += "acct:" + i + " bal:1000 trans:0\n";
        }
        OutputStream output = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(output);
        System.setOut(printStream);
        Bank.main(new String[] {MINE, "4"});
        assertEquals(expected, output.toString());
    }

    private ArrayList<Account> calculateAnswers(Bank bank, String file) throws IOException {
        ArrayList<Account> accounts = new ArrayList<>();
        for (int i = 0; i < Bank.ACCOUNTS; i++) {
            accounts.add(new Account(bank, i, Bank.INITIAL_BALANCE));
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));
        StreamTokenizer tokenizer = new StreamTokenizer(reader);
        while (true) {
            int read = tokenizer.nextToken();
            if (read == StreamTokenizer.TT_EOF) break;
            int from = (int) tokenizer.nval;

            tokenizer.nextToken();
            int to = (int) tokenizer.nval;

            tokenizer.nextToken();
            int amount = (int) tokenizer.nval;

            accounts.get(from).makeTransaction(-amount);
            accounts.get(to).makeTransaction(amount);
        }

        return accounts;
    }

}
