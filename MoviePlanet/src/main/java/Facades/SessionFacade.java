package Facades;

import BuisnessClasses.User;
import Services.UserManagement;
import java.util.List;

public class SessionFacade {

    private static SessionFacade instance;

    private UserManagement userManager;
    private User currentUser;

    private SessionFacade() {
        // CORRECTION IMPORTANTE : Initialiser le UserManagement ici
        this.userManager = new UserManagement();
    }

    public static SessionFacade getInstance() {
        if (instance == null){
            instance = new SessionFacade();
        }
        return instance;
    }

    public boolean login(String username, String password) {
        User user = userManager.login(username, password);
        if (user != null) {
            this.currentUser = user;
            return true;
        }
        return false;
    }

    public void logout() {
        this.currentUser = null;
    }

    // Retrait du paramètre 'nom'
    public boolean register(String username, String password, String email) {
        return userManager.register(username, password, email);
    }

    public List<User> getAllUsers() {
        // Sécurité simple : seul un admin connecté peut voir la liste
        if (currentUser != null && "Admin".equals(currentUser.getRole())) {
            return userManager.getAllUsers();
        }
        return null;
    }

    public boolean deleteUser(int userId) {
        if (currentUser != null && "Admin".equals(currentUser.getRole())) {
            return userManager.deleteUser(userId);
        }
        return false;
    }

    public User getCurrentUser() {
        return currentUser;
    }



}