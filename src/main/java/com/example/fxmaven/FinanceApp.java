package com.example.fxmaven;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class FinanceApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FinanceApp.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 900);

        scene.getStylesheets().add(Objects.requireNonNull(FinanceApp.class.getResource("style.css")).toExternalForm());
        stage.setTitle("FinanceApp");
        stage.setScene(scene);

        TransactionController controller = fxmlLoader.getController();

        stage.setOnCloseRequest(e -> controller.saveDataToFile());
        stage.show();
    }
}
