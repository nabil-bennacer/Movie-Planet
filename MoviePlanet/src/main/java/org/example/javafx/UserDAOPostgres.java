package org.example.javafx;
//Cette classe est spécifiquement conçue pour interagir avec une base de données PostgreSQL afin de gérer les données des utilisateurs.
import java.sql.*;
import Model.User;

public class UserDAOPostgres implements userDAO {
    private Connection dbConnection;//bibliotheque de java.sql Cette connexion est utilisée pour exécuter des requêtes SQL, récupérer des données, insérer des données, etc.

    // Constructeur avec connexion à la base de données
    public UserDAOPostgres(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public User findUserById(int id) throws SQLException {
        String query = "SELECT id, password, nom, email FROM users WHERE id = ?";//Le ? est un paramètre qui sera remplacé par la valeur de id
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, id);//La méthode setInt remplace le premier paramètre ? dans la requête par la valeur de id.
            //ResultSet:C'est une structure qui permet de parcourir les lignes retournées par la base de données.
            //Chaque ligne correspond à un enregistrement dans la table.
            try (ResultSet result = stmt.executeQuery()) {
                if (result.next()) {//deplace le curseur du ResultSet vers la prochaine ligne,si existe un user true sinon false.
                    User user = new User();
                    user.setId(result.getInt("id"));
                    user.setPassword(result.getString("password"));
                    user.setNom(result.getString("nom"));
                    user.setEmail(result.getString("email"));
                    return user;
                }
            }
        }
        return null; //si aucun utilisateur n'est trouvé avec l'ID donné
    }
}

