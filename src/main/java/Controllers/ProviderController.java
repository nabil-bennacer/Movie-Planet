package Controllers;

import BuisnessClasses.Provider;
import Facades.SessionFacade;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.IOException;

public class ProviderController {
    private SessionFacade sessionFacade;
    private Provider selectedProvider;

    @FXML private TextField nomField;
    @FXML private TextField contactNomField;
    @FXML private TextField emailField;
    @FXML private TextField numeroTelField;
    @FXML private TextField prixAbonnementField;
    @FXML private TextField logoUrlField;
    @FXML private TextField siteUrlField;
    @FXML private TextArea descriptionArea;
    @FXML private Label messageLabel;
    @FXML private TableView<Provider> providersTable;
    @FXML private TableColumn<Provider, Provider> logoColumn; // Changé en Provider pour cellule personnalisée
    @FXML private TableColumn<Provider, String> nomColumn;
    @FXML private TableColumn<Provider, String> emailColumn;
    @FXML private TableColumn<Provider, Double> prixColumn;

    private ObservableList<Provider> providersList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        this.sessionFacade = SessionFacade.getInstance();
        
        System.out.println("DEBUG: Initialisation du contrôleur");
        
        // Configuration de la colonne Logo avec cellule personnalisée
        logoColumn.setCellValueFactory(cellData -> {
            System.out.println("DEBUG: CellValueFactory appelée pour: " + cellData.getValue().getNom());
            return new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue());
        });
        
        logoColumn.setCellFactory(column -> new TableCell<Provider, Provider>() {
            private final ImageView imageView = new ImageView();
            private final HBox hbox = new HBox(imageView);

            {
                imageView.setFitHeight(40);
                imageView.setFitWidth(40);
                imageView.setPreserveRatio(true);
                hbox.setAlignment(javafx.geometry.Pos.CENTER);
            }

            @Override
            protected void updateItem(Provider provider, boolean empty) {
                super.updateItem(provider, empty);
                
                if (empty || provider == null) {
                    setGraphic(null);
                } else {
                    String logoUrl = provider.getLogoUrl();
                    
                    if (logoUrl == null || logoUrl.isEmpty()) {
                        Label placeholder = new Label("📷");
                        placeholder.setStyle("-fx-font-size: 20px;");
                        setGraphic(placeholder);
                    } else {
                        // Afficher un loader pendant le chargement
                        Label loadingLabel = new Label("⏳");
                        loadingLabel.setStyle("-fx-font-size: 20px;");
                        setGraphic(loadingLabel);
                        
                        // Charger l'image dans un thread séparé
                        javafx.concurrent.Task<Image> loadTask = new javafx.concurrent.Task<>() {
                            @Override
                            protected Image call() throws Exception {
                                System.out.println("📥 Chargement de: " + logoUrl);
                                return new Image(logoUrl, 40, 40, true, true, true);
                            }
                        };
                        
                        loadTask.setOnSucceeded(event -> {
                            Image image = loadTask.getValue();
                            if (image != null && !image.isError() && image.getWidth() > 0) {
                                imageView.setImage(image);
                                setGraphic(hbox);
                                System.out.println("✅ Logo chargé avec succès: " + provider.getNom());
                            } else {
                                Label errorLabel = new Label("❌");
                                errorLabel.setStyle("-fx-font-size: 20px;");
                                setGraphic(errorLabel);
                                System.err.println("❌ Image invalide pour: " + provider.getNom());
                            }
                        });
                        
                        loadTask.setOnFailed(event -> {
                            Label errorLabel = new Label("❌");
                            errorLabel.setStyle("-fx-font-size: 20px;");
                            setGraphic(errorLabel);
                            Throwable exception = loadTask.getException();
                            System.err.println("❌ Échec du chargement: " + provider.getNom() + " - " + 
                                (exception != null ? exception.getMessage() : "Unknown error"));
                        });
                        
                        // Lancer le chargement dans un thread séparé
                        Thread loadThread = new Thread(loadTask);
                        loadThread.setDaemon(true);
                        loadThread.start();
                    }
                }
            }
        });
        
        // Configuration des autres colonnes
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prixAbonnement"));
        
        // Listener pour la sélection
        providersTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    fillFormWithProvider(newValue);
                }
            }
        );
        
        loadProviders();
        System.out.println("DEBUG: Fin de l'initialisation, nombre de providers: " + providersList.size());
    }

    private void loadProviders() {
        providersList.clear();
        providersList.addAll(sessionFacade.getAllProviders());
        providersTable.setItems(providersList);
    }

    @FXML
    protected void onAddProvider() {
        try {
            String nom = nomField.getText();
            String contactNom = contactNomField.getText();
            String email = emailField.getText();
            String numeroTel = numeroTelField.getText();
            double prix = Double.parseDouble(prixAbonnementField.getText());
            String logo = logoUrlField.getText();
            String site = siteUrlField.getText();
            String desc = descriptionArea.getText();

            sessionFacade.createProvider(nom, contactNom, email, numeroTel, prix, logo, site, desc);
            
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Provider ajouté avec succès!");
            clearFields();
            loadProviders();
        } catch (Exception e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Erreur: " + e.getMessage());
        }
    }

    @FXML
    protected void onUpdateProvider() {
        if (selectedProvider == null) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Veuillez sélectionner un provider à modifier");
            return;
        }

        try {
            selectedProvider.setNom(nomField.getText());
            selectedProvider.setContactNom(contactNomField.getText());
            selectedProvider.setEmail(emailField.getText());
            selectedProvider.setNumeroTel(numeroTelField.getText());
            selectedProvider.setPrixAbonnement(Double.parseDouble(prixAbonnementField.getText()));
            selectedProvider.setLogoUrl(logoUrlField.getText());
            selectedProvider.setSiteUrl(siteUrlField.getText());
            selectedProvider.setDescription(descriptionArea.getText());

            sessionFacade.updateProvider(selectedProvider);
            
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Provider modifié avec succès!");
            clearFields();
            loadProviders();
        } catch (Exception e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Erreur: " + e.getMessage());
        }
    }

    @FXML
    protected void onDeleteProvider() {
        Provider selected = providersTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            sessionFacade.deleteProvider(selected.getId());
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Provider supprimé!");
            clearFields();
            loadProviders();
        } else {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Veuillez sélectionner un provider à supprimer");
        }
    }

    @FXML
    protected void onClearFields() {
        clearFields();
    }

    @FXML
    protected void onBackToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/dashboard-view.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            Stage stage = (Stage) nomField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Tableau de bord - MoviePlanet");
            stage.show();
        } catch (IOException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Erreur de navigation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void fillFormWithProvider(Provider provider) {
        this.selectedProvider = provider;
        nomField.setText(provider.getNom());
        contactNomField.setText(provider.getContactNom());
        emailField.setText(provider.getEmail());
        numeroTelField.setText(provider.getNumeroTel());
        prixAbonnementField.setText(String.valueOf(provider.getPrixAbonnement()));
        logoUrlField.setText(provider.getLogoUrl());
        siteUrlField.setText(provider.getSiteUrl());
        descriptionArea.setText(provider.getDescription());
        
        messageLabel.setStyle("-fx-text-fill: blue;");
        messageLabel.setText("Mode modification - Provider sélectionné: " + provider.getNom());
    }

    private void clearFields() {
        this.selectedProvider = null;
        nomField.clear();
        contactNomField.clear();
        emailField.clear();
        numeroTelField.clear();
        prixAbonnementField.clear();
        logoUrlField.clear();
        siteUrlField.clear();
        descriptionArea.clear();
        messageLabel.setText("");
        providersTable.getSelectionModel().clearSelection();
    }
}
