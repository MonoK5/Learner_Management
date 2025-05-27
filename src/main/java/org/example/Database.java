package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String url = "jdbc:postgresql://localhost:5432/postgres";
    private static final String user = "postgres";
    private static final String password = "root";

    public static Connection initDB() {
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String createDB = """
                CREATE TABLE IF NOT EXISTS Students (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(50),
                    score INT,
                    grade INT
                );
            """;

            stmt.executeUpdate(createDB);
            System.out.println("Table created");

        } catch (SQLException e) {
            System.out.println("Database initialization failed: " + e.getMessage());
        }
        return null;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}