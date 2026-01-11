package Controllers;

import BuisnessClasses.Actor;
import Facades.ActorFacade;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.util.List;

public class ActorController {
    @FXML private TextField searchField;
    @FXML private ComboBox<String> filterComboBox;
    @FXML private GridPane actorGridPane;
    @FXML private Button searchButton;

    private ActorFacade actorFacade;
    private ObservableList<Actor> actorList;

    public ActorController() {
        this.actorFacade = new ActorFacade();
        this.actorList = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        setupFilterComboBox();
        loadAllActors();
        setupSearchListener();
    }

    private void setupFilterComboBox() {
        filterComboBox.setItems(FXCollections.observableArrayList(
                "Tous",
                "Mes favoris"
        ));
        filterComboBox.setValue("Tous");
        filterComboBox.setOnAction(e -> onFilterChange());
    }

    private void setupSearchListener() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() >= 2) {
                List<String> suggestions = actorFacade.getAutocompleteSuggestions(newValue);
                // Afficher les suggestions (implémentation dépend de votre UI)
            }
        });
    }

    private void loadAllActors() {
        try {
            List<Actor> actors = actorFacade.getAllActors();
            actorList.setAll(actors);
            displayActors(actors);
        } catch (Exception e) {
            showErrorMessage("Erreur lors du chargement des acteurs: " + e.getMessage());
        }
    }

    @FXML
    public void onSearchButtonClick() {
        String searchTerm = searchField.getText().trim();

        if (searchTerm.isEmpty()) {
            loadAllActors();
            return;
        }

        try {
            List<Actor> results = actorFacade.searchActorsByName(searchTerm);
            actorList.setAll(results);
            displayActors(results);

            if (results.isEmpty()) {
                showInfoMessage("Aucun acteur trouvé pour '" + searchTerm + "'");
            }
        } catch (Exception e) {
            showErrorMessage("Erreur lors de la recherche: " + e.getMessage());
        }
    }

    @FXML
    public void onFilterChange() {
        String selectedFilter = filterComboBox.getValue();

        if ("Tous".equals(selectedFilter)) {
            loadAllActors();
        } else if ("Mes favoris".equals(selectedFilter)) {
            // Implémenter le filtrage des favoris
            showInfoMessage("Filtrage par favoris à implémenter");
        }
    }

    public void onActorCardClick(int actorId) {
        try {
            Actor actor = actorFacade.getActorById(actorId);
            if (actor != null) {
                openActorProfile(actor);
            } else {
                showErrorMessage("Acteur non trouvé");
            }
        } catch (Exception e) {
            showErrorMessage("Erreur lors de l'ouverture du profil: " + e.getMessage());
        }
    }

    /**
     * Affiche les acteurs dans le GridPane
     */
    private void displayActors(List<Actor> actors) {
        actorGridPane.getChildren().clear();

        if (actors.isEmpty()) {
            // Afficher un message si aucun acteur
            Label emptyLabel = new Label("Aucun acteur trouvé");
            emptyLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #7f8c8d;");
            actorGridPane.add(emptyLabel, 0, 0);
            return;
        }

        int column = 0;
        int row = 0;

        for (Actor actor : actors) {
            VBox card = createActorCard(actor);
            actorGridPane.add(card, column, row);

            column++;
            if (column == 4) { // 4 colonnes
                column = 0;
                row++;
            }
        }
    }

    /**
     * Crée une carte visuelle pour un acteur
     */
    private VBox createActorCard(Actor actor) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(15));
        card.setAlignment(Pos.TOP_CENTER);
        card.setPrefWidth(250);
        card.setMinHeight(350);
        card.setStyle(
            "-fx-background-color: white; " +
            "-fx-border-color: #e0e0e0; " +
            "-fx-border-width: 1px; " +
            "-fx-border-radius: 10px; " +
            "-fx-background-radius: 10px; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );

        // Photo placeholder (grande icône)
        Label photoLabel = new Label("👤");
        photoLabel.setStyle("-fx-font-size: 80px;");
        photoLabel.setAlignment(Pos.CENTER);

        // Nom de l'acteur
        Label nameLabel = new Label(actor.getName());
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        nameLabel.setStyle("-fx-text-fill: #2c3e50;");
        nameLabel.setWrapText(true);
        nameLabel.setMaxWidth(220);
        nameLabel.setAlignment(Pos.CENTER);

        // Ligne de séparation
        Separator separator = new Separator();
        separator.setPrefWidth(200);

        // Nationalité
        HBox nationalityBox = new HBox(5);
        nationalityBox.setAlignment(Pos.CENTER);
        Label nationalityIcon = new Label("🌍");
        nationalityIcon.setStyle("-fx-font-size: 14px;");
        Label nationalityLabel = new Label(actor.getNationality() != null ? actor.getNationality() : "N/A");
        nationalityLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 14px;");
        nationalityBox.getChildren().addAll(nationalityIcon, nationalityLabel);

        // Âge
        HBox ageBox = new HBox(5);
        ageBox.setAlignment(Pos.CENTER);
        Label ageIcon = new Label("🎂");
        ageIcon.setStyle("-fx-font-size: 14px;");
        Label ageLabel = new Label(actor.getAge() + " ans");
        ageLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 14px;");
        ageBox.getChildren().addAll(ageIcon, ageLabel);

        // Spacer pour pousser le bouton en bas
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // Bouton "Voir le profil"
        Button viewProfileBtn = new Button("Voir le profil");
        viewProfileBtn.setPrefWidth(200);
        viewProfileBtn.setStyle(
            "-fx-background-color: #3498db; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 13px; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 5px; " +
            "-fx-padding: 10 20; " +
            "-fx-cursor: hand;"
        );
        viewProfileBtn.setOnAction(e -> onActorCardClick(actor.getId()));

        // Effet hover sur le bouton
        viewProfileBtn.setOnMouseEntered(e -> {
            viewProfileBtn.setStyle(
                "-fx-background-color: #2980b9; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 13px; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 5px; " +
                "-fx-padding: 10 20; " +
                "-fx-cursor: hand;"
            );
        });
        viewProfileBtn.setOnMouseExited(e -> {
            viewProfileBtn.setStyle(
                "-fx-background-color: #3498db; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 13px; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 5px; " +
                "-fx-padding: 10 20; " +
                "-fx-cursor: hand;"
            );
        });

        // Ajouter tous les éléments à la carte
        card.getChildren().addAll(
            photoLabel,
            nameLabel,
            separator,
            nationalityBox,
            ageBox,
            spacer,
            viewProfileBtn
        );

        // Effet hover sur toute la carte
        card.setOnMouseEntered(e -> {
            card.setStyle(
                "-fx-background-color: #f8f9fa; " +
                "-fx-border-color: #3498db; " +
                "-fx-border-width: 2px; " +
                "-fx-border-radius: 10px; " +
                "-fx-background-radius: 10px; " +
                "-fx-effect: dropshadow(gaussian, rgba(52, 152, 219, 0.3), 15, 0, 0, 3); " +
                "-fx-cursor: hand;"
            );
        });

        card.setOnMouseExited(e -> {
            card.setStyle(
                "-fx-background-color: white; " +
                "-fx-border-color: #e0e0e0; " +
                "-fx-border-width: 1px; " +
                "-fx-border-radius: 10px; " +
                "-fx-background-radius: 10px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
            );
        });

        return card;
    }

    /**
     * Ouvre le profil détaillé d'un acteur dans une nouvelle fenêtre
     */
    private void openActorProfile(Actor actor) {
        try {
            // Créer une nouvelle fenêtre pour le profil
            Stage profileStage = new Stage();
            profileStage.setTitle("Profil - " + actor.getName());

            // Créer le layout du profil
            VBox profileLayout = createProfileLayout(actor);

            Scene scene = new Scene(profileLayout, 700, 600);
            profileStage.setScene(scene);
            profileStage.show();

        } catch (Exception e) {
            showErrorMessage("Erreur lors de l'ouverture du profil: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Crée le layout pour le profil détaillé
     */
    private VBox createProfileLayout(Actor actor) {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: white;");
        layout.setAlignment(Pos.TOP_CENTER);

        // Photo
        Label photoLabel = new Label("👤");
        photoLabel.setStyle("-fx-font-size: 120px;");

        // Nom
        Label nameLabel = new Label(actor.getName());
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 32));
        nameLabel.setStyle("-fx-text-fill: #2c3e50;");

        // Informations
        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(15);
        infoGrid.setVgap(15);
        infoGrid.setAlignment(Pos.CENTER);
        infoGrid.setStyle("-fx-padding: 20; -fx-background-color: #ecf0f1; -fx-background-radius: 10;");

        Label nationalityTitleLabel = new Label("Nationalité:");
        nationalityTitleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        Label nationalityValueLabel = new Label(actor.getNationality() != null ? actor.getNationality() : "N/A");
        nationalityValueLabel.setStyle("-fx-font-size: 14px;");

        Label ageTitleLabel = new Label("Âge:");
        ageTitleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        Label ageValueLabel = new Label(actor.getAge() + " ans");
        ageValueLabel.setStyle("-fx-font-size: 14px;");

        infoGrid.add(nationalityTitleLabel, 0, 0);
        infoGrid.add(nationalityValueLabel, 1, 0);
        infoGrid.add(ageTitleLabel, 0, 1);
        infoGrid.add(ageValueLabel, 1, 1);

        // Biographie
        Label bioTitleLabel = new Label("Biographie");
        bioTitleLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        bioTitleLabel.setStyle("-fx-text-fill: #2c3e50;");

        TextArea bioTextArea = new TextArea(actor.getBiography() != null ? actor.getBiography() : "Aucune biographie disponible");
        bioTextArea.setEditable(false);
        bioTextArea.setWrapText(true);
        bioTextArea.setPrefHeight(200);
        bioTextArea.setStyle("-fx-font-size: 13px; -fx-background-color: #f8f9fa; -fx-border-color: #e0e0e0; -fx-border-radius: 5;");

        // Bouton retour
        Button backButton = new Button("← Retour");
        backButton.setStyle(
            "-fx-background-color: #95a5a6; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 10 30; " +
            "-fx-background-radius: 5; " +
            "-fx-cursor: hand;"
        );
        backButton.setOnAction(e -> {
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.close();
        });

        layout.getChildren().addAll(
            photoLabel,
            nameLabel,
            infoGrid,
            bioTitleLabel,
            bioTextArea,
            backButton
        );

        return layout;
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfoMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}