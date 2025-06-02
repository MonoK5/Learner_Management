package org.example;

import java.sql.*;

public class Database {

    private static final String url = "jdbc:postgresql://localhost:5432/postgres";
    private static final String user = "postgres";
    private static final String password = "root";

    public static void initDB() {
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String createDB = """
                CREATE TABLE IF NOT EXISTS student (
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
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}




