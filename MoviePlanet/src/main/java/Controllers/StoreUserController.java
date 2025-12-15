package Controllers;

import BuisnessClasses.Article;
import Facades.StoreFacade;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import java.util.List;

public class StoreUserController {

    @FXML
    private TextField searchField;

    @FXML
    private TilePane articleGrid; // Correspond au "GridView" du diagramme

    @FXML
    private Label messageLabel;

    @FXML
    private Button panierButton;

    // Référence vers la Façade (Singleton ou instance unique préférée)
    private StoreFacade storeFacade;
    private int currentUserId = 1; // ID utilisateur simulé (à remplacer par la session réelle)

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

    // Méthode pour générer l'affichage visuel des cartes d'articles
    private void afficherArticles(List<Article> articles) {
        articleGrid.getChildren().clear(); // On vide la grille actuelle

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

    // Crée une "carte" graphique pour un article (Image + Nom + Prix + Bouton)
    private VBox creerCarteArticle(Article article) {
        VBox card = new VBox(10);
        card.setStyle("-fx-border-color: #bdc3c7; -fx-border-radius: 5; -fx-background-color: white; -fx-padding: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 0);");
        card.setAlignment(Pos.CENTER);

        // Image (Placeholder si pas d'image réelle)
        ImageView imageView = new ImageView();
        try {
            // Essayez de charger l'image, sinon image par défaut
            // Image img = new Image(getClass().getResourceAsStream(article.getImageUrl()));
            // imageView.setImage(img);
            // Pour l'exemple, on met un carré gris :
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            // imageView.setStyle("-fx-background-color: gray;");
        } catch (Exception e) {
            // Ignorer si image non trouvée
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

        card.getChildren().addAll(imageView, nomLabel, filmLabel, prixLabel, stockLabel, btnAjouter);
        return card;
    }

    public void onAjouterPanierClick(int articleId, int qte) {
        boolean succes = storeFacade.ajouterAuPanier(currentUserId, articleId, qte);
        if (succes) {
            afficherMessage("Article ajouté au panier !", false);
            updatePanierIcon();
            // Rafraîchir l'affichage pour mettre à jour le stock visuel
            onRecherche();
        } else {
            // Cas Flux Alternatif 10.2.3 : Stock insuffisant
            Article a = storeFacade.getAllArticles().stream().filter(art -> art.getId() == articleId).findFirst().orElse(null);
            int stockRestant = (a != null) ? a.getStock() : 0;
            afficherErreurStock(stockRestant);
        }
    }

    // Méthode helper pour afficher les erreurs (Diagramme: afficherErreurStock)
    public void afficherErreurStock(int maxDispo) {
        afficherMessage("Stock insuffisant ! Quantité max disponible : " + maxDispo, true);
    }

    private void afficherMessage(String msg, boolean isError) {
        messageLabel.setText(msg);
        messageLabel.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }

    // (Diagramme: updatePanierIcon)
    public void updatePanierIcon() {
        // Ici, on pourrait incrémenter un compteur visuel
        String currentText = panierButton.getText(); // ex: "Panier (0)"
        // Logique simplifiée pour l'exemple
        panierButton.setText("Panier (+1)");
    }
}