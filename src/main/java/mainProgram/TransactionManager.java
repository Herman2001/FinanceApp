package mainProgram;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class TransactionManager {
    private final List<Transaction> transactions = new ArrayList<>();
    private final TransactionRepository repository;

    public double getBalance() {
        return balance;
    }

    double balance;

    public TransactionManager(TransactionRepository repository) {
        this.repository = repository;
        this.transactions.addAll(repository.load());
        this.balance = transactions.stream().mapToDouble(Transaction::getAmount).sum();
    }

    public void saveAll() {
        repository.save(transactions);
    }

    public void addTransaction(Transaction t) {
        transactions.add(t);
        balance += t.getAmount();
    }

    public void updateTransaction(int index, int whatToUpdate, String newDesc, double newAmount, LocalDate newDate ) {
        if (index < 0 || index >= transactions.size()) return;
        Transaction t = transactions.get(index);
        switch (whatToUpdate) {
            case 1 -> t.setDescription(newDesc);
            case 2 -> t.setAmount(newAmount);
            case 3 -> t.setDate(newDate);
        }
        recalcBalance();
    }

    public void updateTransactionForUi(int index, String newDesc, double newAmount, LocalDate newDate ) {
        if (index < 0 || index >= transactions.size()) return;
        Transaction t = transactions.get(index);
        if (newDesc != null) {
            t.setDescription(newDesc);
        }
        if (newAmount >= 0.0) {
            t.setAmount(newAmount);
        }
        if (newDate != null) {
            t.setDate(newDate);
        }
        recalcBalance();
    }

    public boolean removeTransaction(int index) {
        if (index >= 0 && index < transactions.size()) {
            balance -= transactions.get(index).getAmount();
            transactions.remove(index);
            return true;
        }
        return false;
    }

    public List<Transaction> filterByPeriod(int period, String yearInput, YearMonth monthInput, String WeekInput, LocalDate DayInput) {
        List<Transaction> filteredTransactions = new ArrayList<>();
        for (Transaction t : transactions) {
            boolean match = false;
            switch (period) {
                case 1:
                    match = t.getDate().getYear() == Integer.parseInt(yearInput);
                    break;
                case 2:
                    match = YearMonth.from(t.getDate()).equals(monthInput);
                    break;
                case 3:
                    String[]parts = WeekInput.split("-");
                    int inputYear = Integer.parseInt(parts[0]);
                    int inputWeek = Integer.parseInt(parts[1]);

                    WeekFields weekFields = WeekFields.ISO;
                    int transactionWeek = t.getWeek();
                    int transactionYear = t.getDate().get(weekFields.weekBasedYear());

                    match = (transactionYear == inputYear) && (transactionWeek == inputWeek);
                    break;
                case 4:
                    match = t.getDate().equals(DayInput);
                    break;
            }
            if (match) {
                filteredTransactions.add(t);
            }
        }
        return filteredTransactions;
    }

    public List<Transaction> getAllTransactions() {
        // För att ej råka ändra i "orginal" listan.
        return new ArrayList<>(transactions);
    }

    public double getIncomeSum(List<Transaction> transactions) {
        return transactions.stream().filter(t -> t.getAmount() > 0).mapToDouble(Transaction::getAmount).sum();
    }

    public double getSpentSum(List<Transaction> transactions) {
        return transactions.stream().filter(t -> t.getAmount() < 0).mapToDouble(Transaction::getAmount).sum();
    }

    public void recalcBalance() {
        balance = transactions.stream().mapToDouble(Transaction::getAmount).sum();
    }

}

