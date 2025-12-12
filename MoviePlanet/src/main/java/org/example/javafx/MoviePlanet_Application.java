package org.example.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MoviePlanet_Application extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // On charge le fichier login-view.fxml
        FXMLLoader fxmlLoader = new FXMLLoader(MoviePlanet_Application.class.getResource("login-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}