package Facades;

import java.util.List;

import BuisnessClasses.Provider;
import BuisnessClasses.User; 
import BuisnessClasses.CartItem; 
import Services.BasketManagement; 
import Services.ProviderManagement;
import Services.UserManagement;
import BuisnessClasses.PaymentInfo; 
import Services.PaymentManagement;  



public class SessionFacade {
    private static SessionFacade instance;
    private UserManagement userManager;
    private ProviderManagement providerManager;
    private BasketManagement basketManager;
    private User currentUser;
    private PaymentManagement paymentManager;
    

    private SessionFacade() {
        this.userManager = new UserManagement();
        this.providerManager = new ProviderManagement();
        this.basketManager = new BasketManagement();
        this.paymentManager = new PaymentManagement();
    }

    public static SessionFacade getInstance() {
        if (instance == null) {
            instance = new SessionFacade();
        }
        return instance;
    }

    public boolean login(String username, String password) {
        if (userManager == null) { return false; }
        boolean success = userManager.login(username, password);
        if (success) {
            this.currentUser = userManager.getUserByUsername(username); 
        }
        return success;
    }

    public User getCurrentUser() {
        return currentUser;
    }
    
    public void logout() {
        this.currentUser = null;
    }

    // Méthodes pour Provider Management
    public void createProvider(String nom, String contactNom, String email, String numeroTel,
                               double prixAbonnement, String logoUrl, String siteUrl, String description) {
        providerManager.createProvider(nom, contactNom, email, numeroTel,
                prixAbonnement, logoUrl, siteUrl, description);
    }

    public List<Provider> getAllProviders() {
        return providerManager.getAllProviders();
    }

    public void updateProvider(Provider provider) {
        providerManager.updateProvider(provider);
    }

    public void deleteProvider(int id) {
        providerManager.deleteProvider(id);
    }

    // --- Basket Management Methods ---

    public List<CartItem> getBasketItems() {
        if (currentUser == null) return null;
        return basketManager.getBasketItems(currentUser.getId());
    }

    public void removeBasketItem(CartItem item) {
        basketManager.removeItem(item.getId());
    }

    public void updateBasketItemQuantity(CartItem item, int qty) {
        basketManager.updateQuantity(item.getId(), qty);
    }
    
    public double getBasketTotal(List<CartItem> items) {
        return basketManager.calculateTotal(items);
    }

    // --- Payment Methods ---
    public boolean processPayment(PaymentInfo info) {
        if (currentUser == null) return false;
        // On délègue au service
        return paymentManager.processPayment(currentUser.getId(), info);
    }


}