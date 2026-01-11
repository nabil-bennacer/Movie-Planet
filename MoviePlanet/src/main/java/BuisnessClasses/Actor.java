package BuisnessClasses;

public class Actor {
    private int id;
    private String name;
    private String nationality;
    private int age;
    private String photo;
    private String biography;

    public Actor() {}

    public Actor(int id, String name, String nationality, int age, String photo, String biography) {
        this.id = id;
        this.name = name;
        this.nationality = nationality;
        this.age = age;
        this.photo = photo;
        this.biography = biography;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getNationality() { return nationality; }
    public int getAge() { return age; }
    public String getPhoto() { return photo; }
    public String getBiography() { return biography; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setNationality(String nationality) { this.nationality = nationality; }
    public void setAge(int age) { this.age = age; }
    public void setPhoto(String photo) { this.photo = photo; }
    public void setBiography(String biography) { this.biography = biography; }

    @Override
    public String toString() {
        return "Actor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nationality='" + nationality + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Actor actor = (Actor) obj;
        return id == actor.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}