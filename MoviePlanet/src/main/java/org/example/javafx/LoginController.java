package org.example.javafx;
import org.example.javafx.SessionFacade;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    private SessionFacade sessionFacade;

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

        boolean bool = sessionFacade.login(user, pass);

        if (bool) {
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Connexion réussie !");
            // Ici, vous changeriez de scène vers l'application principale
        } else {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Identifiants incorrects.");
        }
    }
}