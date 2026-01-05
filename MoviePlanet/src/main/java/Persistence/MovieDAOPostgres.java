package Persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import BuisnessClasses.Movie;

public class MovieDAOPostgres implements MovieDAO {
    private Connection dbConnection;

    public MovieDAOPostgres() throws SQLException {
        this.dbConnection = DAOFactory.getConnection();
    }

    @Override
    public List<Movie> findAll() throws SQLException {
        List<Movie> movies = new ArrayList<>();
        String query = "SELECT * FROM movies";
        try (Statement stmt = dbConnection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                movies.add(mapResultSetToMovie(rs));
            }
        }
        return movies;
    }

    @Override
    public Movie findById(int id) throws SQLException {
        String query = "SELECT * FROM movies WHERE id = ?";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMovie(rs);
                }
            }
        }
        return null;
    }

    public List<Movie> searchByTitle(String titleQuery) throws SQLException{
        List<Movie> movies = new ArrayList<>();
        String query = "SELECT * FROM movies WHERE title ILIKE ?";

        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setString(1, "%" + titleQuery + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    movies.add(mapResultSetToMovie(rs));
                }
            }
        }
        return movies;
    }

    @Override
    public boolean createMovie(Movie movie) throws SQLException {
        String query = "INSERT INTO movies (title, description, genre, duration, poster_url) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setString(1, movie.getTitle());
            stmt.setString(2, movie.getDescription());
            stmt.setString(3, movie.getGenre());
            stmt.setInt(4, movie.getDuration());
            stmt.setString(5, movie.getPosterUrl());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deleteMovie(int id) throws SQLException {
        String query = "DELETE FROM movies WHERE id = ?";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    private Movie mapResultSetToMovie(ResultSet rs) throws SQLException {
        return new Movie(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getString("genre"),
                rs.getInt("duration"),
                rs.getString("poster_url")
        );
    }
}