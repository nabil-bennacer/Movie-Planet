package Persistence;

import java.sql.SQLException;

/**
 * 
 */
public class PostgresFactory extends DAOFactory {

    /**
     * @return
     */
    @Override
    public UserDAO createUserDAO()  {
        try {
            return new UserDAOPostgres();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArticleDAO createArticleDAO() {
        try {
            return new ArticleDAOPostgres();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}