
import java.io.*;
import java.util.*;

public class UserManagement {

    private UserDAO userDAO;

    public UserManagement(UserDAO userDAO) {

        this.userDAO = userDAO
    }

    public boolean login(int id, String password) {
        try {

            return sessionFacade.login(username, password);

        } catch (Exception e) {
            System.err.println("Erreur dans LoginController: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}