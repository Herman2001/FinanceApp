# Finansprogram (FXmaven)

Ett java program för att hantera transaktioner.  
Skrivet så att det går att köra både i terminal och ett UI med hjälp av JavaFX.
Tester skrivna med Junit 5 för att testa "TransactionManager".

---

## Funktioner
- Lägg till transaktioner med belopp, datum och beskrivning
- Radera transaktioner
- Redigera befintlig transaktion
- Filtrera transaktioner baserat på vecka
- Se saldo och summeringar

---

## Teknik
- Java 21
- Java21 21
- Maven
- JUnit 5 (för tester)

---

## Förbättrnigar
- Mer avancerat UI för förbättrad användarupplevelse
- Spara data i databas istället för CSV-fil. Kanske overkill för detta projekt kanske, men koden är 
  "förberedd" för det med ett interface till save och load funktioner. 
- Går säkert att städa upp koden på fler ställen, med inbyggda funktioner osv...
- Flera tester på andra delar i koden, just nu testas bara TransactionManager. (vilket jag tycker är viktigast)
---

## Krav
- Java 21 eller senare
- Maven 3.9+

---
## Kör projektet från projektets rotmapp

### Kör tester
    mvn test


### UI med JavaFX
    mvn javafx:run

### Terminal applikationen
    mvn clean compile exec:java -Dexec.mainClass="mainProgram.Main"

