package Persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import BuisnessClasses.User;

public class UserDAOPostgres implements UserDAO {
    private Connection dbConnection;

    public UserDAOPostgres() throws SQLException {
        this.dbConnection = DAOFactory.getConnection();
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