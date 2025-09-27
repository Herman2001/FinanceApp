package com.example.fxmaven;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mainProgram.CsvTransactionRepository;
import mainProgram.Transaction;
import mainProgram.TransactionManager;
import mainProgram.TransactionRepository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.List;

public class TransactionController {

    @FXML
    private Label balanceLabel;

    @FXML
    private Button addButton, removeButton, updateButton, listButton, filterButton;

    private final TransactionRepository repo = new CsvTransactionRepository("transactions.csv");
    private final TransactionManager manager = new TransactionManager(repo);

    @FXML
    private void initialize() {
        updateBalance();

        addButton.setOnAction(e -> handleAdd());
        removeButton.setOnAction(e -> handleRemove());
        updateButton.setOnAction(e -> handleUpdate());
        listButton.setOnAction(e -> handleList());
        filterButton.setOnAction(e -> handleFilter());
    }

    private void updateBalance() {
        double income = manager.getIncomeSum(manager.getAllTransactions());
        double spent = manager.getSpentSum(manager.getAllTransactions());
        double total = income + spent;
        balanceLabel.setText("Saldo: " + total + " kr");
    }

    private void handleAdd() {
        Stage stage = new Stage();
        stage.setTitle("Lägg till transaktion");

        Label descLabel = new Label("Beskrivning:");
        TextField descField = new TextField();

        Label amountLabel = new Label("Belopp:");
        TextField amountField = new TextField();

        Label dateLabel = new Label("Datum (YYYY-MM-DD, lämna tomt för idag):");
        TextField dateField = new TextField();

        Button addButton = new Button("Lägg till ✅");
        addButton.setOnAction(event -> {
            String desc = descField.getText();
            double amount;
            try {
                amount = Double.parseDouble(amountField.getText());
            } catch (NumberFormatException e) {
                showAlert("Error ", "Belopp måste vara ett tal!");
                return;
            }

            LocalDate date;
            if (dateField.getText().isEmpty()) {
                date = LocalDate.now();
            } else {
                try {
                    date = LocalDate.parse(dateField.getText());
                } catch (DateTimeParseException e) {
                    showAlert("Error ", "Felaktigt datumformat!");
                    return;
                }
            }

            Transaction t = new Transaction(desc, amount, date);
            manager.addTransaction(t);
            updateBalance();
            stage.close();
        });

        VBox root = new VBox(10, descLabel, descField, amountLabel, amountField, dateLabel, dateField, addButton);
        root.setPadding(new Insets(10));
        Scene scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    private void handleRemove() {
        Stage stage = new Stage();
        stage.setTitle("Ta bort transaktion");

        ListView<Transaction> listView = new ListView<>();
        listView.getItems().addAll(manager.getAllTransactions());

        listView.setCellFactory(lv -> new ListCell<Transaction>() {
            private final Label label = new Label();
            private final Button deleteButton = new Button("❌");
            private final HBox hbox = new HBox(10, label, deleteButton);

            {
                deleteButton.setOnAction(event -> {
                    Transaction item = getItem();
                    if (item != null) {
                        manager.removeTransaction(getIndex());
                        listView.getItems().remove(item);
                        updateBalance();
                    }
                });
            }

            @Override
            protected void updateItem(Transaction item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    label.setText(item.toString());
                    setGraphic(hbox);
                }
            }
        });

        VBox root = new VBox(10, listView);
        root.setPadding(new Insets(10));
        Scene scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        stage.show();
    }

    private void handleUpdate() {
        Stage stage = new Stage();
        stage.setTitle("Uppdatera transaktion");

        ListView<Transaction> listView = new ListView<>();
        listView.getItems().addAll(manager.getAllTransactions());

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        Label instructions = new Label("Välj transaktion att uppdatera:");
        root.getChildren().addAll(instructions, listView);
        
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection == null) return;

            root.getChildren().removeIf(node -> node instanceof VBox);

