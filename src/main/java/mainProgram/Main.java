package mainProgram;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TransactionManager manager = new TransactionManager();
        manager.loadFromFile();

        boolean running = true;
        System.out.println("Välkommen till FinanceApp.");

        while (running) {
            System.out.println("\n1. Lägg till en inkomst/utgift.");
            System.out.println("2. Radera en transaktion.");
            System.out.println("3. Lista alla transaktioner.");
            System.out.println("4. Visa transaktioner för en viss period.");
            System.out.println("5. Visa saldo.");
            System.out.println("6. Avsluta programmet.");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> addTransaction(scanner, manager);
                case 2 -> removeTransaction(scanner, manager);
                case 3 -> listTransactions(manager);
                case 4 -> showTransactionsForPeriod(scanner, manager);
                case 5 -> showBalance(manager);
                case 6 -> running = false;
                default -> System.out.printf("Ogiltligt val.");
            }
        }
        manager.saveToFile();
        System.out.println("Programmet avslutas...");
    }

    private static void addTransaction(Scanner scanner, TransactionManager manager) {
        System.out.print("Beskrivning: ");
        String desc = scanner.nextLine();
        System.out.print("Belopp: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Datum (YYYY-MM-DD, lämna tomt för idag): ");
        String dateStr = scanner.nextLine();

        LocalDate date;
        if (dateStr.isEmpty()) date = LocalDate.now();
        else {
            try {
                date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException e) {
                System.out.println("Felaktigt datumformat, använder dagens datum.");
                date = LocalDate.now();
            }
        }

        manager.addTransaction(new Transaction(desc, amount, date));
        System.out.println("Transaktion tillagd!\n");
    }

    private static void removeTransaction(Scanner scanner, TransactionManager manager) {
        List<Transaction> all = manager.getAllTransactions();
        if (all.isEmpty()) {
            System.out.println("Inga transaktioner hittades.\n");
            return;
        }

        for (int i = 0; i < all.size(); i++) {
            System.out.println(i + ": " + all.get(i));
        }
        System.out.print("Skriv in numret på transaktionen du vill ta bort: ");

        int index = scanner.nextInt();
        scanner.nextLine();

        if (manager.removeTransaction(index)) {
            System.out.println("Transaktion borttagen!\n");
        } else {
            System.out.println("Ogiltigt index.\n");
        }
    }

    private static void listTransactions(TransactionManager manager) {
        List<Transaction> all = manager.getAllTransactions();
        if (all.isEmpty()) {
            System.out.println("Inga transaktioner hittades.\n");
        }
        else {
            all.forEach(System.out::println);
        }
    }

    private static void showBalance(TransactionManager manager) {
        List <Transaction> all = manager.getAllTransactions();
        if (all.isEmpty()) {
            System.out.println("Inga transaktioner hittades.");
        } else {
            double income = manager.getIncomeSum(all);
            double spent = manager.getSpentSum(all);
            double balance = income - spent;
            System.out.println("Saldo: " + balance + "kr");
        }
    }

    private static void showTransactionsForPeriod(Scanner scanner, TransactionManager manager) {
        if (manager.getAllTransactions().isEmpty()) {
            System.out.println("Inga transaktioner hittades.\n");
            return;
        }

        System.out.println("Välj period: 1->År, 2->Månad, 3->Vecka, 4->Dag");
        int period = scanner.nextInt();
        scanner.nextLine();


        // TODO: Kanske ska lägga in så att man inte skickar in null värden i filterByPeriod
        String yearInput = null;
        YearMonth monthInput = null;
        String weekInput = null;
        LocalDate dayInput = null;

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
            System.out.println("Inga transaktioner hittades för vald period.\n");
        } else {
            for (int i = 0; i < filtered.size(); i++) {
                System.out.println(filtered.get(i));
            }
            filtered.forEach(System.out::println);
            double incomeSum = manager.getIncomeSum(filtered);
            double spentSum = manager.getSpentSum(filtered);
            System.out.println("\nTotalt inkomst: " + incomeSum + " kr");
            System.out.println("Totalt utgifter: " + spentSum + " kr");
            System.out.println("Resultat: " + (incomeSum + spentSum) + " kr");
        }
    }
}
