package org.example.javafx;

public class UserManagement {

    private UserDAO userDAO;

    public UserManagement() {
        this.userDAO = DAOFactory.getInstance().createUserDAO();
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