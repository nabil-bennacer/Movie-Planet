package Controllers;

import BuisnessClasses.User;
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

public class LoginController {

    private SessionFacade sessionFacade;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    @FXML
    public void initialize() {
        this.sessionFacade = SessionFacade.getInstance();
    }

    @FXML
    protected void onLoginButtonClick(ActionEvent event) {

        String user = usernameField.getText();
        String pass = passwordField.getText();

        if (user.isEmpty() || pass.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Veuillez remplir tous les champs.");
            return;
        }

        boolean success = sessionFacade.login(user, pass);

        if (success) {
            User currentUser = sessionFacade.getCurrentUser();

            if (currentUser != null && "Admin".equals(currentUser.getRole())) {

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/admin-view.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    messageLabel.setStyle("-fx-text-fill: red;");
                    messageLabel.setText("Erreur de chargement Dashboard Admin.");
                }
            } else {
                messageLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                messageLabel.setText("Connexion réussie !");


                 usernameField.clear();
                 passwordField.clear();
            }

        } else {
            // Echec connexion
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Identifiants incorrects.");
        }
    }

    @FXML
    public void onRegisterLinkClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/register-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Impossible d'ouvrir la page d'inscription.");
        }
    }
}