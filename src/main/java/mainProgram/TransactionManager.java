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
    //private static final String FILE_NAME = "transactions.csv";
    double balance;

    public TransactionManager(TransactionRepository repository) {
        this.repository = repository;
        this.transactions.addAll(repository.load());
        this.balance = transactions.stream().mapToDouble(t -> t.amount).sum();
    }

    public void saveAll() {
        repository.save(transactions);
    }

    public void addTransaction(Transaction t) {
        transactions.add(t);
        balance += t.amount;
    }

    public boolean removeTransaction(int index) {
        if (index >= 0 && index < transactions.size()) {
            balance -= transactions.get(index).amount;
            transactions.remove(index);
            return true;
        }
        return false;
    }

    public List<Transaction> getAllTransactions() {
        //Kan returnera vanliga listan, men väljer att skapa en ny för att man inte ska kunna göra
        //ändringar i "orginal" listan.
        return new ArrayList<>(transactions);
    }

    public List<Transaction> filterByPeriod(int period, String yearInput, YearMonth monthInput, String WeekInput, LocalDate DayInput) {
        List<Transaction> filteredTransactions = new ArrayList<>();
        for (Transaction t : transactions) {
            boolean match = false;
            switch (period) {
                case 1:
                    match = t.date.getYear() == Integer.parseInt(yearInput);
                    break;
                case 2:
                    match = YearMonth.from(t.date).equals(monthInput);
                    break;
                case 3:
                    String[]parts = WeekInput.split("-");
                    int inputYear = Integer.parseInt(parts[0]);
                    int inputWeek = Integer.parseInt(parts[1]);

                    WeekFields weekFields = WeekFields.ISO;
                    int transactionWeek = t.date.get(weekFields.weekOfWeekBasedYear());
                    int transactionYear = t.date.get(weekFields.weekBasedYear());

                    match = (transactionYear == inputYear) && (transactionWeek == inputWeek);
                    break;
                case 4:
                    match = t.date.equals(DayInput);
                    break;
            }
            if (match) {
                filteredTransactions.add(t);
            }
        }
        return filteredTransactions;
    }

    public double getIncomeSum(List<Transaction> transactions) {
        return transactions.stream().filter(t -> t.amount > 0).mapToDouble(t -> t.amount).sum();
    }

    public double getSpentSum(List<Transaction> transactions) {
        return transactions.stream().filter(t -> t.amount < 0).mapToDouble(t -> t.amount).sum();
    }


}

