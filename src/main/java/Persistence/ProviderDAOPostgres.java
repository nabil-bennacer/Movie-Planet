package Persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import BuisnessClasses.Provider;

public class ProviderDAOPostgres implements ProviderDAO {
    private Connection dbConnection;

    public ProviderDAOPostgres() throws SQLException {
        // Utilisation de la configuration centralisée
        this.dbConnection = DriverManager.getConnection(
            DatabaseConfig.getUrl(),
            DatabaseConfig.getUser(),
            DatabaseConfig.getPassword()
        );
        initializeDatabase();
    }

    private void initializeDatabase() throws SQLException {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS providers (" +
                "id SERIAL PRIMARY KEY, " +
                "nom VARCHAR(255) NOT NULL, " +
                "contact_nom VARCHAR(255), " +
                "email VARCHAR(255) UNIQUE NOT NULL, " +
                "numero_tel VARCHAR(50), " +
                "prix_abonnement DECIMAL(10, 2), " +
                "logo_url VARCHAR(255), " +
                "site_url VARCHAR(255), " +
                "description TEXT)";
        try (Statement stmt = dbConnection.createStatement()) {
            stmt.execute(createTableQuery);
        }
    }

    @Override
    public void insert(Provider provider) throws SQLException {
        String query = "INSERT INTO providers (nom, contact_nom, email, numero_tel, prix_abonnement, logo_url, site_url, description) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setString(1, provider.getNom());
            stmt.setString(2, provider.getContactNom());
            stmt.setString(3, provider.getEmail());
            stmt.setString(4, provider.getNumeroTel());
            stmt.setDouble(5, provider.getPrixAbonnement());
            stmt.setString(6, provider.getLogoUrl());
            stmt.setString(7, provider.getSiteUrl());
            stmt.setString(8, provider.getDescription());
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Provider> findAll() throws SQLException {
        List<Provider> providers = new ArrayList<>();
        String query = "SELECT * FROM providers ORDER BY nom";
        try (Statement stmt = dbConnection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                providers.add(new Provider(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("contact_nom"),
                        rs.getString("email"),
                        rs.getString("numero_tel"),
                        rs.getDouble("prix_abonnement"),
                        rs.getString("logo_url"),
                        rs.getString("site_url"),
                        rs.getString("description")
                ));
            }
        }
        return providers;
    }

    @Override
    public void update(Provider provider) throws SQLException {
        String query = "UPDATE providers SET nom=?, contact_nom=?, email=?, numero_tel=?, " +
                "prix_abonnement=?, logo_url=?, site_url=?, description=? WHERE id=?";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setString(1, provider.getNom());
            stmt.setString(2, provider.getContactNom());
            stmt.setString(3, provider.getEmail());
            stmt.setString(4, provider.getNumeroTel());
            stmt.setDouble(5, provider.getPrixAbonnement());
            stmt.setString(6, provider.getLogoUrl());
            stmt.setString(7, provider.getSiteUrl());
            stmt.setString(8, provider.getDescription());
            stmt.setInt(9, provider.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM providers WHERE id = ?";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Provider findById(int id) throws SQLException {
        String query = "SELECT * FROM providers WHERE id = ?";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Provider(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("contact_nom"),
                            rs.getString("email"),
                            rs.getString("numero_tel"),
                            rs.getDouble("prix_abonnement"),
                            rs.getString("logo_url"),
                            rs.getString("site_url"),
                            rs.getString("description")
                    );
                }
            }
        }
        return null;
    }
}
