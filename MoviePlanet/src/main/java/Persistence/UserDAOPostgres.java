package Persistence;

import java.sql.*;
import BuisnessClasses.User;
import java.util.ArrayList;
import java.util.List;

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

    }


    @Override
    public User findUserByUsername(String username) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ?";

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
        // Le rôle par défaut est géré soit ici, soit par la BDD.
        // Ici on force "User" si ce n'est pas spécifié, ou on laisse la BDD faire.
        String query = "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, "User"); // Rôle par défaut à l'inscription
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public List<User> findAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        try (Statement stmt = dbConnection.createStatement();
             ResultSet result = stmt.executeQuery(query)) {
            while (result.next()) {
                users.add(new User(
                        result.getInt("id"),
                        result.getString("username"),
                        result.getString("password"),
                        result.getString("email"),
                        result.getString("role")
                ));
            }
        }
        return users;
    }
    @Override
    public boolean deleteUser(int userId) throws SQLException {
        String query = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        }
    }
}