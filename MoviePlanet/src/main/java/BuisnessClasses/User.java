package BuisnessClasses;

public class User {
    private int id;
    private String password;
    private String nom;
    private String email;
    private String role;

    public User(int id, String password, String nom, String email, String role) {
        this.id = id;
        this.password  = password;
        this.nom = nom;
        this.email = email;
        this.role = role;
    }

    public boolean verifyPassword(String pass) {
        if (pass == null || this.password == null) {
            return false;
        }

        return this.password.equals(pass);
    }

    public String getRole() {
        return role;
    }

    public boolean isAdmin() {
        return "admin".equals(this.role);
    }

    public int getId() {
        return this.id;
    }
}