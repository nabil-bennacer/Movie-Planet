package Persistence;

import BuisnessClasses.Notification;
import java.sql.SQLException;
import java.util.List;

public interface NotificationDAO {
    List<Notification> findAll() throws SQLException;
    boolean create(Notification notification) throws SQLException;
}