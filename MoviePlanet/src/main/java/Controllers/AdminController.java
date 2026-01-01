package Controllers;

import BuisnessClasses.Movie;
import BuisnessClasses.User;
import Facades.SessionFacade;
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

        refreshAll();
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
}