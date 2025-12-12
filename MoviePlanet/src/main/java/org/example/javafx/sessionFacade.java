package org.example.javafx;

public class SessionFacade {

    private static SessionFacade instance;

    private UserManagement userManager;

    private SessionFacade(){
    }

    public static SessionFacade getInstance() {
        if (instance == null){
            instance = new SessionFacade();
        }
        return instance;
    }

    /**
     * @param username
     * @param password 
     * @return
     */
    public boolean login(String username, String password) {
        return userManager.login(username,password);
    }

}