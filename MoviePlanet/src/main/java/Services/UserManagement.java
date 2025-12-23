package Services;

import Persistence.DAOFactory;
import BuisnessClasses.User;
import Persistence.UserDAO;
import java.util.Collections;
import java.util.List;
import java.sql.SQLException;

public class UserManagement {

    private UserDAO userDAO;

    public UserManagement() {
        try {
            this.userDAO = DAOFactory.getInstance().createUserDAO();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User login(String username, String password) {
        try {
            User user = userDAO.findUserByUsername(username);
            if (user != null && user.verifyPassword(password)) {
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean register(String username, String password, String email) {
        try {
            if (userDAO.findUserByUsername(username) != null) {
                return false;
            }
            User newUser = new User(0, username, password, email, "User");
            return userDAO.createUser(newUser);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<User> getAllUsers() {
        try {
            return userDAO.findAllUsers();
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public boolean deleteUser(int userId) {
        try {
            return userDAO.deleteUser(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}