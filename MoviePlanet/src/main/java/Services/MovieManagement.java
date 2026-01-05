package Services;

import BuisnessClasses.Movie;
import Persistence.DAOFactory;
import Persistence.MovieDAO;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class MovieManagement {
    private MovieDAO movieDAO;

    public MovieManagement() {
        try {
            this.movieDAO = DAOFactory.getInstance().createMovieDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Movie> getAllMovies() {
        try {
            return movieDAO.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public Movie getMovieById(int id) {
        try {
            return movieDAO.findById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Movie> searchMovies(String query) {
        try {
            return movieDAO.searchByTitle(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public boolean createMovie(String title, String desc, String genre, int duration, String url) {
        try {
            // ID à 0 car généré par la BDD
            Movie newMovie = new Movie(0, title, desc, genre, duration, url);
            return movieDAO.createMovie(newMovie);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteMovie(int id) {
        try {
            return movieDAO.deleteMovie(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}