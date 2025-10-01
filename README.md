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
- JavaFX
- Maven
- JUnit 5 (för tester)

---

## Förbättrnigar / TODO
- Mer avancerat UI för förbättrad användarupplevelse
- Della upp ansvaraet i TransactionController bättre så att inte allt ligger i samma fil. På samma sätt som jag har gjort i terminal versionen av programmet.
- Vet inte om jag har helt rätt uppdelning med mina filer/mappar så att allt ligger på rätt ställe, men allt funkar iallafall :D
- Spara data i databas istället för CSV-fil. Kanske overkill för detta projekt, men koden är 
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

