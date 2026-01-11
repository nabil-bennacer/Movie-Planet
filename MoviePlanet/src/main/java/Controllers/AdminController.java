package Controllers;

import BuisnessClasses.Movie;
import BuisnessClasses.Actor;
import BuisnessClasses.User;
import Facades.SessionFacade;
import Facades.ActorFacade;
import Services.ActorService;
import Persistence.ActorDAO;
import Persistence.PostgresFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminController {

    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, Integer> userIdColumn;
    @FXML private TableColumn<User, String> userPseudoColumn;
    @FXML private TableColumn<User, String> userEmailColumn;
    @FXML private TableColumn<User, String> userRoleColumn;


    @FXML private TableView<Movie> movieTable;
    @FXML private TableColumn<Movie, Integer> movieIdColumn;
    @FXML private TableColumn<Movie, String> movieTitleColumn;
    @FXML private TableColumn<Movie, String> movieGenreColumn;
    @FXML private TableColumn<Movie, Integer> movieDurationColumn;

    @FXML private TableView<Actor> actorTable;
    @FXML private TableColumn<Actor, Integer> actorIdColumn;
    @FXML private TableColumn<Actor, String> actorNameColumn;
    @FXML private TableColumn<Actor, String> actorNationalityColumn;
    @FXML private TableColumn<Actor, Integer> actorAgeColumn;
    @FXML private TextField actorNameField;
    @FXML private TextField actorNationalityField;
    @FXML private TextField actorAgeField;
    @FXML private TextField actorPhotoUrlField;
    @FXML private TextArea actorBiographyArea;
    @FXML private Label actorMessageLabel;

    private ActorFacade actorFacade;
    private ObservableList<Actor> actorList;
    private Actor selectedActor;

    @FXML private TextField titleField;
    @FXML private TextField genreField;
    @FXML private TextField durationField;
    @FXML private TextField posterUrlField;
    @FXML private TextArea descriptionArea;
    @FXML private Label movieMessageLabel;
    @FXML private TextArea notifMessageArea;
    @FXML private Label notifStatusLabel;

    @FXML
    public void initialize() {

        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        userPseudoColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        userEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        userRoleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));


        movieIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        movieTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        movieGenreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        movieDurationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));

        movieTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillForm(newSelection);
            }
        });

        try {

            actorFacade = new ActorFacade();
            actorList = FXCollections.observableArrayList();

            if (actorTable != null) {
                setupActorTableColumns();
                loadActors();
                actorTable.getSelectionModel().selectedItemProperty().addListener(
                        (observable, oldValue, newValue) -> onActorSelected(newValue)
                );
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'initialisation des acteurs: " + e.getMessage());
            e.printStackTrace();
        }

        refreshAll();
    }

    private void fillForm(Movie movie) {
        titleField.setText(movie.getTitle());
        genreField.setText(movie.getGenre());
        durationField.setText(String.valueOf(movie.getDuration()));
        posterUrlField.setText(movie.getPosterUrl());
        descriptionArea.setText(movie.getDescription());
        movieMessageLabel.setText("Film sélectionné (ID: " + movie.getId() + ")");
        movieMessageLabel.setStyle("-fx-text-fill: blue;");
    }

    @FXML
    public void handleClearForm() {
        titleField.clear();
        genreField.clear();
        durationField.clear();
        posterUrlField.clear();
        descriptionArea.clear();
        movieTable.getSelectionModel().clearSelection();
        movieMessageLabel.setText("Formulaire vidé. Prêt pour l'ajout.");
        movieMessageLabel.setStyle("-fx-text-fill: black;");
    }

    @FXML
    public void handleUpdateMovie() {
        Movie selected = movieTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            movieMessageLabel.setText("Veuillez sélectionner un film dans la liste.");
            movieMessageLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        try {
            String title = titleField.getText();
            String genre = genreField.getText();
            String desc = descriptionArea.getText();
            String url = posterUrlField.getText();
            int duration = Integer.parseInt(durationField.getText());


            boolean success = SessionFacade.getInstance().updateMovie(selected.getId(), title, desc, genre, duration, url);

            if (success) {
                movieMessageLabel.setText("Film modifié avec succès !");
                movieMessageLabel.setStyle("-fx-text-fill: green;");
                handleClearForm();
                loadMovies();
            } else {
                movieMessageLabel.setText("Erreur lors de la modification.");
            }
        } catch (NumberFormatException e) {
            movieMessageLabel.setText("La durée doit être un nombre.");
        }
    }

    private void refreshAll() {
        loadUsers();
        loadMovies();
    }

    private void loadUsers() {
        if (SessionFacade.getInstance().getAllUsers() != null) {
            userTable.setItems(FXCollections.observableArrayList(SessionFacade.getInstance().getAllUsers()));
        }
    }

    private void loadMovies() {
        if (SessionFacade.getInstance().getAllMovies() != null) {
            movieTable.setItems(FXCollections.observableArrayList(SessionFacade.getInstance().getAllMovies()));
        }
    }


    @FXML
    public void handleDeleteUser() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            SessionFacade.getInstance().deleteUser(selected.getId());
            loadUsers();
        }
    }


    @FXML
    public void handleAddMovie() {
        try {
            String title = titleField.getText();
            String genre = genreField.getText();
            String desc = descriptionArea.getText();
            String url = posterUrlField.getText();
            int duration = Integer.parseInt(durationField.getText());

            if (title.isEmpty() || genre.isEmpty()) {
                movieMessageLabel.setText("Titre et Genre obligatoires.");
                return;
            }

            boolean success = SessionFacade.getInstance().createMovie(title, desc, genre, duration, url);
            if (success) {
                movieMessageLabel.setText("Film ajouté !");
                movieMessageLabel.setStyle("-fx-text-fill: green;");
                clearMovieForm();
                loadMovies();
                handleClearForm();
            } else {
                movieMessageLabel.setText("Erreur lors de l'ajout.");
            }
        } catch (NumberFormatException e) {
            movieMessageLabel.setText("La durée doit être un nombre entier.");
            movieMessageLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    public void handleDeleteMovie() {
        Movie selected = movieTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            SessionFacade.getInstance().deleteMovie(selected.getId());
            loadMovies();
        } else {
            movieMessageLabel.setText("Sélectionnez un film à supprimer.");
        }
    }

    private void clearMovieForm() {
        titleField.clear();
        genreField.clear();
        durationField.clear();
        posterUrlField.clear();
        descriptionArea.clear();
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        SessionFacade.getInstance().logout();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/main/login-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML
    public void handleSendNotification() {
        String msg = notifMessageArea.getText();
        if (msg.isEmpty()) {
            notifStatusLabel.setText("Le message ne peut pas être vide.");
            return;
        }

        boolean success = SessionFacade.getInstance().sendNotification(msg);

        if (success) {
            notifStatusLabel.setText("Notification envoyée à tous les utilisateurs !");
            notifStatusLabel.setStyle("-fx-text-fill: green;");
            notifMessageArea.clear();
        } else {
            notifStatusLabel.setText("Erreur lors de l'envoi.");
            notifStatusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    private void setupActorTableColumns() {
        actorIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        actorNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        actorNationalityColumn.setCellValueFactory(new PropertyValueFactory<>("nationality"));
        actorAgeColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
    }

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

    private void onActorSelected(Actor actor) {
        if (actor != null) {
            selectedActor = actor;
            fillActorForm(actor);
        }
    }

    private void fillActorForm(Actor actor) {
        actorNameField.setText(actor.getName());
        actorNationalityField.setText(actor.getNationality());
        actorAgeField.setText(String.valueOf(actor.getAge()));
        actorPhotoUrlField.setText(actor.getPhoto());
        actorBiographyArea.setText(actor.getBiography());
    }

    @FXML
public void handleAddActor() {
    try {
        if (!validateActorForm()) return;

        Actor newActor = createActorFromForm();
        newActor.setId(0);

        PostgresFactory factory = new PostgresFactory();
        ActorDAO actorDAO = factory.createActorDAO();
        ActorService service = new ActorService(actorDAO);

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

    @FXML
    public void handleUpdateActor() {
    try {
        if (selectedActor == null) {
            showActorError("Veuillez sélectionner un acteur à modifier");
            return;
        }

        if (!validateActorForm()) return;

        Actor updatedActor = createActorFromForm();
        updatedActor.setId(selectedActor.getId());


        PostgresFactory factory = new PostgresFactory();
        ActorDAO actorDAO = factory.createActorDAO();
        ActorService service = new ActorService(actorDAO);

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

    @FXML
public void handleDeleteActor() {
    Actor selected = actorTable.getSelectionModel().getSelectedItem();

    if (selected == null) {
        showActorError("Veuillez sélectionner un acteur à supprimer");
        return;
    }

    Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
    confirmAlert.setTitle("Confirmation");
    confirmAlert.setHeaderText("Supprimer l'acteur");
    confirmAlert.setContentText("Êtes-vous sûr de vouloir supprimer " + selected.getName() + " ?");

    confirmAlert.showAndWait().ifPresent(response -> {
        if (response == ButtonType.OK) {
            try {
                PostgresFactory factory = new PostgresFactory();
                ActorDAO actorDAO = factory.createActorDAO();
                ActorService service = new ActorService(actorDAO);

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

    private Actor createActorFromForm() {
        Actor actor = new Actor();
        actor.setName(actorNameField.getText().trim());
        actor.setNationality(actorNationalityField.getText().trim());
        actor.setAge(Integer.parseInt(actorAgeField.getText().trim()));
        actor.setPhoto(actorPhotoUrlField.getText().trim());
        actor.setBiography(actorBiographyArea.getText().trim());
        return actor;
    }

    private void showActorError(String message) {
        actorMessageLabel.setText("❌ " + message);
        actorMessageLabel.setStyle("-fx-text-fill: #e74c3c;");
    }

    private void showActorSuccess(String message) {
        actorMessageLabel.setText("✓ " + message);
        actorMessageLabel.setStyle("-fx-text-fill: #27ae60;");
    }

    private void clearActorMessage() {
        actorMessageLabel.setText("");
    }

}