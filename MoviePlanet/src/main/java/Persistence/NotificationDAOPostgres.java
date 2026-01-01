package Persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import BuisnessClasses.Notification;

public class NotificationDAOPostgres implements NotificationDAO {
    private Connection dbConnection;

    public NotificationDAOPostgres() throws SQLException {
        this.dbConnection = DAOFactory.getConnection();
    }

    @Override
    public List<Notification> findAll() throws SQLException {
        List<Notification> notifs = new ArrayList<>();
        // On récupère les plus récentes en premier
        String query = "SELECT * FROM notifications ORDER BY created_at DESC";

        try (Statement stmt = dbConnection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                notifs.add(new Notification(
                        rs.getInt("id"),
                        rs.getString("message"),
                        rs.getTimestamp("created_at")
                ));
            }
        }
        return notifs;
    }

    @Override
    public boolean create(Notification notification) throws SQLException {
        String query = "INSERT INTO notifications (message) VALUES (?)";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setString(1, notification.getMessage());
            return stmt.executeUpdate() > 0;
        }
    }
}