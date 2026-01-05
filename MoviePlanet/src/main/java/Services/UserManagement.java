package Services;

import Persistence.DAOFactory;
import BuisnessClasses.User;
import Persistence.UserDAO;

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

            if(user == null) { return null; }

            if (user.verifyPassword(password)) {
                return user;
            }
            return null;

        } catch (Exception e) {
            System.err.println("Erreur dans UserManagement: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}