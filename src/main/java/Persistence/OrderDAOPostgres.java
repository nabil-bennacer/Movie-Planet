package Persistence;

import BuisnessClasses.Order;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderDAOPostgres implements OrderDAO {
    private Connection dbConnection;

    public OrderDAOPostgres() throws SQLException {
        this.dbConnection = DriverManager.getConnection(
            DatabaseConfig.getUrl(),
            DatabaseConfig.getUser(),
            DatabaseConfig.getPassword()
        );
    }

    @Override
    public void createOrder(Order order) throws SQLException {
        String query = "INSERT INTO orders (user_id, total_amount, payment_method, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, order.getUserId());
            stmt.setDouble(2, order.getTotalAmount());
            stmt.setString(3, order.getPaymentMethod());
            stmt.setString(4, order.getStatus());
            stmt.executeUpdate();
        }
    }
}