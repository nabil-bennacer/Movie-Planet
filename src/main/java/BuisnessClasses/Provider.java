package BuisnessClasses;

public class Provider {
    private int id;
    private String nom;
    private String contactNom;
    private String email;
    private String numeroTel;
    private double prixAbonnement;
    private String logoUrl;
    private String siteUrl;
    private String description;

    public Provider(int id, String nom, String contactNom, String email, String numeroTel,
                    double prixAbonnement, String logoUrl, String siteUrl, String description) {
        this.id = id;
        this.nom = nom;
        this.contactNom = contactNom;
        this.email = email;
        this.numeroTel = numeroTel;
        this.prixAbonnement = prixAbonnement;
        this.logoUrl = logoUrl;
        this.siteUrl = siteUrl;
        this.description = description;
    }

    // Getters
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getContactNom() { return contactNom; }
    public String getEmail() { return email; }
    public String getNumeroTel() { return numeroTel; }
    public double getPrixAbonnement() { return prixAbonnement; }
    public String getLogoUrl() { return logoUrl; }
    public String getSiteUrl() { return siteUrl; }
    public String getDescription() { return description; }

    // Setters (pour les modifications)
    public void setNom(String nom) { this.nom = nom; }
    public void setContactNom(String contactNom) { this.contactNom = contactNom; }
    public void setEmail(String email) { this.email = email; }
    public void setNumeroTel(String numeroTel) { this.numeroTel = numeroTel; }
    public void setPrixAbonnement(double prixAbonnement) { this.prixAbonnement = prixAbonnement; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
    public void setSiteUrl(String siteUrl) { this.siteUrl = siteUrl; }
    public void setDescription(String description) { this.description = description; }
}
