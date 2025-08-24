package org.connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/contact_manager_db";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "admin";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            System.err.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        return connection;
    }

    public static void initializeDatabase() {
        // SQL to create the 'contacts' table
        String createContactsTableSQL = "CREATE TABLE IF NOT EXISTS contacts ("
                + "id SERIAL PRIMARY KEY,"
                + "name VARCHAR(100) NOT NULL,"
                + "phone VARCHAR(20),"
                + "email VARCHAR(100)"
                + ");";

        // --- NEW: SQL to create the 'profile' table ---
        String createProfileTableSQL = "CREATE TABLE IF NOT EXISTS profile ("
                + "id INT PRIMARY KEY,"
                + "bio TEXT,"
                + "image_data BYTEA" // BYTEA is for binary data like images
                + ");";

        // --- NEW: SQL to insert a default profile row if it doesn't exist ---
        // This ensures we always have one row to UPDATE.
        String insertDefaultProfileSQL = "INSERT INTO profile (id, bio, image_data) VALUES (1, '', NULL)"
                + " ON CONFLICT (id) DO NOTHING;";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Execute all setup commands
            stmt.execute(createContactsTableSQL);
            stmt.execute(createProfileTableSQL);
            stmt.execute(insertDefaultProfileSQL);

            System.out.println("Database initialized. Tables 'contacts' and 'profile' are ready.");

        } catch (SQLException e) {
            System.err.println("Error during database initialization.");
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("Database connection is null. Please check your connection details.");
        }
    }
}