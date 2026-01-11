package Persistence;

import java.sql.SQLException;

/**
 * 
 */
public class PostgresFactory extends DAOFactory {

    /**
     * @return
     */
    @Override
    public UserDAO createUserDAO()  {
        try {
            return new UserDAOPostgres();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ProviderDAO createProviderDAO() {
        try {
            return new ProviderDAOPostgres();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BasketDAO createBasketDAO() { 
        try {
            return new BasketDAOPostgres();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
public OrderDAO createOrderDAO() {
    try {
        return new OrderDAOPostgres();
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}

}