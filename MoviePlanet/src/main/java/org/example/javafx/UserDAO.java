package org.example.javafx;
import java.sql.SQLException;

//Retourne un objet de type org.example.javafx.User (l'utilisateur correspondant à cet ID).
//Peut lever une exception SQLException si un problème survient lors de l'accès à la base de données.
public interface UserDAO {
    User findUserByUsername(String username) throws SQLException;
}