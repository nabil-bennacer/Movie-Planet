package org.example.javafx;

import java.sql.*;
import org.example.javafx.User;

public class UserDAOPostgres implements UserDAO {
    private Connection dbConnection;

    public UserDAOPostgres() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/movieplanet";
        String user = "movieplanet";
        String password = "MoviePlanet";
        this.dbConnection = DriverManager.getConnection(url, user, password);

        initializeDatabase();
    }

    private void initializeDatabase() throws SQLException {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS users (" +
                "id SERIAL PRIMARY KEY, " +
                "username VARCHAR(255) UNIQUE, " +
                "password VARCHAR(255), " +
                "nom VARCHAR(255), " +
                "email VARCHAR(255))";
        try (Statement stmt = dbConnection.createStatement()) {
            stmt.execute(createTableQuery);
        }
    }

    @Override
    public User findUserByUsername(String username) throws SQLException {
        String query = "SELECT id, password, nom, email FROM users WHERE username = ?";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet result = stmt.executeQuery()) {
                if (result.next()) {
                    return new User(
                            result.getInt("id"),
                            result.getString("password"),
                            result.getString("nom"),
                            result.getString("email")
                    );
                }
            }
        }
        return null;
    }
}