package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

import static org.example.StudService.capitalizeName;

public class StudentDB {
    // Database connection parameters
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "root";

    // Establishes and returns a connection to the database
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Initializes the 'students' table if it doesn't exist
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

    // Adds a new student to the database if not already present
    public static void addStudent(Student student) throws SQLException {
        Random random = new Random();
        int randomId;
        boolean unique = false;

        if (studentExists(student)) {
            System.out.println("Student " + student.getName() + " with score " +
                    student.getScore() + " already exists.");
            return;
        } else {
            System.out.println(capitalizeName(student.getName()) + " was added successfully.");
        }

        // Generate a unique random ID
        do {
            randomId = random.nextInt(1000); // ID range: 0–999
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

    // Checks if a student already exists based on name and score
    public static boolean studentExists(Student student) throws SQLException {
        if (student == null) {
            System.out.println("Student cannot be null");
            return false;
        }

        String sql = "SELECT COUNT(*) FROM students WHERE name = ? AND score = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getName());
            pstmt.setInt(2, student.getScore());

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking student existence: " + e.getMessage());
            throw e;
        }
    }

    // Retrieves all students sorted by name
    public static ArrayList<Student> getAllStudents() {
        ArrayList<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY name";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

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

    // ✅ Updated: Updates an existing student record with validation and feedback
    public static void updateStudent(Student student) {
        if (student == null) {
            System.out.println("Cannot update: student is null.");
            return;
        }

        // Check if student exists in DB
        if (getStudentById(student.getId()) == null) {
            System.out.println("Student with ID " + student.getId() + " does not exist.");
            return;
        }

        // Basic validation for name
        if (student.getName() == null || student.getName().trim().isEmpty()) {
            System.out.println("Invalid name. Update aborted.");
            return;
        }

        // Update name, score, and grade
        String sql = "UPDATE students SET name = ?, score = ?, grade = ? WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, student.getName());
            pstmt.setInt(2, student.getScore());
            pstmt.setInt(3, student.getGrade());
            pstmt.setInt(4, student.getId());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student with ID " + student.getId() + " updated successfully.");
            } else {
                System.out.println("Update failed: no student found with ID " + student.getId());
            }
        } catch (SQLException e) {
            System.out.println("Error updating student: " + e.getMessage());
        }
    }

    // Deletes a student by ID
    public static void deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Successfully deleted student with ID: " + id);
            } else {
                System.out.println("No student found with ID: " + id);
            }

        } catch (SQLException e) {
            System.err.println("Database error deleting student: " + e.getMessage());
        }
    }

    // Searches students by (partial) name
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

    // Retrieves a single student by ID
    public static Student getStudentById(int id) {
        String sql = "SELECT * FROM students WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("score"),
                        rs.getInt("grade")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving student: " + e.getMessage());
        }

        return null;
    }
}
