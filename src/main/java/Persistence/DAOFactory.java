package Persistence;

import java.sql.SQLException;

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

    public abstract UserDAO createUserDAO() throws SQLException;
    public abstract ProviderDAO createProviderDAO() throws SQLException;
    public abstract BasketDAO createBasketDAO() throws SQLException;
    public abstract OrderDAO createOrderDAO() throws SQLException;
}