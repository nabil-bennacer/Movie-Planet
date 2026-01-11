package IntegrationTest;

import BuisnessClasses.Article;
import BuisnessClasses.Commentaire;
import BuisnessClasses.Movie;
import BuisnessClasses.User;
import Facades.StoreFacade;
import Services.MovieManagement;
import Services.NotificationManagement;
import Services.UserManagement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.List;

public class IntegrationTest {

    @Test
    public void testUserLogin() {
        System.out.println("Test : User Management (Login)");

        UserManagement userManagement = new UserManagement();
        User user = userManagement.login("admin", "admin");

        Assertions.assertNotNull(user);
        Assertions.assertEquals("admin", user.getUsername());

        System.out.println("Login réussi pour : " + user.getUsername());
    }

    @Test
    public void testCreateNotification() {
        System.out.println("Test : Notification Management (Create)");

        NotificationManagement notifManager = new NotificationManagement();
        String msg = "Test Unitaire " + System.currentTimeMillis();
        boolean result = notifManager.createNotification(msg);

        Assertions.assertTrue(result);

        System.out.println("Notification créée : " + msg);
    }

    @Test
    public void testSearchMovies() {
        System.out.println("Test : Movie Management (Search)");

        MovieManagement movieManager = new MovieManagement();
        // Recherche d'un film connu (ex: Batman)
        List<Movie> results = movieManager.searchMovies("Inception");

        Assertions.assertNotNull(results);
        Assertions.assertFalse(results.isEmpty());

        System.out.println("Film trouvé : " + results.get(0).getTitle());
    }

    @Test
    public void testFanStoreAndCommentsSimple() {
        System.out.println("Test : Fan Store & Comments");
        StoreFacade store = new StoreFacade();

        // 1. Création rapide d'un article
        String articleName = "Test" + System.currentTimeMillis();
        store.ajouterArticle(new Article(0, articleName, 15.0, "Desc", "img.png", 10, "Film"));

        // 2. Récupération de l'ID (Fan Store)
        Article article = store.searchArticles(articleName).get(0);
        int id = article.getId();
        Assertions.assertNotNull(article);

        // 3. Ajout et Vérification du Commentaire (Comment Use Case)
        store.ajouterCommentaire(new Commentaire(1, id, "Top !", 5));

        List<Commentaire> comments = store.getCommentaires(id);
        Assertions.assertFalse(comments.isEmpty(), "Le commentaire devrait être présent");
        Assertions.assertEquals("Top !", comments.get(0).getTexte());

        // Nettoyage
        store.supprimerArticle(id);
    }
}