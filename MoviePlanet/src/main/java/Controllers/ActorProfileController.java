package Controllers;

import BuisnessClasses.Actor;
import Facades.ActorFacade;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.*;

public class ActorProfileController {
    @FXML private Label lblActorName;
    @FXML private Label lblNationality;
    @FXML private Label lblAge;
    @FXML private ImageView imgActorPhoto;
    @FXML private TextArea txtBiography;
    @FXML private ListView<String> listFilmography;
    @FXML private Button backButton;

    private ActorFacade actorFacade;
    private Actor currentActor;

    public ActorProfileController() {
        this.actorFacade = new ActorFacade();
    }

    @FXML
    public void initialize() {
        txtBiography.setEditable(false);
        txtBiography.setWrapText(true);
    }

    public void loadActorProfile(int actorId) {
        try {
            currentActor = actorFacade.getActorById(actorId);
            if (currentActor != null) {
                displayActorDetails(currentActor);
            } else {
                showErrorMessage("Acteur non trouvé");
            }
        } catch (Exception e) {
            showErrorMessage("Erreur lors du chargement du profil: " + e.getMessage());
        }
    }

    private void displayActorDetails(Actor actor) {
        lblActorName.setText(actor.getName());
        lblNationality.setText("Nationalité: " + actor.getNationality());
        lblAge.setText("Âge: " + actor.getAge() + " ans");
        txtBiography.setText(actor.getBiography());

        // Charger la photo
        if (actor.getPhoto() != null && !actor.getPhoto().isEmpty()) {
            try {
                Image image = new Image(actor.getPhoto());
                imgActorPhoto.setImage(image);
            } catch (Exception e) {
                System.err.println("Impossible de charger la photo: " + e.getMessage());
            }
        }

        // Charger la filmographie (à implémenter selon votre modèle)
        // loadFilmography(actor.getId());
    }

    @FXML
    public void onBackButtonClick() {
        // Retourner à la vue précédente
        System.out.println("Retour à la liste des acteurs");
        // Utiliser SceneManager pour revenir à ActorListView
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}