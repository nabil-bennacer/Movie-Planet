package Persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import BuisnessClasses.User;

public class UserDAOPostgres implements UserDAO {
    private Connection dbConnection;

    public UserDAOPostgres() throws SQLException {
        // Connexion à Neon via DatabaseConfig
        this.dbConnection = DriverManager.getConnection(
            DatabaseConfig.getUrl(),
            DatabaseConfig.getUser(),
            DatabaseConfig.getPassword()
        );
    }

    @Override
    public User findUserByUsername(String username) throws SQLException {
        String query = "SELECT id, username, password, email, role FROM users WHERE username = ?";
        
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setString(1, username);
            
            try (ResultSet result = stmt.executeQuery()) {
                if (result.next()) {
                    return new User(
                            result.getInt("id"),
                            result.getString("username"),
                            result.getString("password"),
                            result.getString("email"),
                            result.getString("role")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public boolean createUser(User user) throws SQLException {
        String query = "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getRole() != null ? user.getRole() : "User");
            return stmt.executeUpdate() > 0;
        }
    }
}