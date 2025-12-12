package org.example.javafx;

import java.sql.*;
import org.example.javafx.User;

public class UserDAOPostgres implements UserDAO {
    private Connection dbConnection;

    public UserDAOPostgres(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public User findUserByUsername(String username) throws SQLException {
        String query = "SELECT id, password, nom, email FROM users WHERE username = ?";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet result = stmt.executeQuery()) {
                if (result.next()) {
                    return new User(result.getInt("id"), result.getString("password"), result.getString("nom"), result.getString("email"));
                }
            }
        }
        return null;
    }
}