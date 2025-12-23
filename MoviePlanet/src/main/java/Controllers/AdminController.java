package Controllers;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;

public class AdminController {

    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, Integer> idColumn;
    @FXML private TableColumn<User, String> usernameColumn;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private TableColumn<User, String> roleColumn;

    @FXML
    public void initialize() {
        // Configuration des colonnes
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        refreshTable();
    }

    private void refreshTable() {
        if (SessionFacade.getInstance().getAllUsers() != null) {
            ObservableList<User> users = FXCollections.observableArrayList(
                    SessionFacade.getInstance().getAllUsers()
            );
            userTable.setItems(users);
        }
    }

    @FXML
    public void handleDeleteUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            boolean success = SessionFacade.getInstance().deleteUser(selectedUser.getId());
            if (success) {
                refreshTable();
            }
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
}