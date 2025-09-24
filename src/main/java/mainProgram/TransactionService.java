package mainProgram;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class TransactionService {
    private final TransactionManager manager;
    private final Scanner scanner;

    public TransactionService(TransactionManager manager, Scanner scanner) {
        this.manager = manager;
        this.scanner = scanner;
    }

    public void addTransaction() {
        System.out.println("Beskrivning: ");
        System.out.print("> ");
        String description = scanner.nextLine();

        System.out.println("Belopp: ");
        System.out.print("> ");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Belopp måste vara ett tal.");
            return;
        }

        System.out.println("Datum (YYYY-MM-DD) lämna tomt för att använda dagens datum: ");
        System.out.print("> ");
        String dateString = scanner.nextLine();
        LocalDate date;
        if (dateString.isEmpty()) {
            date = LocalDate.now();
        } else {
            try {
                date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (DateTimeParseException e) {
                System.out.println("Felaktigt datum, använder dagens datum istället.");
                date = LocalDate.now();
            }
        }

        manager.addTransaction(new Transaction(description, amount, date));
        manager.saveAll();
        System.out.println("Transaktion tillagd!");
    }

    public void removeTransaction() {
        List<Transaction> all = manager.getAllTransactions();
        if (all.isEmpty()) {
            System.out.println("Inga transaktioner hittades.");
            return;
        }

        for (int i = 0; i < all.size(); i++) {
            System.out.println(i + ": " + all.get(i));
        }
        System.out.print("Skriv in numret på transaktionen du vill ta bort: ");
        System.out.print("> ");

        try {
            int index = Integer.parseInt(scanner.nextLine());
            if (manager.removeTransaction(index)) {
                System.out.println("Transaktion" + index + " bortagen!");
            } else  {
                System.out.println("Ogiltligt index.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ogiltligt index, du måste skriva in en av ovanstående siffror.");
        }
    }

    public void ListAllTransactions() {
        List<Transaction> all = manager.getAllTransactions();
        if (all.isEmpty()) {
            System.out.println("Inga transaktioner hittades.");
            return;
        } else {
            all.forEach(System.out::println);
        }
    }

    public void showBalance() {
        List<Transaction> all = manager.getAllTransactions();
        if (all.isEmpty()) {
            System.out.println("Inga transaktioner hittades.");
        } else {
            double income = manager.getIncomeSum(all);
            double spent = manager.getSpentSum(all);
            double balance = income - spent;
            System.out.println("Balance: " + balance + "kr");
        }
    }

    public void showTransactionsForPeriod() {
        String yearInput = null;
        YearMonth monthInput = null;
        String weekInput = null;
        LocalDate dayInput = null;

        List<Transaction> all = manager.getAllTransactions();
        if (all.isEmpty()) {
            System.out.println("Inga transaktioner hittades.");
            return;
        }

        System.out.println("Välj period: 1->År, 2->Månad, 3->Vecka, 4->Dag");
        System.out.print("> ");
        int period;

        try {
            period = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Ogiltligt val, skriv endast in siffror.");
            return;
        }

        try {
            switch (period) {
                case 1 -> {
                    System.out.print("Skriv in år (YYYY): ");
                    yearInput = scanner.nextLine();
                }
                case 2 -> {
                    System.out.print("Skriv in månad (YYYY-MM): ");
                    monthInput = YearMonth.parse(scanner.nextLine());
                }
                case 3 -> {
                    System.out.print("Skriv in vecka (YYYY-WW): ");
                    weekInput = scanner.nextLine();
                }
                case 4 -> {
                    System.out.print("Skriv in dag (YYYY-MM-DD): ");
                    dayInput = LocalDate.parse(scanner.nextLine());
                }
                default -> {
                    System.out.println("Ogiltigt val.");
                    return;
                }
            }
        } catch (Exception e) {
            System.out.println("Felaktigt format, försök igen.\n");
            return;
        }

        List<Transaction> filtered = manager.filterByPeriod(period, yearInput, monthInput, weekInput, dayInput);

        if (filtered.isEmpty()) {
            System.out.println("Inga transaktioner hittades.");
            return;
        } else {
            filtered.forEach(System.out::println);
            double income = manager.getIncomeSum(filtered);
            double spent = manager.getSpentSum(filtered);
            double balance = income - spent;
            System.out.println("Inkomst: "  + income + " kr");
            System.out.println("Spenderat: " + spent + " kr");
            System.out.println("Resultat: " + balance + " kr");
        }
    }
}
