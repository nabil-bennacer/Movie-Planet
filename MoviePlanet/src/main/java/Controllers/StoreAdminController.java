package Controllers;

import BuisnessClasses.Article;
import Facades.StoreFacade;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class StoreAdminController {

    @FXML private TableView<Article> articlesTable;
    @FXML private TableColumn<Article, String> colNom;
    @FXML private TableColumn<Article, Double> colPrix;
    @FXML private TableColumn<Article, Integer> colStock;
    @FXML private TableColumn<Article, String> colFilm;

    @FXML private TextField nomField;
    @FXML private TextField prixField;
    @FXML private TextField stockField;
    @FXML private TextField filmField;
    @FXML private TextField imageField;
    @FXML private TextArea descField;
    @FXML private Label messageLabel;

    private StoreFacade storeFacade;
    private Article articleSelectionne;

    public StoreAdminController() {
        this.storeFacade = new StoreFacade();
    }

    @FXML
    public void initialize() {
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colFilm.setCellValueFactory(new PropertyValueFactory<>("filmLie"));

        articlesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                remplirFormulaire(newSelection);
            }
        });

        rafraichirTableau();
    }

    private void rafraichirTableau() {
        articlesTable.setItems(FXCollections.observableArrayList(storeFacade.getAllArticles()));
    }

    private void remplirFormulaire(Article a) {
        this.articleSelectionne = a;
        nomField.setText(a.getNom());
        prixField.setText(String.valueOf(a.getPrix()));
        stockField.setText(String.valueOf(a.getStock()));
        filmField.setText(a.getFilmLie());
        imageField.setText(a.getImageUrl());
        descField.setText(a.getDescription());
    }

    private void viderFormulaire() {
        this.articleSelectionne = null;
        nomField.clear();
        prixField.clear();
        stockField.clear();
        filmField.clear();
        imageField.clear();
        descField.clear();
        articlesTable.getSelectionModel().clearSelection();
    }

    @FXML
    public void onNouveauClick() {
        viderFormulaire();
        messageLabel.setText("Mode : Nouvel article");
        messageLabel.setStyle("-fx-text-fill: blue;");
    }

    @FXML
    public void onValiderClick() {
        try {
            String nom = nomField.getText();
            double prix = Double.parseDouble(prixField.getText());
            int stock = Integer.parseInt(stockField.getText());
            String film = filmField.getText();
            String img = imageField.getText();
            String desc = descField.getText();

            if (articleSelectionne == null) {
                Article newArticle = new Article(0, nom, prix, desc, img, stock, film);
                if (storeFacade.ajouterArticle(newArticle)) {
                    messageLabel.setText("Article ajouté avec succès !");
                    messageLabel.setStyle("-fx-text-fill: green;");
                }
            } else {
                articleSelectionne.setNom(nom);
                articleSelectionne.setPrix(prix);
                articleSelectionne.setStock(stock);
                articleSelectionne.setFilmLie(film);
                articleSelectionne.setImageUrl(img);
                articleSelectionne.setDescription(desc);

                if (storeFacade.modifierArticle(articleSelectionne)) {
                    messageLabel.setText("Article modifié avec succès !");
                    messageLabel.setStyle("-fx-text-fill: green;");
                }
            }
            rafraichirTableau();
            viderFormulaire();

        } catch (NumberFormatException e) {
            messageLabel.setText("Erreur : Vérifiez les champs numériques (Prix, Stock).");
            messageLabel.setStyle("-fx-text-fill: red;");
        } catch (Exception e) {
            messageLabel.setText("Erreur technique : " + e.getMessage());
        }
    }

    @FXML
    public void onSupprimerClick() {
        if (articleSelectionne != null) {
            if (storeFacade.supprimerArticle(articleSelectionne.getId())) {
                messageLabel.setText("Article supprimé.");
                messageLabel.setStyle("-fx-text-fill: orange;");
                rafraichirTableau();
                viderFormulaire();
            } else {
                messageLabel.setText("Erreur lors de la suppression.");
            }
        } else {
            messageLabel.setText("Veuillez sélectionner un article à supprimer.");
        }
    }
}