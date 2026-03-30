package model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionTest {

    @Test
    public void testSaleTransaction() {
        LocalDateTime date = LocalDateTime.of(2026, 3, 22, 14, 30);
        Transaction transaction = new Transaction(1, "Order 1 - 5 x Bricks [PD001]", 250.0, date);

        assertEquals(1, transaction.getId());
        assertEquals("Order 1 - 5 x Bricks [PD001]", transaction.getDescription());
        assertEquals(250.0, transaction.getAmount(), "Transaction records the full sale amount");
    }
}




