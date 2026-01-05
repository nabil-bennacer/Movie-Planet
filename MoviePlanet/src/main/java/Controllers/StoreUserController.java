package Controllers;

import BuisnessClasses.Article;
import Facades.StoreFacade;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class StoreUserController {

    @FXML
    private TextField searchField;

    @FXML
    private TilePane articleGrid;

    @FXML
    private Label messageLabel;

    @FXML
    private Button panierButton;

    private StoreFacade storeFacade;
    private int currentUserId = 1; // ID utilisateur simulé

    public StoreUserController() {
        this.storeFacade = new StoreFacade();
    }

    @FXML
    public void initialize() {
        // Au chargement, on affiche tous les articles
        List<Article> articles = storeFacade.getAllArticles();
        afficherArticles(articles);
    }

    @FXML
    public void onRecherche() {
        String query = searchField.getText();
        List<Article> resultats;

        if (query == null || query.trim().isEmpty()) {
            resultats = storeFacade.getAllArticles();
        } else {
            resultats = storeFacade.searchArticles(query);
        }
        afficherArticles(resultats);
    }

    private void afficherArticles(List<Article> articles) {
        articleGrid.getChildren().clear();

        if (articles.isEmpty()) {
            messageLabel.setText("Aucun article trouvé.");
            return;
        } else {
            messageLabel.setText(articles.size() + " articles disponibles.");
        }

        for (Article a : articles) {
            VBox card = creerCarteArticle(a);
            articleGrid.getChildren().add(card);
        }
    }

    private VBox creerCarteArticle(Article article) {
        VBox card = new VBox(10);
        // Ajout du curseur main pour indiquer que c'est cliquable
        card.setStyle("-fx-border-color: #bdc3c7; -fx-border-radius: 5; -fx-background-color: white; -fx-padding: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 0); -fx-cursor: hand;");
        card.setAlignment(Pos.CENTER);

        // --- GESTION DU CLIC SUR LA CARTE ---
        card.setOnMouseClicked(event -> {
            ouvrirDetailsArticle(article);
        });

        // Image
        ImageView imageView = new ImageView();
        try {
            if (article.getImageUrl() != null && !article.getImageUrl().isEmpty()) {
                // Tentative de chargement d'image (ajustez selon votre dossier resources)
                // Image img = new Image(getClass().getResourceAsStream("/" + article.getImageUrl()));
                // imageView.setImage(img);
            }
            // Placeholder par défaut
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
        } catch (Exception e) {
            // Ignorer erreur image
        }

        Label nomLabel = new Label(article.getNom());
        nomLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        Label filmLabel = new Label("(" + article.getFilmLie() + ")");
        filmLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 10;");

        Label prixLabel = new Label(String.format("%.2f €", article.getPrix()));
        prixLabel.setStyle("-fx-text-fill: #e67e22; -fx-font-weight: bold;");

        Label stockLabel = new Label(article.getStock() > 0 ? "En stock: " + article.getStock() : "RUPTURE");
        stockLabel.setStyle(article.getStock() > 0 ? "-fx-text-fill: green;" : "-fx-text-fill: red;");

        Button btnAjouter = new Button("Ajouter au panier");
        btnAjouter.setDisable(article.estEnRupture());

        // Action du bouton Ajouter
        btnAjouter.setOnAction(e -> onAjouterPanierClick(article.getId(), 1));

        // IMPORTANT : Empêcher le clic du bouton de propager l'événement à la carte (sinon ça ouvre les détails en même temps)
        btnAjouter.setOnMouseClicked(event -> event.consume());

        card.getChildren().addAll(imageView, nomLabel, filmLabel, prixLabel, stockLabel, btnAjouter);
        return card;
    }

    // --- NOUVELLE MÉTHODE : Ouverture de la fenêtre de détails ---
    private void ouvrirDetailsArticle(Article article) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/articleDetail-view.fxml"));
            Parent root = loader.load();

            // Passer l'article au contrôleur de détail
            ArticleDetailController controller = loader.getController();
            controller.setArticle(article);

            Stage stage = new Stage();
            stage.setTitle("Détails : " + article.getNom());
            stage.setScene(new Scene(root));

            // Fenêtre modale (bloque la fenêtre principale tant qu'elle est ouverte)
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            afficherMessage("Erreur impossible d'ouvrir les détails : " + e.getMessage(), true);
        }
    }

    public void onAjouterPanierClick(int articleId, int qte) {
        boolean succes = storeFacade.ajouterAuPanier(currentUserId, articleId, qte);
        if (succes) {
            afficherMessage("Article ajouté au panier !", false);
            updatePanierIcon();
            onRecherche(); // Rafraîchir pour MAJ stock visuel
        } else {
            Article a = storeFacade.getAllArticles().stream().filter(art -> art.getId() == articleId).findFirst().orElse(null);
            int stockRestant = (a != null) ? a.getStock() : 0;
            afficherErreurStock(stockRestant);
        }
    }

    public void afficherErreurStock(int maxDispo) {
        afficherMessage("Stock insuffisant ! Max dispo : " + maxDispo, true);
    }

    private void afficherMessage(String msg, boolean isError) {
        messageLabel.setText(msg);
        messageLabel.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }

    public void updatePanierIcon() {
        panierButton.setText("Panier (+1)");
    }
}