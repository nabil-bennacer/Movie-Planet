package Persistence;

import BuisnessClasses.Commentaire;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentaireDAOPostgres implements CommentaireDAO {
    private Connection dbConnection;

    public CommentaireDAOPostgres() throws SQLException {
        this.dbConnection = DAOFactory.getConnection();
    }

    @Override
    public List<Commentaire> findByArticleId(int articleId) throws SQLException {
        List<Commentaire> commentaires = new ArrayList<>();
        // Jointure pour récupérer le nom de l'utilisateur directement
        String query = "SELECT c.*, u.username FROM commentaires c " +
                "JOIN users u ON c.user_id = u.id " +
                "WHERE c.article_id = ? ORDER BY c.date_publication DESC";

        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, articleId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Commentaire c = new Commentaire(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getInt("article_id"),
                            rs.getString("texte"),
                            rs.getInt("note"),
                            rs.getTimestamp("date_publication")
                    );
                    c.setNomAuteur(rs.getString("username"));
                    commentaires.add(c);
                }
            }
        }
        return commentaires;
    }

    @Override
    public void create(Commentaire c) throws SQLException {
        String query = "INSERT INTO commentaires (user_id, article_id, texte, note, date_publication) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";

        try (PreparedStatement stmt = dbConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, c.getUserId());
            stmt.setInt(2, c.getArticleId());
            stmt.setString(3, c.getTexte());
            stmt.setInt(4, c.getNote());

            stmt.executeUpdate();
        }
    }
}