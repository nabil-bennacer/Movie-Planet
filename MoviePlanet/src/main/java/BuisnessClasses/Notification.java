package BuisnessClasses;

import java.util.Date;

public class Notification {
    private int id;
    private String message;
    private Date date;

    public Notification(int id, String message, Date date) {
        this.id = id;
        this.message = message;
        this.date = date;
    }

    public int getId() { return id; }
    public String getMessage() { return message; }
    public Date getDate() { return date; }

    @Override
    public String toString() {
        return "[" + date + "] " + message;
    }
}