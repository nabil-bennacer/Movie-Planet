package Controllers;

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

    /**
     * Méthode appelée automatiquement par JavaFX après le chargement du FXML.
     * C'est ici qu'on initialise les dépendances.
     */
    @FXML
    public void initialize() {
        // On récupère l'instance unique de SessionFacade
        this.sessionFacade = SessionFacade.getInstance();
    }

    @FXML
    protected void onLoginButtonClick() {
        if (sessionFacade == null) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Erreur : Facade non initialisée.");
            return;
        }

        String user = usernameField.getText();
        String pass = passwordField.getText();

        boolean bool = sessionFacade.login(user, pass);

        if (bool) {
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Connexion réussie !");

            try {

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/userStore-view.fxml"));
                Parent root = fxmlLoader.load();

                Stage stage = (Stage) messageLabel.getScene().getWindow();

                Scene scene = new Scene(root, 800, 600);
                stage.setTitle("Movie Planet - Boutique");
                stage.setScene(scene);
                stage.centerOnScreen();
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Erreur lors du chargement de la boutique.");
            }

        } else {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Identifiants incorrects.");
        }
    }
}