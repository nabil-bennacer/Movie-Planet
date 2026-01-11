package Persistence;

import java.sql.SQLException;
import java.util.List;

import BuisnessClasses.Provider;

public interface ProviderDAO {
    void insert(Provider provider) throws SQLException;
    List<Provider> findAll() throws SQLException;
    void update(Provider provider) throws SQLException;
    void delete(int id) throws SQLException;
    Provider findById(int id) throws SQLException;
}
