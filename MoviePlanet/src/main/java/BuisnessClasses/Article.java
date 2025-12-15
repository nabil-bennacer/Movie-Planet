package BuisnessClasses;

public class Article {
    private int id;
    private String nom;
    private double prix;
    private String description;
    private String imageUrl;
    private int stock;
    private String filmLie;

    public Article(int id, String nom, double prix, String description, String imageUrl, int stock, String filmLie) {
        this.id = id;
        this.nom = nom;
        this.prix = prix;
        this.description = description;
        this.imageUrl = imageUrl;
        this.stock = stock;
        this.filmLie = filmLie;
    }

    // Getters
    public int getId() { return id; }
    public String getNom() { return nom; }
    public double getPrix() { return prix; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public int getStock() { return stock; }
    public String getFilmLie() { return filmLie; }

    // Setters
    public void setStock(int stock) { this.stock = stock; }
    public void setId(int id) { this.id = id; }

    // Méthodes métier
    public boolean estEnRupture() {
        return this.stock <= 0;
    }
}