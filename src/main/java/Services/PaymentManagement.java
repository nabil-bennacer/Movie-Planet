package Services;

import BuisnessClasses.Order;
import BuisnessClasses.PaymentInfo;
import BuisnessClasses.CartItem;
import Persistence.BasketDAO;
import Persistence.DAOFactory;
import Persistence.OrderDAO;

import java.sql.SQLException;
import java.util.List;

public class PaymentManagement {

    private OrderDAO orderDAO;
    private BasketDAO basketDAO;

    public PaymentManagement() {
        try {
            this.orderDAO = DAOFactory.getInstance().createOrderDAO();
            this.basketDAO = DAOFactory.getInstance().createBasketDAO();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur initialisation DAO Payment", e);
        }
    }

    
    public boolean validatePaymentInfo(PaymentInfo info) {
        if (info == null) return false;
        
        return !info.getCardNumber().isEmpty() && !info.getMethod().isEmpty();
    }

    public boolean executeTransaction(double amount, String provider) {
        
        System.out.println("Transaction de " + amount + "€ via " + provider + " approuvée.");
        return true; 
    }

    public boolean processPayment(int userId, PaymentInfo info) {
        try {
            // 1. Valider les infos
            if (!validatePaymentInfo(info)) return false;

            // 2. Récupérer le montant total réel depuis le panier
            List<CartItem> items = basketDAO.findItemsByUserId(userId);
            if (items.isEmpty()) return false;

            double totalAmount = items.stream().mapToDouble(CartItem::getTotalPrice).sum();

            // 3. Exécuter la transaction financière
            if (executeTransaction(totalAmount, info.getMethod())) {
                
                // 4. Créer l'objet Commande
                Order newOrder = new Order(userId, totalAmount, info.getMethod(), "PAID");
                
                // 5. Sauvegarder la commande (DAO)
                orderDAO.createOrder(newOrder);
                
                // 6. Vider le panier (DAO)
                basketDAO.clearBasket(userId);
                
                System.out.println("Email de confirmation envoyé à l'utilisateur.");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}