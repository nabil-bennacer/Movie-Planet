
package Persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 
 */
public abstract class DAOFactory {

    private static DAOFactory instance = null;
    private static Connection connection = null;

    public static DAOFactory getInstance() {
        if (instance == null) {
            instance = new PostgresFactory();
        }
        return instance;
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            String url = "jdbc:postgresql://ep-morning-wind-agpw3rb0-pooler.c-2.eu-central-1.aws.neon.tech:5432/neondb?sslmode=require";
            String user = "neondb_owner";
            String password = "npg_NXDSbcf26hVt";
            connection = DriverManager.getConnection(url, user, password);
        }
        return connection;
    }

    public abstract UserDAO createUserDAO() throws SQLException;
    public abstract ArticleDAO createArticleDAO() throws SQLException;
    public abstract CommentaireDAO createCommentaireDAO() throws SQLException;
    public abstract MovieDAO createMovieDAO() throws SQLException;
    public abstract NotificationDAO createNotificationDAO() throws SQLException;
}