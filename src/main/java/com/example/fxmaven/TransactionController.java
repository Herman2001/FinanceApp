package com.example.fxmaven;

import mainProgram.CsvTransactionRepository;
import mainProgram.Transaction;
import mainProgram.TransactionManager;
import mainProgram.TransactionRepository;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;

public class TransactionController {

    @FXML
    private CheckBox themeToggle;
    @FXML
    private Label balanceLabel;
    @FXML
    private Button addButton, removeButton, updateButton, listButton, filterButton;

    @FXML
    private StackPane contentArea;

    private final TransactionRepository repo = new CsvTransactionRepository("transactions.csv");
    private final TransactionManager manager = new TransactionManager(repo);

    private VBox mainView;

    @FXML
    private void initialize() {
        updateBalance();

        addButton.setOnAction(e -> showAddView());
        removeButton.setOnAction(e -> showRemoveView());
        updateButton.setOnAction(e -> showUpdateView());
        listButton.setOnAction(e -> showListView());
        filterButton.setOnAction(e -> showFilterView());

        themeToggle.setOnAction(e -> toggleTheme());
        //För att "temat" ska laddas in från början och man inte ska behöva trycka 2 gånger första gången man ändrar
        Platform.runLater(this::toggleTheme);
    }

    // ============== ÄNDRA TEMA ==============

    private void toggleTheme() {
        Scene scene = themeToggle.getScene();
        if (scene != null) {
            scene.getStylesheets().clear();
            if (themeToggle.isSelected()) {
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/example/fxmaven/style.css")).toExternalForm());
            } else {
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/example/fxmaven/light.css")).toExternalForm());
            }
        }
    }

    // ============== ALERTS  ==============

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // ============== VIEWS ==============

    private void switchView(VBox newView) {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(newView);
    }

    private void showMainView() {
        if (mainView == null) {
            mainView = new VBox(10);
            mainView.setPadding(new Insets(20));
            Label welcome = new Label("Välkommen! Använd knapparna ovan för att hantera dina transaktion.");
            mainView.getChildren().add(welcome);
        }
        switchView(mainView);
    }

    // ============== SALDO ==============

    private void updateBalance() {
        double income = manager.getIncomeSum(manager.getAllTransactions());
        double spent = manager.getSpentSum(manager.getAllTransactions());
        double total = income + spent;
        balanceLabel.setText("Saldo: " + total + " kr");
    }

    // ============== TRANSAKTIONER ==============

