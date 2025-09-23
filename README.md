# Finansprogram (FXmaven)

Ett java program för att hantera transaktioner.  
Skrivet så att det går att köra både i terminal och ett UI med hjälp av JavaFX.
Tester skrivna med Junit 5 för att testa "TransactionManager".

---

## Funktioner
- Lägg till transaktioner med belopp, datum och beskrivning
- Radera transaktioner
- Filtrera transaktioner baserat på vecka
- Se saldo och summeringar

---

## Teknik
- **Java 21**
- **JavaFX 21**
- **Maven**
- **JUnit 5** (för tester)

---

## Krav
- Java 21 eller senare
- Maven 3.9+

---
## För att köra projektet
### Tester:
    mvn test
### UI med JavaFX:
    mvn javafx:run
### Terminal applikationen:
    mvn clean compile exec:java -Dexec.mainClass="mainProgram.Main"

