package Persistence;

import BuisnessClasses.Actor;
import Persistence.ActorDAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActorDAOPostgres implements ActorDAO {
    private Connection dbConnection;

    public ActorDAOPostgres() throws SQLException {
        this.dbConnection = DAOFactory.getConnection();
    }

    public void initializeDatabase() throws SQLException{
        String createTable = "CREATE TABLE IF NOT EXISTS actors (" +
                "id SERIAL PRIMARY KEY," +
                "name VARCHAR(255) NOT NULL," +
                "nationality VARCHAR(100)," +
                "age INTEGER," +
                "photo VARCHAR(500)," +
                "biography TEXT)";

        try (Statement stmt = dbConnection.createStatement()) {
            stmt.execute(createTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Actor> findAll() {
        List<Actor> actors = new ArrayList<>();
        String query = "SELECT * FROM actors ORDER BY name";

        try (PreparedStatement stmt = dbConnection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                actors.add(mapResultSetToActor(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return actors;
    }

    @Override
    public Actor findById(int id) {
        String query = "SELECT * FROM actors WHERE id = ?";

        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToActor(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Actor> searchByName(String name) {
        List<Actor> actors = new ArrayList<>();
        String query = "SELECT * FROM actors WHERE LOWER(name) LIKE LOWER(?) ORDER BY name";

        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setString(1, "%" + name + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    actors.add(mapResultSetToActor(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return actors;
    }

    @Override
    public boolean save(Actor actor) {
        String query = "INSERT INTO actors (name, nationality, age, photo, biography) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = dbConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, actor.getName());
            stmt.setString(2, actor.getNationality());
            stmt.setInt(3, actor.getAge());
            stmt.setString(4, actor.getPhoto());
            stmt.setString(5, actor.getBiography());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        actor.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean update(Actor actor) {
        String query = "UPDATE actors SET name = ?, nationality = ?, age = ?, photo = ?, biography = ? WHERE id = ?";

        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setString(1, actor.getName());
            stmt.setString(2, actor.getNationality());
            stmt.setInt(3, actor.getAge());
            stmt.setString(4, actor.getPhoto());
            stmt.setString(5, actor.getBiography());
            stmt.setInt(6, actor.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean delete(int id) {
        String query = "DELETE FROM actors WHERE id = ?";

        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private Actor mapResultSetToActor(ResultSet rs) throws SQLException {
        return new Actor(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("nationality"),
                rs.getInt("age"),
                rs.getString("photo"),
                rs.getString("biography")
        );
    }
}