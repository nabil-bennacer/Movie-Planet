package Persistence;

import java.sql.*;
import BuisnessClasses.User;

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
                "username VARCHAR(255) UNIQUE NOT NULL, " +
                "password VARCHAR(255) NOT NULL, " +
                "nom VARCHAR(255), " +
                "email VARCHAR(255), " +
                "role VARCHAR(20) NOT NULL DEFAULT 'visitor' CHECK (role IN ('admin', 'visitor')))";

        try (Statement stmt = dbConnection.createStatement()) {
            stmt.execute(createTableQuery);
        }
    }

    @Override
    public User findUserByUsername(String username) throws SQLException {
        String query = "SELECT id, password, nom, email, role FROM users WHERE username = ?";

        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet result = stmt.executeQuery()) {
                if (result.next()) {
                    return new User(
                            result.getInt("id"),
                            result.getString("password"),
                            result.getString("nom"),
                            result.getString("email"),
                            result.getString("role")
                    );
                }
            }
        }
        return null;
    }
}