package Persistence;

import BuisnessClasses.CartItem;
import java.sql.SQLException;
import java.util.List;

public interface BasketDAO {
    List<CartItem> findItemsByUserId(int userId) throws SQLException;
    void deleteItem(int itemId) throws SQLException;
    void updateQuantity(int itemId, int quantity) throws SQLException;
    void addItem(CartItem item) throws SQLException; 
    void clearBasket(int userId) throws SQLException;
}