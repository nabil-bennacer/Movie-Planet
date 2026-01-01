package Facades;

import BuisnessClasses.User;
import Services.UserManagement;
import Services.MovieManagement;
import Services.NotificationManagement;
import java.util.List;
import BuisnessClasses.Movie;
import BuisnessClasses.Notification;

public class SessionFacade {

    private static SessionFacade instance;

    private UserManagement userManager;
    private MovieManagement movieManager;
    private User currentUser;
    private NotificationManagement notifManager;

    private SessionFacade() {
        // CORRECTION IMPORTANTE : Initialiser le UserManagement ici
        this.userManager = new UserManagement();
        this.movieManager = new MovieManagement();
        this.notifManager = new NotificationManagement();
    }

    public static SessionFacade getInstance() {
        if (instance == null){
            instance = new SessionFacade();
        }
        return instance;
    }

    public List<Movie> getAllMovies() {
        return movieManager.getAllMovies();
    }

    public Movie getMovieById(int id) {
        return movieManager.getMovieById(id);
    }

    public boolean login(String username, String password) {
        User user = userManager.login(username, password);
        if (user != null) {
            this.currentUser = user;
            return true;
        }
        return false;
    }

    public void logout() {
        this.currentUser = null;
    }

    // Retrait du paramètre 'nom'
    public boolean register(String username, String password, String email) {
        return userManager.register(username, password, email);
    }

    public List<User> getAllUsers() {
        // Sécurité simple : seul un admin connecté peut voir la liste
        if (currentUser != null && "Admin".equals(currentUser.getRole())) {
            return userManager.getAllUsers();
        }
        return null;
    }

    public boolean deleteUser(int userId) {
        if (currentUser != null && "Admin".equals(currentUser.getRole())) {
            return userManager.deleteUser(userId);
        }
        return false;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean createMovie(String title, String desc, String genre, int duration, String url) {
        if (currentUser != null && "Admin".equals(currentUser.getRole())) {
            return movieManager.createMovie(title, desc, genre, duration, url);
        }
        return false;
    }

    public boolean deleteMovie(int movieId) {
        if (currentUser != null && "Admin".equals(currentUser.getRole())) {
            return movieManager.deleteMovie(movieId);
        }
        return false;
    }

    public List<Notification> getNotifications() {
        return notifManager.getAllNotifications();
    }

    public boolean sendNotification(String message) {
        // Seul l'admin peut envoyer
        if (currentUser != null && "Admin".equals(currentUser.getRole())) {
            return notifManager.createNotification(message);
        }
        return false;
    }



}