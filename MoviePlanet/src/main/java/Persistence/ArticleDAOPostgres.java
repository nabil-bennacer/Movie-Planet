package Persistence;

import BuisnessClasses.Article;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArticleDAOPostgres implements ArticleDAO {
    private Connection dbConnection;

    public ArticleDAOPostgres() throws SQLException {
        this.dbConnection = DAOFactory.getConnection();
    }


    @Override
    public List<Article> findAll() throws SQLException {
        List<Article> articles = new ArrayList<>();
        String query = "SELECT * FROM articles";

        try (Statement stmt = dbConnection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                articles.add(mapResultSetToArticle(rs));
            }
        }
        return articles;
    }

    @Override
    public Article findById(int id) throws SQLException {
        String query = "SELECT * FROM articles WHERE id = ?";

        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToArticle(rs);
                }
            }
        }
        return null;
    }

    @Override
    public void update(Article article) throws SQLException {
        // Mise à jour complète (utile pour le stock et les modifs admin)
        String query = "UPDATE articles SET nom=?, prix=?, description=?, image_url=?, stock=?, film_lie=? WHERE id=?";

        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setString(1, article.getNom());
            stmt.setDouble(2, article.getPrix());
            stmt.setString(3, article.getDescription());
            stmt.setString(4, article.getImageUrl());
            stmt.setInt(5, article.getStock());
            stmt.setString(6, article.getFilmLie());
            stmt.setInt(7, article.getId());

            stmt.executeUpdate();
        }
    }

    @Override
    public void create(Article article) throws SQLException {
        String query = "INSERT INTO articles (nom, prix, description, image_url, stock, film_lie) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = dbConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, article.getNom());
            stmt.setDouble(2, article.getPrix());
            stmt.setString(3, article.getDescription());
            stmt.setString(4, article.getImageUrl());
            stmt.setInt(5, article.getStock());
            stmt.setString(6, article.getFilmLie());

            stmt.executeUpdate();

            // Récupérer l'ID généré pour mettre à jour l'objet Java
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    article.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM articles WHERE id = ?";

        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Méthode utilitaire pour éviter de répéter le code de mapping
    private Article mapResultSetToArticle(ResultSet rs) throws SQLException {
        return new Article(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getDouble("prix"),
                rs.getString("description"),
                rs.getString("image_url"),
                rs.getInt("stock"),
                rs.getString("film_lie")
        );
    }
}