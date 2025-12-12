package BuisnessClasses;

public class User {
    private int id;
    private String password;
    private String nom;
    private String email;

    public User(int id, String password, String nom, String email) {
        this.id = id;
        this.password  = password;
        this.nom = nom;
        this.email = email;
    }

    public boolean verifyPassword(String pass) {
        if (pass == null || this.password == null) {
            return false;
        }

        return this.password.equals(pass);
        }
}