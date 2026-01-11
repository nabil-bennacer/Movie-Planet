package Services;

import java.sql.SQLException;
import java.util.List;

import BuisnessClasses.Provider;
import Persistence.DAOFactory;
import Persistence.ProviderDAO;

public class ProviderManagement {
    private ProviderDAO providerDAO;

    public ProviderManagement() {
        try {
            this.providerDAO = DAOFactory.getInstance().createProviderDAO();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createProvider(String nom, String contactNom, String email, String numeroTel,
                               double prixAbonnement, String logoUrl, String siteUrl, String description) {
        try {
            Provider provider = new Provider(0, nom, contactNom, email, numeroTel,
                    prixAbonnement, logoUrl, siteUrl, description);
            providerDAO.insert(provider);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création du provider: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<Provider> getAllProviders() {
        try {
            return providerDAO.findAll();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des providers: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    public void updateProvider(Provider provider) {
        try {
            providerDAO.update(provider);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du provider: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void deleteProvider(int id) {
        try {
            providerDAO.delete(id);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du provider: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
