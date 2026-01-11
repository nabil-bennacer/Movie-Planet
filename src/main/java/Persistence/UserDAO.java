package Persistence;
import java.sql.SQLException;

import BuisnessClasses.User;

//Retourne un objet de type BuisnessClasses.User (l'utilisateur correspondant à cet ID).
//Peut lever une exception SQLException si un problème survient lors de l'accès à la base de données.
public interface UserDAO {
    User findUserByUsername(String username) throws SQLException;
    boolean createUser(User user) throws SQLException;
}