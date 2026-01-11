package Controllers;

import Services.ActorService;
import BuisnessClasses.Actor;
import Facades.ActorFacade;
import Persistence.ActorDAO;
import Persistence.PostgresFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Contrôleur pour l'administration des acteurs
 */
public class ActorAdminController {

    // ============================================
    // TAB ACTEURS - Table et colonnes
    // ============================================
    @FXML private TableView<Actor> actorTable;
    @FXML private TableColumn<Actor, Integer> actorIdColumn;
    @FXML private TableColumn<Actor, String> actorNameColumn;
    @FXML private TableColumn<Actor, String> actorNationalityColumn;
    @FXML private TableColumn<Actor, Integer> actorAgeColumn;

    // Champs de formulaire
    @FXML private TextField actorNameField;
    @FXML private TextField actorNationalityField;
    @FXML private TextField actorAgeField;
    @FXML private TextField actorPhotoUrlField;
    @FXML private TextArea actorBiographyArea;
    @FXML private Label actorMessageLabel;

    // ============================================
    // TAB USERS (si nécessaire)
    // ============================================
    @FXML private TableView<?> userTable;
    @FXML private TableColumn<?, ?> userIdColumn;
    @FXML private TableColumn<?, ?> userPseudoColumn;
    @FXML private TableColumn<?, ?> userEmailColumn;
    @FXML private TableColumn<?, ?> userRoleColumn;

    // ============================================
    // TAB MOVIES (si nécessaire)
    // ============================================
    @FXML private TableView<?> movieTable;
    @FXML private TableColumn<?, ?> movieIdColumn;
    @FXML private TableColumn<?, ?> movieTitleColumn;
    @FXML private TableColumn<?, ?> movieGenreColumn;
    @FXML private TableColumn<?, ?> movieDurationColumn;
    @FXML private TextField titleField;
    @FXML private TextField genreField;
    @FXML private TextField durationField;
    @FXML private TextField posterUrlField;
    @FXML private TextArea descriptionArea;
    @FXML private Label movieMessageLabel;

    // ============================================
    // TAB NOTIFICATIONS (si nécessaire)
    // ============================================
    @FXML private TextArea notifMessageArea;
    @FXML private Label notifStatusLabel;

    // Services
    private ActorFacade actorFacade;
    private ObservableList<Actor> actorList;
    private Actor selectedActor;

