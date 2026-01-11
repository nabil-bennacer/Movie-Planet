package Persistence;

import BuisnessClasses.CartItem;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BasketDAOPostgres implements BasketDAO {
    private Connection dbConnection;

    public BasketDAOPostgres() throws SQLException {
        this.dbConnection = DriverManager.getConnection(
            DatabaseConfig.getUrl(),
            DatabaseConfig.getUser(),
            DatabaseConfig.getPassword()
        );
    }

    @Override
    public List<CartItem> findItemsByUserId(int userId) throws SQLException {
        List<CartItem> items = new ArrayList<>();
        String query = "SELECT * FROM cart_items WHERE user_id = ?";
        
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    items.add(new CartItem(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("product_name"),
                        rs.getDouble("unit_price"),
                        rs.getInt("quantity")
                    ));
                }
            }
        }
        return items;
    }

    @Override
    public void deleteItem(int itemId) throws SQLException {
        String query = "DELETE FROM cart_items WHERE id = ?";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, itemId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateQuantity(int itemId, int quantity) throws SQLException {
        String query = "UPDATE cart_items SET quantity = ? WHERE id = ?";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, itemId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void addItem(CartItem item) throws SQLException {
        String query = "INSERT INTO cart_items (user_id, product_name, unit_price, quantity) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, item.getUserId());
            stmt.setString(2, item.getProductName());
            stmt.setDouble(3, item.getUnitPrice());
            stmt.setInt(4, item.getQuantity());
            stmt.executeUpdate();
        }
    }
    public void clearBasket(int userId) throws SQLException {
        String query = "DELETE FROM cart_items WHERE user_id = ?";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }
}