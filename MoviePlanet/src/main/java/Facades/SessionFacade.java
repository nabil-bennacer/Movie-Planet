package Facades;

import BuisnessClasses.User;
import Services.UserManagement;

public class SessionFacade {

    private static SessionFacade instance;
    private UserManagement userManager;
    private User currentUser;

    private SessionFacade() {
        this.userManager = new UserManagement();
    }

    public static SessionFacade getInstance() {
        if (instance == null){
            instance = new SessionFacade();
        }
        return instance;
    }

    public boolean login(String username, String password) {
        if (userManager == null) {
            System.err.println("Erreur: UserManagement est null dans SessionFacade");
            return false;
        }

        User user = userManager.login(username, password);

        if (user != null) {
            this.currentUser = user;
            return true;
        }
        return false;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }
}