# Finansprogram (FXmaven)

Ett Java-program för att hantera personliga transaktioner och ekonomi.

Kan köras både i terminal och med grafiskt gränssnitt (JavaFX).

Tester skrivna med Juni5.

![App Screenshot](docs/appDemo.png)

---

## ✨ Funktioner
➕ Lägg till transaktioner med belopp, datum och beskrivning

❌ Radera transaktioner

✏️ Redigera befintliga transaktioner

🔍 Filtrera transaktioner baserat på år, månad, vecka eller dag

💰 Se aktuellt saldo och sammanfattningar

🎨 Ljust och mörkt tema i UI:t

---

## 🛠️ Teknik
-  Java 21
-  JavaFX - För grafiskt gränssnitt
-  Maven - Byggverktyg och dependency management
-  JUnit 5 - För enhetstester
-  CSV - För datalagring
---

## 🔮 Förbättringar / TODO
- Mer avancerat UI för förbättrad användarupplevelse
- Della upp ansvaraet i TransactionController bättre så att inte allt ligger i samma fil. På samma sätt som jag har gjort i terminal versionen av programmet.
- Spara data i databas istället för CSV-fil. Kanske overkill för detta projekt, men koden är 
  "förberedd" för det med ett interface till save och load funktioner.
- Flera tester på andra delar i koden, just nu testas bara TransactionManager. (vilket jag tycker är viktigast)
---

## Krav
- Java 21 eller senare
- Maven 3.9+

---
## 🚀 Kör projektet
#### Från projektets rotmapp:

### Kör tester
    mvn test


### UI med JavaFX
    mvn javafx:run

### Terminal applikationen
    mvn clean compile exec:java -Dexec.mainClass="mainProgram.Main"

### 👨‍💻 
Herman Bengtsson

