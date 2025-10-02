package mainProgram;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionManagerTest {

    TransactionManager manager;


    @BeforeEach
    public void setUp() {
        TransactionRepository repo = new inMemoryTransactionRepository();
        manager = new TransactionManager(repo);
    }

    @Test
    void testAddAndGetAllTransactions() {
        Transaction t1 = new Transaction("Lön", 10000, LocalDate.of(2025, 1, 1));
        Transaction t2 = new Transaction("mat", -1500, LocalDate.of(2025, 1, 1));
        manager.addTransaction(t1);
        manager.addTransaction(t2);

        List<Transaction> all = manager.getAllTransactions();
        assertEquals(2, all.size());
        assertEquals(t1, all.get(0));
        assertEquals(t2, all.get(1));
        assertEquals(-1500, all.get(1).getAmount());
        assertEquals(10000, all.get(0).getAmount());
    }

    @Test
    void testRemoveTransaction() {
        Transaction t1 = new Transaction("Mat", -2000, LocalDate.of(2025, 1, 1));
        manager.addTransaction(t1);

        assertTrue(manager.removeTransaction(0));
        assertFalse(manager.removeTransaction(5));
    }

    @Test
    void testUpdateTransactionDesc() {
        Transaction t1 = new Transaction("Mat", -2000, LocalDate.of(2025, 1, 1));
        manager.addTransaction(t1);

        manager.updateTransaction(0, 1, "Kaffe", 0.0, null);
        assertEquals("Kaffe", t1.getDescription());
    }

    @Test
    void testUpdateTransactionBalance() {
        Transaction t1 = new Transaction("Mat", -2000, LocalDate.of(2025, 1, 1));
        manager.addTransaction(t1);

        manager.updateTransaction(0, 2, null, 2000, null);
        assertEquals(2000, manager.getBalance());
    }

    @Test
    void testUpdateTransactionBalanceUI (){
        Transaction t1 = new Transaction("Mat", -2000, LocalDate.of(2025, 1, 1));
        manager.addTransaction(t1);

        manager.updateTransactionForUi(0,  null, 2000, null);
        assertEquals(2000, manager.getBalance());
    }


    @Test
    void testFilterByYear() {
        Transaction t1 = new Transaction("Lön", 40000, LocalDate.of(2025, 1, 1));
        Transaction t2 = new Transaction("Mat", -2000, LocalDate.of(2024, 1, 1));

        manager.addTransaction(t1);
        manager.addTransaction(t2);

        List<Transaction> all = manager.filterByPeriod(1, "2025", null, null, null);
        assertEquals(1, all.size());
        assertEquals(t1, all.get(0));
    }

    @Test
    void testFilterByMonth() {
        Transaction t1 = new Transaction("Godis", -5000, LocalDate.of(2025, 2, 1));
        Transaction t2 = new Transaction("Golf klubbor", -150, LocalDate.of(2025, 3, 5));
        manager.addTransaction(t1);
        manager.addTransaction(t2);

        List<Transaction> filtered = manager.filterByPeriod(2, null, YearMonth.of(2025, 2), null, null);
        assertEquals(1, filtered.size());
        assertEquals(t1, filtered.get(0));
    }

    @Test
    void testIncomeAndSpentSum() {
        Transaction t1 = new Transaction("Lön", 10000, LocalDate.now());
        Transaction t2 = new Transaction("Mat", -200, LocalDate.now());
        manager.addTransaction(t1);
        manager.addTransaction(t2);

        List<Transaction> all = manager.getAllTransactions();
        assertEquals(10000, manager.getIncomeSum(all));
        assertEquals(-200, manager.getSpentSum(all));
    }
}
