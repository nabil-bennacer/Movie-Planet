package org.example.javafx;

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