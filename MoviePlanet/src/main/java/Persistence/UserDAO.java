package Persistence;
import BuisnessClasses.User;

import java.sql.SQLException;

public interface UserDAO {
    User findUserByUsername(String username) throws SQLException;
}