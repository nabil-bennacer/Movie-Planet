package Facades;

import BuisnessClasses.Article;
import Services.CatalogueManagement;

import java.util.List;

public class StoreFacade {
    private CatalogueManagement catalogueManagement;

    public StoreFacade() {
        this.catalogueManagement = new CatalogueManagement();
    }

    public List<Article> getAllArticles() {
        return catalogueManagement.getArticles();
    }

    public List<Article> searchArticles(String query) {
        return catalogueManagement.filtrerArticles(query);
    }

    public boolean ajouterAuPanier(int currentUserId, int articleId, int qte) {
        // 1. Vérifier si le stock est suffisant via le Manager (qui gère déjà les exceptions SQL)
        if (catalogueManagement.verifierStock(articleId, qte)) {

            // 2. Tenter de décrémenter le stock en base de données
            boolean stockMisAJour = catalogueManagement.decrementerStock(articleId, qte);

            if (stockMisAJour) {
                /*
                // 3. Si le stock a bien été décrémenté, on ajoute l'article au panier
                panierManager.ajouter(currentUserId, articleId, qte);
                return true; // Succès
                */
            }
        }

        // 4. Echec : Stock insuffisant ou erreur BDD
        return false;
    }
}