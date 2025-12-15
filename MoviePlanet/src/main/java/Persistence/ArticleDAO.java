package Persistence;
import BuisnessClasses.Article;

import java.sql.SQLException;
import java.util.List;

public interface ArticleDAO {
    List<Article> findAll() throws SQLException;
    Article findById(int id) throws SQLException;
    void update(Article article) throws SQLException;
    // On peut ajouter create/delete si besoin pour la partie Admin
    void create(Article article) throws SQLException;

    void delete(int id) throws SQLException;
}