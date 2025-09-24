package mainProgram;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CsvTransactionRepository implements TransactionRepository {
    private final String fileName;

    public CsvTransactionRepository(String fileName) {
        this.fileName = fileName;
    }
    @Override
    public void save(List<Transaction> transactions) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (Transaction transaction : transactions) {
                writer.println(transaction.toCSV());
            }
            System.out.println("Transaktion sparad.");
        } catch (IOException e) {
            System.out.println("Kunde inte spara filen " + e.getMessage());
        }
    }

    @Override
    public List<Transaction> load() {
        System.out.println("Laddar filer från " + fileName);
        List<Transaction> transactions = new ArrayList<>();
        File file = new File(fileName);
        if (!file.exists()) {
            return transactions;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while((line = br.readLine()) != null) {
                String[] parts = line.split(",", 3);
                if (parts.length == 3) {
                    LocalDate date = LocalDate.parse(parts[0]);
                    String description = parts[1];
                    double amount = Double.parseDouble(parts[2]);
                    Transaction transaction = new Transaction(description, amount, date);
                    transactions.add(transaction);
                    //För debug
                    System.out.println("----- KONTROLL -----");
                    System.out.println(transaction + " laddad från " + file.getName() + "\n");
                }
            }
        } catch (IOException e) {
            System.out.println("Kunde inte spara filen " + e.getMessage());
        }
        return transactions;
    }
}
