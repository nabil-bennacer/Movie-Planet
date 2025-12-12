package org.example.javafx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    @FXML
    protected void onLoginButtonClick() {
        String user = usernameField.getText();
        String pass = passwordField.getText();

        // Exemple simple de vérification
        if ("admin".equals(user) && "1234".equals(pass)) {
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Connexion réussie !");
            // Ici, vous changeriez de scène vers l'application principale
        } else {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Identifiants incorrects.");
        }
    }
}