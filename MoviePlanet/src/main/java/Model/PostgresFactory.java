package Model;

import java.io.*;
import java.util.*;

/**
 * 
 */
public class PostgresFactory extends DAOFactory {

    /**
     * @return
     */
    @Override
    public UserDAO createUserDAO() {
        return new UserDAOPostgres();
    }

}