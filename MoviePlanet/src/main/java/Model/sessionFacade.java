
import java.io.*;
import java.util.*;

/**
 * 
 */
public class SessionFacade {

    private static SessionFacade instance;

    private UserManagement userManager;

    private SessionFacade(){
        this.userManager=userManager;
    }

    public Static SessionFacade

    public static SessionFacade getInstance() {
        if (instance == null){
            instance = new SessionFacade();
        }
        return instance;
    }

    /**
     * @param id 
     * @param password 
     * @return
     */
    public boolean login(int id, String password) {
        return userManager.login(id,password);
    }

}