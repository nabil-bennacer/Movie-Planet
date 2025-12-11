
package Model;

import java.io.*;
import java.util.*;

/**
 * 
 */
public abstract class DAOFactory {

    private static DAOFactory instance = null;

    public static DAOFactory getInstance() {
        if (instance == null) {
            instance = new PostgresFactory();
        }
        return instance;
    }

    public abstract UserDAO createUserDAO();





    /**
     * @return
     */
    public UserDAO createUserDAO() {
        // TODO implement here
        return null;
    }

}