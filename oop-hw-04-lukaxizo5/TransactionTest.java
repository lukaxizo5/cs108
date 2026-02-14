import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TransactionTest {
    @Test
    public void gettersTest() {
        Transaction transaction = new Transaction(1, 6, 100);
        assertEquals(1, transaction.getFrom());
        assertEquals(6, transaction.getTo());
        assertEquals(100, transaction.getAmount());
    }

    @Test
    public void equalsTest() {
        Transaction transaction1 = new Transaction(7, 12, 19);
        Transaction transaction2 = new Transaction(7, 12, 19);
        Transaction transaction3 = new Transaction(7, 13, 19);
        Transaction transaction4 = new Transaction(7, 12, 29);
        Transaction transaction5 = new Transaction(9, 10, 29);
        assertEquals(transaction1, transaction2);
        assertNotEquals(transaction1, 10);
        assertNotEquals(transaction1, null);
        assertNotEquals(transaction1, transaction3);
        assertNotEquals(transaction1, transaction4);
        assertNotEquals(transaction1, transaction5);
    }

    @Test
    public void toStringTest() {
        Transaction transaction1 = new Transaction(7, 12, 19);
        Transaction transaction2 = new Transaction(1, 2, 3);
        assertEquals("from:7 to:12 amt:19", transaction1.toString());
        assertEquals("from:1 to:2 amt:3", transaction2.toString());
    }
}
