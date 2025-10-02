package mainProgram;
import java.time.LocalDate;
import java.time.temporal.WeekFields;

public class Transaction {
    private String description;
    private double amount;
    private LocalDate date;
    private int  week;

    public Transaction(String description, double amount, LocalDate date) {
        this.description = description;
        this.amount = amount;
        this.date = date;

        WeekFields weekfields = WeekFields.ISO;
        this.week = date.get(weekfields.weekOfWeekBasedYear());

    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getWeek() {
        return week;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDate(LocalDate date) {
        this.date = date;
        WeekFields wf = WeekFields.ISO;
        this.week = date.get(wf.weekOfWeekBasedYear());

    }


    @Override
    public String toString() {
        return date + " | week: " + week + " | " + description + " | " + amount + "kr";
    }

    public String toCSV() {
        return date + "," + description + "," + amount;
    }
}
