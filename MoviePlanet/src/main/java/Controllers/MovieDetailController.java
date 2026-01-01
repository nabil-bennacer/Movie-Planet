package Controllers;

import BuisnessClasses.Movie;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.IOException;

public class MovieDetailController {

    @FXML private Label titleLabel;
    @FXML private Label genreLabel;
    @FXML private Label durationLabel;
    @FXML private TextArea descriptionArea;
    @FXML private ImageView posterImage;

    private Movie currentMovie;

    public void setMovie(Movie movie) {
        this.currentMovie = movie;
        updateUI();
    }

    private void updateUI() {
        if (currentMovie != null) {
            titleLabel.setText(currentMovie.getTitle());
            genreLabel.setText("Genre : " + currentMovie.getGenre());
            durationLabel.setText("Durée : " + currentMovie.getDuration() + " min");
            descriptionArea.setText(currentMovie.getDescription());

            try {
                String url = (currentMovie.getPosterUrl() != null) ? currentMovie.getPosterUrl() : "https://via.placeholder.com/300x450";
                posterImage.setImage(new Image(url));
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    @FXML
    public void goBack(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/main/movie-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}