package Model;
// Cette classe est spécifiquement conçue pour interagir avec une base de données PostgreSQL afin de gérer les données des utilisateurs.

import java.sql.*;

public class UserDAOPostgres implements userDAO {
    private Connection dbConnection; // bibliothèque de java.sql. Cette connexion est utilisée pour exécuter des requêtes SQL, récupérer des données, insérer des données, etc.

    // Constructeur avec connexion à la base de données
    public UserDAOPostgres() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/movieplanet"; // URL de la base PostgreSQL (en minuscules)
        String user = "movieplanet"; // Nom d'utilisateur de la base (en minuscules)
        String password = "MoviePlanet"; // Mot de passe de la base
        this.dbConnection = DriverManager.getConnection(url, user, password);

        // Initialiser la base de données (créer les tables si elles n'existent pas)
        initializeDatabase();
    }

    // Méthode pour initialiser la base de données
    private void initializeDatabase() throws SQLException {
        // Créer la table "users" si elle n'existe pas
        String createTableQuery = "CREATE TABLE IF NOT EXISTS users (" +
                                  "id SERIAL PRIMARY KEY, " +
                                  "password VARCHAR(255), " +
                                  "nom VARCHAR(255), " +
                                  "email VARCHAR(255))";
        try (Statement stmt = dbConnection.createStatement()) {
            stmt.execute(createTableQuery); // Exécute la requête pour créer la table
        }
    }

    @Override
    public User findUserById(int id) throws SQLException {
        // Requête SQL pour trouver un utilisateur par son ID
        String query = "SELECT id, password, nom, email FROM users WHERE id = ?"; // Le ? est un paramètre qui sera remplacé par la valeur de id
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, id); // La méthode setInt remplace le premier paramètre ? dans la requête par la valeur de id.
            // ResultSet : C'est une structure qui permet de parcourir les lignes retournées par la base de données.
            // Chaque ligne correspond à un enregistrement dans la table.
            try (ResultSet result = stmt.executeQuery()) {
                if (result.next()) { // Déplace le curseur du ResultSet vers la prochaine ligne. Si un utilisateur existe, retourne true, sinon false.
                    User user = new User();
                    user.setId(result.getInt("id"));
                    user.setPassword(result.getString("password"));
                    user.setNom(result.getString("nom"));
                    user.setEmail(result.getString("email"));
                    return user; // Retourne l'utilisateur trouvé
                }
            }
        }
        return null; // Si aucun utilisateur n'est trouvé avec l'ID donné
    }
}