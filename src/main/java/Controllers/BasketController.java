package Controllers;

import BuisnessClasses.CartItem;
import Facades.SessionFacade;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.util.Callback;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class BasketController {

    private SessionFacade sessionFacade;

    @FXML
    private TableView<CartItem> cartTable;
    @FXML
    private TableColumn<CartItem, String> productColumn;
    @FXML
    private TableColumn<CartItem, Double> priceColumn;
    @FXML
    private TableColumn<CartItem, Integer> quantityColumn;
    @FXML
    private TableColumn<CartItem, Double> totalItemColumn; // Prix total ligne
    @FXML
    private Label totalLabel;

    @FXML
    public void initialize() {
        this.sessionFacade = SessionFacade.getInstance();
        
        // Configuration des colonnes
        productColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        
        // Pour afficher le total de la ligne (calculé)
        totalItemColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getTotalPrice())
        );

        onLoadBasket();
    }

    public void onLoadBasket() {
        if (sessionFacade.getCurrentUser() == null) {
            totalLabel.setText("Veuillez vous connecter.");
            return;
        }

        List<CartItem> items = sessionFacade.getBasketItems();
        displayCart(items);
    }

    public void displayCart(List<CartItem> items) {
        ObservableList<CartItem> observableItems = FXCollections.observableArrayList(items);
        cartTable.setItems(observableItems);
        
        double total = sessionFacade.getBasketTotal(items);
        updateTotal(total);
    }

    public void updateTotal(double amount) {
        totalLabel.setText(String.format("Total: %.2f €", amount));
    }

  
    @FXML
    public void onRemoveItem() {
        CartItem selectedItem = cartTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {

            sessionFacade.removeBasketItem(selectedItem);
           
            onLoadBasket();
        }
    }


    @FXML
    public void onIncreaseQuantity() {
        CartItem selectedItem = cartTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            sessionFacade.updateBasketItemQuantity(selectedItem, selectedItem.getQuantity() + 1);
            onLoadBasket();
        }
    }

    @FXML
    public void onProceedToPayment() {
        // Vérifier que le panier n'est pas vide avant de procéder au paiement
        if (cartTable.getItems().isEmpty()) {
            // Afficher un message d'erreur si le panier est vide
            totalLabel.setStyle("-fx-text-fill: red;");
            totalLabel.setText("Votre panier est vide !");
            return;
        }
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/payment-view.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            Stage stage = (Stage) cartTable.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Paiement - MoviePlanet");
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur de navigation vers le paiement: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    protected void onBackToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/dashboard-view.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            Stage stage = (Stage) cartTable.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Tableau de bord - MoviePlanet");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}