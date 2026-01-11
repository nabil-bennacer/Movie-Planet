package BuisnessClasses;

public class PaymentInfo {
    private String method; // "Carte Bancaire", "PayPal"
    private String cardNumber;
    private String cvv;
    private String expiryDate;

    public PaymentInfo(String method, String cardNumber, String cvv, String expiryDate) {
        this.method = method;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expiryDate = expiryDate;
    }

    public String getMethod() { return method; }
    public String getCardNumber() { return cardNumber; }
    public String getCvv() { return cvv; }
    public String getExpiryDate() { return expiryDate; }
}