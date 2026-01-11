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
    public MovieDAO createMovieDAO(){
        try{
            return new MovieDAOPostgres();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public NotificationDAO createNotificationDAO(){
        try{
            return new NotificationDAOPostgres();
        }
        catch (SQLException e){
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

    @Override
    public CommentaireDAO createCommentaireDAO() {
        try {
            return new CommentaireDAOPostgres();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ActorDAO createActorDAO() {
        try {
            return new ActorDAOPostgres();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}