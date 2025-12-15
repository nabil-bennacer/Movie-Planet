package Services;

import Persistence.DAOFactory;
import Persistence.ArticleDAO;
import BuisnessClasses.Article;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CatalogueManagement {

    private ArticleDAO articleDAO;

    public CatalogueManagement() {
        try {
            // Utilisation du Singleton comme dans votre UserManagement
            this.articleDAO = DAOFactory.getInstance().createArticleDAO();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Récupérer tous les articles (Flux Principal Store)
    public List<Article> getArticles() {
        try {
            return articleDAO.findAll();
        } catch (SQLException e) {
            System.err.println("Erreur dans CatalogueManagement (getArticles): " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>(); // Retourne une liste vide en cas d'erreur
        }
    }

    // Filtrer les articles (Recherche Utilisateur)
    public List<Article> filtrerArticles(String critere) {
        try {
            List<Article> allArticles = articleDAO.findAll();

            if (critere == null || critere.trim().isEmpty()) {
                return allArticles;
            }

            String lowerCritere = critere.toLowerCase();
            return allArticles.stream()
                    .filter(a -> a.getNom().toLowerCase().contains(lowerCritere) ||
                            a.getFilmLie().toLowerCase().contains(lowerCritere))
                    .collect(Collectors.toList());

        } catch (SQLException e) {
            System.err.println("Erreur dans CatalogueManagement (filtrerArticles): " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Vérifier la disponibilité (Utilisé avant ajout au panier)
    public boolean verifierStock(int id, int qte) {
        try {
            Article article = articleDAO.findById(id);
            return article != null && article.getStock() >= qte;
        } catch (SQLException e) {
            System.err.println("Erreur dans CatalogueManagement (verifierStock): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Décrémenter le stock (Après validation panier)
    public boolean decrementerStock(int id, int qte) {
        try {
            Article article = articleDAO.findById(id);
            if (article != null && article.getStock() >= qte) {
                article.setStock(article.getStock() - qte);
                articleDAO.update(article);
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Erreur dans CatalogueManagement (decrementerStock): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // --- Méthodes pour l'Administrateur (Gestion Catalogue) ---

    public boolean ajouterArticle(Article article) {
        try {
            articleDAO.create(article);
            return true;
        } catch (SQLException e) {
            System.err.println("Erreur dans CatalogueManagement (ajouterArticle): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean modifierArticle(Article article) {
        try {
            articleDAO.update(article);
            return true;
        } catch (SQLException e) {
            System.err.println("Erreur dans CatalogueManagement (modifierArticle): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean supprimerArticle(int id) {
        try {
            articleDAO.delete(id);
            return true;
        } catch (SQLException e) {
            System.err.println("Erreur dans CatalogueManagement (supprimerArticle): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}