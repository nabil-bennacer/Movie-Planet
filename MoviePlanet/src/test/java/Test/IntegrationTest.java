package IntegrationTest;

import BuisnessClasses.Movie;
import BuisnessClasses.User;
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
}