package Services;

import BuisnessClasses.Notification;
import Persistence.DAOFactory;
import Persistence.NotificationDAO;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class NotificationManagement {
    private NotificationDAO notificationDAO;

    public NotificationManagement() {
        try {
            this.notificationDAO = DAOFactory.getInstance().createNotificationDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Notification> getAllNotifications() {
        try {
            return notificationDAO.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public boolean createNotification(String message) {
        try {
            // L'ID et la Date sont gérés par la base de données
            Notification notif = new Notification(0, message, null);
            return notificationDAO.create(notif);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}