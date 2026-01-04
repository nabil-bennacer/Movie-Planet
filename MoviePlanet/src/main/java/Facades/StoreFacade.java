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
        if (catalogueManagement.verifierStock(articleId, qte)) {
            // catalogueManagement.decrementerStock(articleId, qte);
            // panierManager.ajouter(currentUserId, articleId, qte);
            return true;
        }
        return false;
    }

    public boolean ajouterArticle(Article article) {
        return catalogueManagement.ajouterArticle(article);
    }

    public boolean modifierArticle(Article article) {
        return catalogueManagement.modifierArticle(article);
    }

    public boolean supprimerArticle(int id) {
        return catalogueManagement.supprimerArticle(id);
    }
}