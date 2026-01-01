package Persistence;
import BuisnessClasses.User;
import java.util.List;
import java.sql.SQLException;


public interface UserDAO {
    User findUserByUsername(String username) throws SQLException;

    // Pour l'inscription
    boolean createUser(User user) throws SQLException;

    // Pour la suppression (Admin ou User lui-même)
    boolean deleteUser(int userId) throws SQLException;

    // Pour que l'Admin puisse voir la liste des comptes
    List<User> findAllUsers() throws SQLException;

}