
import java.io.*;
import java.util{
        this.id = id;
        this.password = password;
        this.nom = nom;
        this.email = email;
    }.*;

public class User {
    private int id;
    private String password;
    private String nom;
    private String email;

    public User(int id, String password, String nom, String email)

    public boolean verifyPassword(String pass) {
        if (pass == null || this.password == null) {
            return false;
        }
        // Version simple pour le prototype
        // En production, il faudrait utiliser BCrypt.checkpw() ou similaire
        return this.password.equals(pass);
        }
}