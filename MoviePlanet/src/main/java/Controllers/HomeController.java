package Controllers;

import BuisnessClasses.Movie;
import Facades.SessionFacade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class HomeController {

    @FXML private Label welcomeLabel;
    @FXML private FlowPane moviesContainer;
    @FXML private TextField searchField;

    @FXML
    public void initialize() {
        if (SessionFacade.getInstance().getCurrentUser() != null) {
            welcomeLabel.setText("Bienvenue, " + SessionFacade.getInstance().getCurrentUser().getUsername());
        }
        loadMovies();
    }

    private void loadMovies() {
        List<Movie> movies = SessionFacade.getInstance().getAllMovies();
        moviesContainer.getChildren().clear();

        for (Movie movie : movies) {
            VBox card = createMovieCard(movie);
            moviesContainer.getChildren().add(card);
        }
    }

    private VBox createMovieCard(Movie movie) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.TOP_CENTER);
        card.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-background-radius: 5; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
        card.setPrefWidth(160);
        card.setPrefHeight(260);

        // Image
        ImageView imageView = new ImageView();
        imageView.setFitWidth(140);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);

        try {
            // Chargement de l'image (URL ou placeholder si vide/erreur)
            String url = (movie.getPosterUrl() != null && !movie.getPosterUrl().isEmpty()) ? movie.getPosterUrl() : "https://via.placeholder.com/150x225?text=No+Image";
            imageView.setImage(new Image(url, true)); // true = loading en arrière-plan
        } catch (Exception e) {
            // Gestion silencieuse
        }

        // Titre
        Label titleLabel = new Label(movie.getTitle());
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13;");
        titleLabel.setWrapText(true);
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        card.getChildren().addAll(imageView, titleLabel);

        // Interaction : Clic sur la carte
        card.setOnMouseClicked(event -> openMovieDetails(movie, event));
        card.setStyle(card.getStyle() + "-fx-cursor: hand;"); // Curseur main au survol

        return card;
    }

    private void openMovieDetails(Movie movie, javafx.scene.input.MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/movie-detail-view.fxml"));
            Parent root = loader.load();


            MovieDetailController controller = loader.getController();
            controller.setMovie(movie);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    public void openNotifications() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/notification-view.fxml"));
            Parent root = loader.load();

            // Ouvrir dans une nouvelle petite fenêtre (Pop-up)
            Stage stage = new Stage();
            stage.setTitle("Vos Notifications");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleSearch() {
        String query = searchField.getText();

        List<Movie> results;
        if (query == null || query.trim().isEmpty()) {
            results = SessionFacade.getInstance().getAllMovies();
        } else {
            results = SessionFacade.getInstance().searchMovies(query);
        }

        moviesContainer.getChildren().clear();

        if (results == null || results.isEmpty()) {
            Label noResultLabel = new Label("Aucun film trouvé.");
            noResultLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16;");
            moviesContainer.getChildren().add(noResultLabel);
        } else {
            for (Movie movie : results) {
                VBox card = createMovieCard(movie);
                moviesContainer.getChildren().add(card);
            }
        }
    }

    @FXML
    public void openStore(ActionEvent event) {
        try {
            // Chargement de la vue Boutique Utilisateur
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/userStore-view.fxml"));
            Parent root = loader.load();

            // Changement de scène
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}