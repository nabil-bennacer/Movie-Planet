package BuisnessClasses;

public class Movie {
    private int id;
    private String title;
    private String description;
    private String genre;
    private int duration;
    private String posterUrl;

    public Movie(int id, String title, String description, String genre, int duration, String posterUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.duration = duration;
        this.posterUrl = posterUrl;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getGenre() { return genre; }
    public int getDuration() { return duration; }
    public String getPosterUrl() { return posterUrl; }
}