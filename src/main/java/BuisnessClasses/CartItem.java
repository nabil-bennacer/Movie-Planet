package BuisnessClasses;

public class CartItem {
    private int id;
    private int userId; // Lien vers l'utilisateur
    private String productName;
    private double unitPrice;
    private int quantity;

    public CartItem(int id, int userId, String productName, double unitPrice, int quantity) {
        this.id = id;
        this.userId = userId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    // Constructeur sans ID pour la création avant insertion DB
    public CartItem(int userId, String productName, double unitPrice, int quantity) {
        this.userId = userId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getProductName() { return productName; }
    public double getUnitPrice() { return unitPrice; }
    public int getQuantity() { return quantity; }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Méthode calculée
    public double getTotalPrice() {
        return this.unitPrice * this.quantity;
    }
}