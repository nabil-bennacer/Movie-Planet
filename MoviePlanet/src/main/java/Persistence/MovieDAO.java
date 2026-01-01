package Persistence;

import BuisnessClasses.Movie;
import java.sql.SQLException;
import java.util.List;

public interface MovieDAO {
    List<Movie> findAll() throws SQLException;
    Movie findById(int id) throws SQLException;

    boolean createMovie(Movie movie) throws SQLException;
    boolean deleteMovie(int id) throws SQLException;
}