            TextField descField = new TextField(newSelection.getDescription());
            TextField amountField = new TextField(String.valueOf(newSelection.getAmount()));
            TextField dateField = new TextField(newSelection.getDate().toString());

            Button updateBtn = new Button("Uppdatera ✅");
            updateBtn.setOnAction(event -> {
                String desc = descField.getText();
                double amount;
                LocalDate date;

                try {
                    amount = Double.parseDouble(amountField.getText());
                } catch (NumberFormatException e) {
                    showAlert("Fel", "Belopp måste vara ett tal!");
                    return;
                }

                try {
                    if (dateField.getText().isEmpty()) {
                        date = LocalDate.now();
                    } else {
                        date = LocalDate.parse(dateField.getText());
                    }
                } catch (DateTimeParseException e) {
                    showAlert("Fel", "Felaktigt datumformat!");
                    return;
                }

                int index = listView.getSelectionModel().getSelectedIndex();
                manager.updateTransactionForUi(index, desc, amount, date);
                listView.getItems().set(index, new Transaction(desc, amount, date));
                manager.saveAll();
                updateBalance();
                showAlert("Info", "Transaktion uppdaterad!");
            });

            VBox form = new VBox(5, new Label("Beskrivning:"), descField,
                    new Label("Belopp:"), amountField,
                    new Label("Datum (YYYY-MM-DD):"), dateField,
                    updateBtn);
            form.setPadding(new Insets(10));
            root.getChildren().add(form);
        });

        Scene scene = new Scene(root, 500, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void handleList() {
        Stage stage = new Stage();
        stage.setTitle("Alla transaktioner.");

        ListView<Transaction> listView = new ListView<>();
        listView.getItems().addAll(manager.getAllTransactions());

        Scene scene = new Scene(listView, 400, 300);
        stage.setScene(scene);
        stage.show();
    }

    private void handleFilter() {
        Stage stage = new Stage();
        stage.setTitle("Filtrerade transaktioner");

        ComboBox<String> periodBox = new ComboBox<>();
        periodBox.getItems().addAll("År", "Månad", "Vecka", "Dag");
        periodBox.setValue("År");

        TextField inputField = new TextField();
        inputField.setPromptText("YYYY");
        periodBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (periodBox.getValue().equals("År")) {
                inputField.setPromptText("YYYY");
            } else if (periodBox.getValue().equals("Månad")) {
                inputField.setPromptText("YYYY-MM");
            } else if (periodBox.getValue().equals("Vecka")) {
                inputField.setPromptText("YYYY-WW");
            } else if (periodBox.getValue().equals("Dag")) {
                inputField.setPromptText("YYYY-MM-DD");
            }
        });

        ListView<Transaction> resultList = new ListView<>();

        Button filterButton = new Button("Filtrera");
        filterButton.setOnAction(event -> {
            String selectedPeriod = periodBox.getValue();
            String input = inputField.getText().trim();
            List<Transaction> filtered = List.of();

            try {
                switch (selectedPeriod) {
                    case "År" -> {
                        filtered = manager.filterByPeriod(1, input, null, null, null);
                    }
                    case "Månad" -> {
                        filtered = manager.filterByPeriod(2,null, YearMonth.parse(input), null, null);
                    }
                    case "Vecka" -> {
                        filtered = manager.filterByPeriod(3, null, null, input, null);
                    }
                    case "Dag" -> {
                        filtered = manager.filterByPeriod(4, null, null, null, LocalDate.parse(input));
                    }
                }
            } catch (Exception ex) {
                System.out.println("Felaktigt format" + ex.getMessage());
            }

            resultList.getItems().setAll(filtered);
        });

        VBox root = new VBox(10, periodBox, inputField, filterButton, resultList);
        Scene scene = new Scene(root, 400, 400);
        stage.setScene(scene);
        stage.show();
    }

    public void saveDataToFile() {
        manager.saveAll();
    }
}
