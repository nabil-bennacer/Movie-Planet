package Controllers;

import java.io.IOException;

import Facades.SessionFacade;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
            
            // Navigation vers le Dashboard (pas directement vers Provider)
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/dashboard-view.fxml"));
                Scene scene = new Scene(loader.load(), 800, 600);
                
                // Récupérer la fenêtre actuelle et changer de scène
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Tableau de bord - MoviePlanet");
                stage.show();
            } catch (IOException e) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Erreur de navigation: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Identifiants incorrects.");
        }
    }
}