package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

public class StudentDB {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "root";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void initializeDatabase() {
        String sql = """
            CREATE TABLE IF NOT EXISTS students (
                id INT PRIMARY KEY,
                name VARCHAR(50),
                score INT,
                grade INT
            );
        """;

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Table 'students' is ready.");
        } catch (SQLException e) {
            System.out.println("Database initialization failed: " + e.getMessage());
        }
    }


    public static void addStudent(Student student) {
        Random random = new Random();
        int randomId;
        boolean unique = false;

        // Generate random IDs until you find one that doesn't exist in DB
        do {
            randomId = random.nextInt(1000); // example range 0 to 999,999
            if (getStudentById(randomId) == null) {
                unique = true;
            }
        } while (!unique);

        String sql = "INSERT INTO students (id, name, score, grade) VALUES (?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, randomId);
            pstmt.setString(2, student.getName());
            pstmt.setInt(3, student.getScore());
            pstmt.setInt(4, student.getGrade());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding student: " + e.getMessage());
        }
    }



    public static ArrayList<Student> getAllStudents() {
        ArrayList<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";

        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("score"),
                        rs.getInt("grade")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving students: " + e.getMessage());
        }

        return students;
    }



    public static void updateStudent(Student student) {
        String sql = "UPDATE students SET name = ?, score = ? WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, student.getName());
            pstmt.setInt(2, student.getScore());
            pstmt.setInt(3, student.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating student: " + e.getMessage());
        }
    }


    public static void deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting student: " + e.getMessage());
        }
    }

    public static ArrayList<Student> searchStudentByName(String name) {
        ArrayList<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE LOWER(name) LIKE LOWER(?)";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + name + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("score"),
                        rs.getInt("grade")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error searching students: " + e.getMessage());
        }

        return students;
    }

    public static Student getStudentById(int id) {
        String sql = "SELECT * FROM students WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Student(rs.getInt("id"), rs.getString("name"), rs.getInt("score"), rs.getInt("grade"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving student: " + e.getMessage());
        }
        return null;
    }



}
