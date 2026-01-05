package Controllers;

import BuisnessClasses.Article;
import BuisnessClasses.Commentaire;
import BuisnessClasses.User;
import Facades.SessionFacade;
import Facades.StoreFacade;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

public class ArticleDetailController {

    @FXML private Label lblNom;
    @FXML private Label lblPrix;
    @FXML private Text txtDescription;
    @FXML private Label lblStock;
    @FXML private ImageView imgArticle;
    @FXML private Spinner<Integer> spinnerQte;
    @FXML private ListView<String> listCommentaires;
    @FXML private TextArea txtAreaCommentaire;
    @FXML private Slider sliderNote;
    @FXML private Label messageLabel;

    private Article currentArticle;
    private StoreFacade storeFacade;
    private SessionFacade sessionFacade;

    public ArticleDetailController() {
        this.storeFacade = new StoreFacade();
        this.sessionFacade = SessionFacade.getInstance();
    }

    /**
     * Méthode appelée depuis StoreUserController pour passer l'article à afficher.
     */
    public void setArticle(Article article) {
        this.currentArticle = article;
        afficherDetails();
        chargerCommentaires();

        // Réinitialiser le message d'info
        if (messageLabel != null) messageLabel.setText("");
    }

    private void afficherDetails() {
        if (currentArticle != null) {
            lblNom.setText(currentArticle.getNom());
            lblPrix.setText(String.format("%.2f €", currentArticle.getPrix()));
            txtDescription.setText(currentArticle.getDescription());

            if (currentArticle.estEnRupture()) {
                lblStock.setText("Rupture de stock");
                lblStock.setStyle("-fx-text-fill: red; -fx-font-style: italic;");
                spinnerQte.setDisable(true); // Empêcher la sélection de quantité si rupture
            } else {
                lblStock.setText("En stock : " + currentArticle.getStock());
                lblStock.setStyle("-fx-text-fill: #7f8c8d; -fx-font-style: italic;");
                spinnerQte.setDisable(false);
                // Configurer le spinner pour ne pas dépasser le stock max
                SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, currentArticle.getStock(), 1);
                spinnerQte.setValueFactory(valueFactory);
            }

            // Gestion de l'image
            try {
                // On suppose que les images sont dans resources ou chargées via URL
                // Si c'est une URL web ou un fichier local :
                if (currentArticle.getImageUrl() != null && !currentArticle.getImageUrl().isEmpty()) {
                    // Attention: Si vos images sont dans src/main/resources/img, utilisez getClass().getResource(...)
                    // Ici on tente un chargement standard (compatible URL http ou file:)
                    // Si c'est un chemin relatif local, il faudra peut-être adapter selon votre structure de dossiers
                    String imagePath = currentArticle.getImageUrl();
                    // Exemple basique : on essaye de charger. En prod, il faudrait gérer le chemin "img/" plus proprement.
                    // Image img = new Image(getClass().getResourceAsStream("/" + imagePath));
                    // imgArticle.setImage(img);
                }
            } catch (Exception e) {
                System.err.println("Impossible de charger l'image : " + e.getMessage());
            }
        }
    }

    private void chargerCommentaires() {
        if (currentArticle == null) return;

        // Récupération via la façade
        List<Commentaire> avis = storeFacade.getCommentaires(currentArticle.getId());

        listCommentaires.getItems().clear();

        if (avis != null && !avis.isEmpty()) {
            for (Commentaire c : avis) {
                // On utilise la méthode toString() de Commentaire définie précédemment
                // Format: "NomUser (Note/5) : Texte"
                listCommentaires.getItems().add(c.toString());
            }
        } else {
            listCommentaires.getItems().add("Aucun avis pour le moment. Soyez le premier !");
        }
    }

    @FXML
    public void onAjouterPanierClick() {
        User user = sessionFacade.getCurrentUser();
        if (user == null) {
            setMessage("Veuillez vous connecter pour acheter.", true);
            return;
        }

        if (currentArticle.estEnRupture()) {
            setMessage("Cet article est en rupture de stock.", true);
            return;
        }

        int qte = spinnerQte.getValue();

        // Appel à la façade
        boolean success = storeFacade.ajouterAuPanier(user.getId(), currentArticle.getId(), qte);

        if (success) {
            setMessage("Article ajouté au panier !", false);
            // Optionnel : fermer la fenêtre après un court délai ou laisser l'utilisateur continuer
        } else {
            setMessage("Erreur : Stock insuffisant.", true);
        }
    }

    @FXML
    public void onPublierCommentaireClick() {
        User user = sessionFacade.getCurrentUser();

        // Vérification de connexion (même si normalement l'accès est restreint)
        if (user == null) {
            setMessage("Vous devez être connecté pour laisser un avis.", true);
            return;
        }

        String texte = txtAreaCommentaire.getText();
        if (texte == null || texte.trim().isEmpty()) {
            setMessage("Votre commentaire ne peut pas être vide.", true);
            return;
        }

        int note = (int) sliderNote.getValue();

        // Création de l'objet métier
        Commentaire nouveauCommentaire = new Commentaire(user.getId(), currentArticle.getId(), texte, note);

        // Appel à la façade pour persister
        boolean success = storeFacade.ajouterCommentaire(nouveauCommentaire);

        if (success) {
            setMessage("Merci pour votre avis !", false);
            txtAreaCommentaire.clear(); // Vider le champ
            sliderNote.setValue(5); // Remettre la note par défaut
            chargerCommentaires(); // Rafraîchir la liste affichée
        } else {
            setMessage("Erreur lors de la publication de l'avis.", true);
        }
    }

    @FXML
    public void onRetourClick() {
        // Fermer la fenêtre actuelle (Pop-up détails)
        Stage stage = (Stage) lblNom.getScene().getWindow();
        stage.close();
    }

    // Méthode utilitaire pour afficher les messages (Erreur en rouge, Succès en vert)
    private void setMessage(String msg, boolean isError) {
        if (messageLabel != null) {
            messageLabel.setText(msg);
            if (isError) {
                messageLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            } else {
                messageLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            }
        }
    }
}