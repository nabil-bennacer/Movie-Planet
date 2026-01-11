package Controllers;

import BuisnessClasses.PaymentInfo;
import Facades.SessionFacade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class PaymentController {

    private SessionFacade sessionFacade;

    @FXML private ComboBox<String> paymentMethodBox;
    @FXML private TextField cardNumberField;
    @FXML private TextField expiryField;
    @FXML private TextField cvvField;
    @FXML private Label errorLabel;

    @FXML
    public void initialize() {
        this.sessionFacade = SessionFacade.getInstance();
        
        // Initialiser la ComboBox
        paymentMethodBox.getItems().addAll("Carte Bancaire", "PayPal");
        paymentMethodBox.getSelectionModel().selectFirst();
    }

    @FXML
    public void onConfirmPayment(ActionEvent event) {
        String method = paymentMethodBox.getValue();
        String card = cardNumberField.getText();
        String expiry = expiryField.getText();
        String cvv = cvvField.getText();

        if (card.isEmpty() || expiry.isEmpty() || cvv.isEmpty()) {
            showError("Veuillez remplir tous les champs.");
            return;
        }

        PaymentInfo info = new PaymentInfo(method, card, cvv, expiry);
        
        boolean success = sessionFacade.processPayment(info);

        if (success) {
            // Succès : On redirige vers le Dashboard
            System.out.println("Paiement réussi !");
            navigateTo("/main/dashboard-view.fxml", "Tableau de bord", event);
        } else {
            showError("Le paiement a été refusé ou le panier est vide.");
        }
    }

    @FXML
    public void onCancelPayment(ActionEvent event) {
        // Retour au panier
        navigateTo("/main/basket-view.fxml", "Mon Panier", event);
    }

    private void showError(String msg) {
        errorLabel.setText(msg);
        errorLabel.setStyle("-fx-text-fill: red;");
    }

    private void navigateTo(String fxmlPath, String title, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(loader.load(), 800, 600);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title + " - MoviePlanet");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}