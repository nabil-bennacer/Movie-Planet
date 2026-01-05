package Persistence;

import BuisnessClasses.Commentaire;
import java.sql.SQLException;
import java.util.List;

public interface CommentaireDAO {
    List<Commentaire> findByArticleId(int articleId) throws SQLException;
    void create(Commentaire commentaire) throws SQLException;
}