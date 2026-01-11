package Persistence;

public class DatabaseConfig {
    // Configuration Neon - NOUVEAU COMPTE
    private static final String DB_URL = "jdbc:postgresql://ep-morning-wind-agpw3rb0-pooler.c-2.eu-central-1.aws.neon.tech/neondb?sslmode=require";
    private static final String DB_USER = "neondb_owner";
    private static final String DB_PASSWORD = "npg_NXDSbcf26hVt";

    public static String getUrl() {
        return DB_URL;
    }

    public static String getUser() {
        return DB_USER;
    }

    public static String getPassword() {
        return DB_PASSWORD;
    }
}