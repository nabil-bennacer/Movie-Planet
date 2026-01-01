package Controllers;

import BuisnessClasses.Notification;
import Facades.SessionFacade;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class NotificationController {

    @FXML
    private ListView<String> notificationList;

    @FXML
    public void initialize() {
        loadNotifications();
    }

    private void loadNotifications() {
        var notifs = SessionFacade.getInstance().getNotifications();
        var messages = FXCollections.<String>observableArrayList();

        for (Notification n : notifs) {
            // Format : [Date] Message
            messages.add("[" + n.getDate().toString() + "]\n" + n.getMessage());
        }

        notificationList.setItems(messages);
    }

    @FXML
    public void closeWindow() {
        Stage stage = (Stage) notificationList.getScene().getWindow();
        stage.close();
    }
}