    /**
     * Initialisation du contrôleur
     */
    @FXML
    public void initialize() {
        // Initialiser le service des acteurs
        PostgresFactory factory = new PostgresFactory();
        ActorDAO actorDAO = factory.createActorDAO();
        ActorService actorService = new ActorService(actorDAO);
        actorFacade = new ActorFacade(actorService);

        // Initialiser la liste observable
        actorList = FXCollections.observableArrayList();

        // Configurer les colonnes de la table
        setupActorTableColumns();

        // Charger les acteurs
        loadActors();

        // Ajouter un listener pour la sélection
        actorTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> onActorSelected(newValue)
        );
    }

    /**
     * Configure les colonnes de la table des acteurs
     */
    private void setupActorTableColumns() {
        actorIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        actorNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        actorNationalityColumn.setCellValueFactory(new PropertyValueFactory<>("nationality"));
        actorAgeColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
    }

    /**
     * Charge tous les acteurs depuis la base de données
     */
    private void loadActors() {
        try {
            actorList.clear();
            actorList.addAll(actorFacade.getAllActors());
            actorTable.setItems(actorList);
            clearActorMessage();
        } catch (Exception e) {
            showActorError("Erreur lors du chargement des acteurs: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gère la sélection d'un acteur dans la table
     */
    private void onActorSelected(Actor actor) {
        if (actor != null) {
            selectedActor = actor;
            fillActorForm(actor);
        }
    }

    /**
     * Remplit le formulaire avec les données d'un acteur
     */
    private void fillActorForm(Actor actor) {
        actorNameField.setText(actor.getName());
        actorNationalityField.setText(actor.getNationality());
        actorAgeField.setText(String.valueOf(actor.getAge()));
        actorPhotoUrlField.setText(actor.getPhoto());
        actorBiographyArea.setText(actor.getBiography());
    }

    /**
     * Ajoute un nouvel acteur
     */
    @FXML
    public void handleAddActor() {
        try {
            // Valider les champs
            if (!validateActorForm()) {
                return;
            }

            // Créer un nouvel acteur
            Actor newActor = createActorFromForm();
            newActor.setId(0); // Nouveau acteur

            // Sauvegarder dans la base de données
            ActorService service = new ActorService(new PostgresFactory().createActorDAO());
            boolean success = service.saveActor(newActor);

            if (success) {
                showActorSuccess("Acteur ajouté avec succès !");
                loadActors();
                handleClearActorForm();
            } else {
                showActorError("Erreur lors de l'ajout de l'acteur");
            }

        } catch (Exception e) {
            showActorError("Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Modifie un acteur existant
     */
    @FXML
    public void handleUpdateActor() {
        try {
            if (selectedActor == null) {
                showActorError("Veuillez sélectionner un acteur à modifier");
                return;
            }

            // Valider les champs
            if (!validateActorForm()) {
                return;
            }

            // Mettre à jour l'acteur
            Actor updatedActor = createActorFromForm();
            updatedActor.setId(selectedActor.getId());

            // Sauvegarder dans la base de données
            ActorService service = new ActorService(new PostgresFactory().createActorDAO());
            boolean success = service.updateActor(updatedActor);

            if (success) {
                showActorSuccess("Acteur modifié avec succès !");
                loadActors();
                handleClearActorForm();
            } else {
                showActorError("Erreur lors de la modification de l'acteur");
            }

        } catch (Exception e) {
            showActorError("Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Supprime l'acteur sélectionné
     */
    @FXML
    public void handleDeleteActor() {
        Actor selected = actorTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showActorError("Veuillez sélectionner un acteur à supprimer");
            return;
        }

        // Confirmation
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmation");
        confirmAlert.setHeaderText("Supprimer l'acteur");
        confirmAlert.setContentText("Êtes-vous sûr de vouloir supprimer " + selected.getName() + " ?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    ActorService service = new ActorService(new PostgresFactory().createActorDAO());
                    boolean success = service.deleteActor(selected.getId());

                    if (success) {
                        showActorSuccess("Acteur supprimé avec succès !");
                        loadActors();
                        handleClearActorForm();
                    } else {
                        showActorError("Erreur lors de la suppression");
                    }
                } catch (Exception e) {
                    showActorError("Erreur: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Vide le formulaire des acteurs
     */
    @FXML
    public void handleClearActorForm() {
        actorNameField.clear();
        actorNationalityField.clear();
        actorAgeField.clear();
        actorPhotoUrlField.clear();
        actorBiographyArea.clear();
        selectedActor = null;
        actorTable.getSelectionModel().clearSelection();
        clearActorMessage();
    }

    /**
     * Valide le formulaire des acteurs
     */
    private boolean validateActorForm() {
        if (actorNameField.getText().trim().isEmpty()) {
            showActorError("Le nom de l'acteur est obligatoire");
            return false;
        }

        if (actorAgeField.getText().trim().isEmpty()) {
            showActorError("L'âge est obligatoire");
            return false;
        }

        try {
            int age = Integer.parseInt(actorAgeField.getText().trim());
            if (age < 0 || age > 150) {
                showActorError("L'âge doit être entre 0 et 150");
                return false;
            }
        } catch (NumberFormatException e) {
            showActorError("L'âge doit être un nombre valide");
            return false;
        }

        return true;
    }

    /**
     * Crée un objet Actor à partir du formulaire
     */
    private Actor createActorFromForm() {
        Actor actor = new Actor();
        actor.setName(actorNameField.getText().trim());
        actor.setNationality(actorNationalityField.getText().trim());
        actor.setAge(Integer.parseInt(actorAgeField.getText().trim()));
        actor.setPhoto(actorPhotoUrlField.getText().trim());
        actor.setBiography(actorBiographyArea.getText().trim());
        return actor;
    }

    /**
     * Affiche un message d'erreur
     */
    private void showActorError(String message) {
        actorMessageLabel.setText("❌ " + message);
        actorMessageLabel.setStyle("-fx-text-fill: #e74c3c;");
    }

    /**
     * Affiche un message de succès
     */
    private void showActorSuccess(String message) {
        actorMessageLabel.setText("✓ " + message);
        actorMessageLabel.setStyle("-fx-text-fill: #27ae60;");
    }

    /**
     * Efface le message
     */
    private void clearActorMessage() {
        actorMessageLabel.setText("");
    }

    // ============================================
    // MÉTHODES POUR LES AUTRES TABS (placeholders)
    // ============================================

    @FXML
    public void handleDeleteUser() {
        showInfo("Fonctionnalité à implémenter");
    }

    @FXML
    public void handleDeleteMovie() {
        showInfo("Fonctionnalité à implémenter");
    }

    @FXML
    public void handleAddMovie() {
        showInfo("Fonctionnalité à implémenter");
    }

    @FXML
    public void handleUpdateMovie() {
        showInfo("Fonctionnalité à implémenter");
    }

    @FXML
    public void handleClearForm() {
        if (titleField != null) titleField.clear();
        if (genreField != null) genreField.clear();
        if (durationField != null) durationField.clear();
        if (posterUrlField != null) posterUrlField.clear();
        if (descriptionArea != null) descriptionArea.clear();
    }

    @FXML
    public void handleSendNotification() {
        showInfo("Notification envoyée !");
    }

    @FXML
    public void handleLogout() {
        // Implémenter la déconnexion
        showInfo("Déconnexion...");
        // Fermer la fenêtre ou revenir à l'écran de connexion
    }

    /**
     * Affiche une alerte d'information
     */
    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}