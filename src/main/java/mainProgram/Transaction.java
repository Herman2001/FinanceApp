package mainProgram;
import java.time.LocalDate;
import java.time.temporal.WeekFields;

public class Transaction {
    String description;
    double amount;
    LocalDate date;
    int week;

    public Transaction(String description, double amount, LocalDate date) {
        this.description = description;
        this.amount = amount;
        this.date = date;

        WeekFields weekfields = WeekFields.ISO;
        this.week = date.get(weekfields.weekOfWeekBasedYear());

    }

    @Override
    public String toString() {
        return date + " | week: " + week + " | " + description + " | " + amount + "kr";
    }

    public String toCSV() {
        return date + "," + description + "," + amount;
    }

}
