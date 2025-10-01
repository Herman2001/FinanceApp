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
    // ====================== TRANSAKTIONER ======================

    public void addTransaction() {
        String description = promptForString("Beskrivning: ");
        double amount = promptForDouble("Belopp: ");
        LocalDate date = promptForDateOrToday("Datum (YYYY-MM-DD) lämna tomt för att använda dagens datum: ");

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

        listAllTransactions();

        int index = promptForInt("Skriv in numret på transaktionen du vill ta bort: ", 0 , all.size() - 1);
        if (manager.removeTransaction(index)) {
            manager.saveAll();
            System.out.println("Transaktion " + index + " bortagen!");
        } else {
            System.out.println("Ogiltligt index.");
        }
    }

    public void updateTransaction() {
        List<Transaction> all = manager.getAllTransactions();
        if (all.isEmpty()) {
           System.out.println("Inga transaktioner hittades.");
           return;
        }

        listAllTransactions();

        int indexToUpdate = promptForInt("Skriv in numret på transaktionen du vill ändra: ", 0, all.size() - 1);

        System.out.println("Vad vill du uppdatera? \n 1-> Beskrivning \n2-> Belopp \n3-> Datum");
        int whatToUpdate = promptForInt("Val: ", 1, 3);

        switch (whatToUpdate) {
            case 1 -> {
                String newDesc = promptForString("Ny beskrivning: ");
                manager.updateTransaction(indexToUpdate, whatToUpdate, newDesc, 0.0, null);
            }
            case 2 -> {
                double newAmount = promptForDouble("Nytt belopp: ");
                manager.updateTransaction(indexToUpdate, whatToUpdate, null, newAmount, null);
            }
            case 3 -> {
                LocalDate newDate = promptForDateOrToday("Nytt datum (YYYY-MM-DD) lämna tomt för dagens datum: ");
                manager.updateTransaction(indexToUpdate, whatToUpdate, null, 0.0, newDate);
            }
        }

        manager.saveAll();
        System.out.println("Transaktion uppdaterad!");
    }

    public void listAllTransactions() {
        List<Transaction> all = manager.getAllTransactions();
        if (all.isEmpty()) {
            System.out.println("Inga transaktioner hittades.");
        } else {
            for (int i = 0; i < all.size(); i++) {
                System.out.println(i + ": " + all.get(i));
            }
        }
    }

    // ====================== SALDO ======================

    public void showBalance() {
        List<Transaction> all = manager.getAllTransactions();
        if (all.isEmpty()) {
            System.out.println("Inga transaktioner hittades.");
        } else {
            double income = manager.getIncomeSum(all);
            double spent = manager.getSpentSum(all);
            double balance = income - spent;
            System.out.println("Saldo: " + balance + "kr");
        }
    }

    // ====================== FILTRERA ======================

    public void showTransactionsForPeriod() {
        List<Transaction> all = manager.getAllTransactions();
        if (all.isEmpty()) {
            System.out.println("Inga transaktioner hittades.");
            return;
        }

        System.out.println("Välj period: 1->År, 2->Månad, 3->Vecka, 4->Dag");
        int period = promptForInt("Val: ", 1, 4);

        String yearInput = null;
        YearMonth monthInput = null;
        String weekInput = null;
        LocalDate dayInput = null;


       switch (period) {
           case 1 -> yearInput = promptForString("Skriv in år (YYYY): ");
           case 2 -> monthInput = YearMonth.parse(promptForString("Skriv inb månad (YYYY-MM): "));
           case 3 -> weekInput = promptForString("Skriv in vecka (YYYY-WW): ");
           case 4 -> dayInput = promptForDateOrToday("Skriv in dag (YYYY-MM-DD) lämna tomt för dagens datum: ");
       }

        List<Transaction> filtered = manager.filterByPeriod(period, yearInput, monthInput, weekInput, dayInput);
        if (filtered.isEmpty()) {
            System.out.println("Inga transaktioner hittades.");
            return;
        }

        filtered.forEach(System.out::println);
        double income = manager.getIncomeSum(filtered);
        double spent = manager.getSpentSum(filtered);
        double balance = income - spent;
        System.out.println("Inkomst: "  + income + " kr");
        System.out.println("Spenderat: " + spent + " kr");
        System.out.println("Resultat: " + balance + " kr");
    }

    // ====================== HJÄLPMETODER INPUT ======================

    private int promptForInt(String message, int min, int max) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine();
            try {
                int value = Integer.parseInt(input);
                if (value < min || value > max) {
                    System.out.println("Värdet måste vara mellan " + min + " och " + max + ".");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Fel: " + input + " är inte ett giltligt tal.");
            }
        }
    }

    private double promptForDouble(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine();
            try {
                return Double.parseDouble(input);
            }  catch (NumberFormatException e) {
                System.out.println("Fel: " + input + " är inte ett giltligt tal.");
            }
        }
    }

    private String promptForString(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine();
            if (!input.isBlank()) return input;
            System.out.println("Fel: fältet får inte vara tomt.");
        }
    }

    private LocalDate promptForDateOrToday(String message) {
        System.out.print(message);
        String input = scanner.nextLine();
        if (input.isBlank()) return LocalDate.now();
        try {
            return LocalDate.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            System.out.println("Felaktigt datum, använder dagens datum istället.");
            return LocalDate.now();
        }
    }
}
