package BuisnessClasses;

import java.sql.Timestamp;

public class Commentaire {
    private int id;
    private int userId;
    private int articleId;
    private String texte;
    private int note;
    private Timestamp datePublication;

    // Attributs supplémentaires pour l'affichage (optionnel, ex: nom de l'auteur)
    private String nomAuteur;

    public Commentaire(int id, int userId, int articleId, String texte, int note, Timestamp datePublication) {
        this.id = id;
        this.userId = userId;
        this.articleId = articleId;
        this.texte = texte;
        this.note = note;
        this.datePublication = datePublication;
    }

    // Constructeur simplifié pour la création
    public Commentaire(int userId, int articleId, String texte, int note) {
        this.userId = userId;
        this.articleId = articleId;
        this.texte = texte;
        this.note = note;
    }

    // Getters et Setters
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public int getArticleId() { return articleId; }
    public String getTexte() { return texte; }
    public int getNote() { return note; }
    public Timestamp getDatePublication() { return datePublication; }

    public String getNomAuteur() { return nomAuteur; }
    public void setNomAuteur(String nomAuteur) { this.nomAuteur = nomAuteur; }

    @Override
    public String toString() {
        return (nomAuteur != null ? nomAuteur : "User " + userId) + " (" + note + "/5) : " + texte;
    }
}