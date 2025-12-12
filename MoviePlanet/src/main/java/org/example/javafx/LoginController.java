package org.example.javafx;

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
            // TODO: Code pour changer de scène ici
        } else {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Identifiants incorrects.");
        }
    }
}