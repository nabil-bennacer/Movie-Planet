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

    public boolean login(String username, String password) {
        try {
            User user = userDAO.findUserByUsername(username);

            if(user == null) {return false;}

            return user.verifyPassword(password);
        } catch (Exception e) {
            System.err.println("Erreur dans LoginController: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}