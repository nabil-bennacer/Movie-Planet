package org.example.javafx;

public class SessionFacade {

    private static SessionFacade instance;

    private UserManagement userManager;

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
        // Vérification de sécurité
        if (userManager == null) {
            System.err.println("Erreur: UserManagement est null dans SessionFacade");
            return false;
        }
        return userManager.login(username, password);
    }
}