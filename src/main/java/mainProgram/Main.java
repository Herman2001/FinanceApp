package mainProgram;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TransactionRepository repo = new CsvTransactionRepository("transactions.csv");
        TransactionManager manager = new TransactionManager(repo);
        TransactionService service = new TransactionService(manager, scanner);
        boolean running = true;
        System.out.println("Välkommen till FinanceApp.");

        while (running) {
            System.out.println("\n1. Lägg till en inkomst/utgift.");
            System.out.println("2. Radera en transaktion.");
            System.out.println("3. Lista alla transaktioner.");
            System.out.println("4. Visa transaktioner för en viss period.");
            System.out.println("5. Visa saldo.");
            System.out.println("6. Avsluta programmet.");
            System.out.print("> ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Ogiltligt val");
                continue;
            }


            switch (choice) {
                case 1 -> service.addTransaction();
                case 2 -> service.removeTransaction();
                case 3 -> service.ListAllTransactions();
                case 4 -> service.showTransactionsForPeriod();
                case 5 -> service.showBalance();
                case 6 -> running = false;
                default -> System.out.println("Ogiltligt val.");
            }
        }
        manager.saveAll();
        System.out.println("Programmet avslutas...");
    }
}
