package Persistence;

import BuisnessClasses.Order;
import java.sql.SQLException;

public interface OrderDAO {
    void createOrder(Order order) throws SQLException;
}