    private void showAddView() {
        VBox view = new VBox(10);
        view.setPadding(new Insets(20));

        Label title = new Label("Lägg till transaktion");

        Label descLabel = new Label("Beskrivning:");
        TextField descField = new TextField();

        Label amountLabel = new Label("Belopp:");
        TextField amountField = new TextField();

        Label dateLabel = new Label("Datum (YYYY-MM-DD, lämna tomt för idag):");
        TextField dateField = new TextField();

        Button addButton = new Button("Lägg till ✅");
        Button cancelButton = new Button("Avbryt");

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
            showMainView();
        });

        cancelButton.setOnAction(event -> showMainView());

        HBox buttons = new HBox(10, addButton, cancelButton);
        view.getChildren().addAll(title, descLabel, descField, amountLabel, amountField, dateLabel, dateField, buttons);
        switchView(view);
    }


    private void showRemoveView() {
        VBox view = new VBox(10);
        view.setPadding(new Insets(20));

        Label title = new Label("Ta bort transaktion");

        ListView<Transaction> listView = new ListView<>();
        listView.getItems().addAll(manager.getAllTransactions());

        listView.setCellFactory(lv -> new ListCell<>() {
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

        Button backButton = new Button("Tillbaka");
        backButton.setOnAction(event -> showMainView());

        view.getChildren().addAll(title, listView, backButton);
        switchView(view);
    }

    private void showUpdateView() {
        VBox view = new VBox(10);
        view.setPadding(new Insets(20));

        Label title = new Label("Uppdatera transaktion");

        ListView<Transaction> listView = new ListView<>();
        listView.getItems().addAll(manager.getAllTransactions());

        Label instructions = new Label("Välj transaktion att uppdatera:");
        view.getChildren().addAll(title, instructions, listView);

        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection == null) return;

            view.getChildren().removeIf(node -> node instanceof VBox && node != view);

            TextField descField = new TextField(newSelection.getDescription());
            TextField amountField = new TextField(String.valueOf(newSelection.getAmount()));
            TextField dateField = new TextField(newSelection.getDate().toString());

            Button updateButton = new Button("Uppdatera ✅");
            Button cancelButton = new Button("Avbryt");

            updateButton.setOnAction(event -> {
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
                showMainView();
            });

            cancelButton.setOnAction(event -> showMainView());

            HBox buttons = new HBox(10, updateButton, cancelButton);
            VBox form = new VBox(5, new Label("Beskrivning:"), descField,
                    new Label("Belopp:"), amountField,
                    new Label("Datum (YYYY-MM-DD):"), dateField,
                    buttons);
            form.setPadding(new Insets(10));
            view.getChildren().add(form);
        });

       Button backButton = new Button("Tillbaka");
       backButton.setOnAction(event -> showMainView());
       view.getChildren().add(backButton);

       switchView(view);
    }

    private void showListView() {
        VBox view = new VBox(10);
        view.setPadding(new Insets(20));

        Label title = new Label("Alla transaktioner");

        ListView<Transaction> listView = new ListView<>();
        listView.getItems().addAll(manager.getAllTransactions());

        Button backButton = new Button("Tillbaka");
        backButton.setOnAction(event -> showMainView());

        view.getChildren().addAll(title, listView, backButton);
        switchView(view);
    }

    // ============== FILTRERA ==============

    private void showFilterView() {
        VBox view = new VBox(10);
        view.setPadding(new Insets(20));

        Label title = new Label("Filtrerade transaktioner");

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
        TextArea incomeSpentLabel = new TextArea();
        incomeSpentLabel.setEditable(false);
        incomeSpentLabel.setPrefRowCount(3);

        Button filterButton = new Button("Filtrera");
        Button backButton = new Button("Tillbaka");

        filterButton.setOnAction(event -> {
            String selectedPeriod = periodBox.getValue();
            String input = inputField.getText().trim();
            List<Transaction> filtered = List.of();

            try {
                switch (selectedPeriod) {
                    case "År" -> filtered = manager.filterByPeriod(1, input, null, null, null);
                    case "Månad" -> filtered = manager.filterByPeriod(2,null, YearMonth.parse(input), null, null);
                    case "Vecka" -> filtered = manager.filterByPeriod(3, null, null, input, null);
                    case "Dag" -> filtered = manager.filterByPeriod(4, null, null, null, LocalDate.parse(input));
                }
            } catch (Exception ex) {
                System.out.println("Felaktigt format" + ex.getMessage());
            }

            resultList.getItems().setAll(filtered);
            if (!filtered.isEmpty()) {
                double income = manager.getIncomeSum(filtered);
                double spent = manager.getSpentSum(filtered);
                double result = income - spent;
                incomeSpentLabel.setText(
                        "Inkomst: " + income + "kr\n" +
                        "Spenderat: " + spent + "kr\n" +
                        "Resultat: " + result + "kr\n"
                );
            } else {
                incomeSpentLabel.setText("Inga transaktioner hittades.");
            }
        });

        backButton.setOnAction(event -> showFilterView());

        HBox buttons = new HBox(10, filterButton, backButton);
        view.getChildren().addAll(title, periodBox, inputField, buttons, resultList, incomeSpentLabel);
        switchView(view);
    }

    public void saveDataToFile() {
        manager.saveAll();
    }
}