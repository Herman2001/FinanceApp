/*
    Valde att göra ett inteface för att man ska kunna spara datan på andra sätt t.ex i en
    databas utan att behöva ändra något i de andra filerna.
*/
package mainProgram;

import java.util.List;

public interface TransactionRepository {
    void save(List<Transaction> transactions);
    List<Transaction> load();
}
