package Controllers;

import Facades.SessionFacade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    public void handleRegister(ActionEvent event) {
        String user = usernameField.getText();
        String pass = passwordField.getText();
        String mail = emailField.getText();

        if (user.isEmpty() || pass.isEmpty() || mail.isEmpty()) {
            errorLabel.setText("Veuillez remplir tous les champs.");
            return;
        }

        boolean success = SessionFacade.getInstance().register(user, pass, mail);

        if (success) {
            errorLabel.setStyle("-fx-text-fill: green;");
            errorLabel.setText("Compte créé ! Redirection...");
            goBackToLogin(event);
        } else {
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText("Erreur : Ce pseudo est déjà pris.");
        }
    }

    @FXML
    public void goBackToLogin(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/main/login-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}