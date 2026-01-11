package Services;

import BuisnessClasses.CartItem;
import Persistence.BasketDAO;
import Persistence.DAOFactory;

import java.sql.SQLException;
import java.util.List;

public class BasketManagement {
    
    private BasketDAO basketDAO;

    public BasketManagement() {
        try {
            this.basketDAO = DAOFactory.getInstance().createBasketDAO();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création du BasketDAO", e);
        }
    }

    public List<CartItem> getBasketItems(int userId) {
        try {
            return basketDAO.findItemsByUserId(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void removeItem(int itemId) {
        try {
            basketDAO.deleteItem(itemId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateQuantity(int itemId, int quantity) {
        try {
            if (quantity > 0) {
                basketDAO.updateQuantity(itemId, quantity);
            } else {
                // Si quantité 0 ou moins, on supprime l'article 
                basketDAO.deleteItem(itemId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double calculateTotal(List<CartItem> items) {
        double total = 0.0;
        if (items != null) {
            for (CartItem item : items) {
                total += item.getTotalPrice();
            }
        }
        return total;
    }
    
    public boolean validateStock(int itemId, int qty) {
        // Simulation: on retourne true pour l'instant
        // À connecter avec un InventoryDAO plus tard
        return true; 
    }
}