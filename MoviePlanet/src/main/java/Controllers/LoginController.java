package Controllers;

import BuisnessClasses.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import Facades.SessionFacade;
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
    protected void onLoginButtonClick() {
        if (sessionFacade == null) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Erreur : Facade non initialisée.");
            return;
        }

        String userAuth = usernameField.getText();
        String passAuth = passwordField.getText();

        boolean success = sessionFacade.login(userAuth, passAuth);

        if (success) {
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Connexion réussie !");

            try {
                User currentUser = sessionFacade.getCurrentUser();
                String fxmlFile;
                String title;

                if (currentUser.isAdmin()) {
                    fxmlFile = "/main/adminStore-view.fxml";
                    title = "Movie Planet - Administration";
                } else {
                    fxmlFile = "/main/userStore-view.fxml";
                    title = "Movie Planet - Boutique";
                }

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
                Parent root = fxmlLoader.load();

                Stage stage = (Stage) messageLabel.getScene().getWindow();
                Scene scene = new Scene(root, 800, 600);
                stage.setTitle(title);
                stage.setScene(scene);
                stage.centerOnScreen();
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Erreur lors du chargement de la page.");
            }

        } else {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Identifiants incorrects.");
        }
    }
}