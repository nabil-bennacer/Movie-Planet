package BuisnessClasses;

public class Order {
    private int id;
    private int userId;
    private double totalAmount;
    private String paymentMethod;
    private String status;

    public Order(int userId, double totalAmount, String paymentMethod, String status) {
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }

    // Getters
    public int getUserId() { return userId; }
    public double getTotalAmount() { return totalAmount; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getStatus() { return status; }
}