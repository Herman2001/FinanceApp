package mainProgram;

import java.util.ArrayList;
import java.util.List;

public class inMemoryTransactionRepository implements TransactionRepository {
    private final List<Transaction> transactions = new ArrayList<>();

    @Override
    public void save(List<Transaction> transactions) {
        transactions.clear();
        transactions.addAll(transactions);
    }

    @Override
    public List<Transaction> load() {
        return new ArrayList<>(transactions);
    }
}
