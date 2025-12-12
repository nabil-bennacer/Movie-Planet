package Model;
//DAO = Data Access Object
import java.sql.SQLException;

//Retourne un objet de type User (l'utilisateur correspondant à cet ID).
//Peut lever une exception SQLException si un problème survient lors de l'accès à la base de données.
public interface userDAO {
    User findUserById(int id) throws SQLException;

}