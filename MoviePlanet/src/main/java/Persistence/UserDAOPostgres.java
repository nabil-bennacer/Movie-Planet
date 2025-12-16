package Persistence;

import java.sql.*;
import BuisnessClasses.User;

public class UserDAOPostgres implements UserDAO {
    private Connection dbConnection;

    public UserDAOPostgres() throws SQLException {
//        String url = "jdbc:postgresql://localhost:5432/movieplanet";
//        String user = "movieplanet";
//        String password = "MoviePlanet";
//        String url = System.getenv("DB_URL");
//        String user = System.getenv("DB_USER");
//        String password = System.getenv("DB_PASSWORD");
        String url = "jdbc:postgresql://ep-morning-wind-agpw3rb0-pooler.c-2.eu-central-1.aws.neon.tech:5432/neondb?sslmode=require";
        String user = "neondb_owner";
        String password = "npg_NXDSbcf26hVt";
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