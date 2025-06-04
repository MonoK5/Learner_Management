// StudentDB.java
package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

import static org.example.StudService.capitalizeName;

public class StudentDB {

    public static void addStudent(Student student) throws SQLException {
        Random random = new Random();
        int randomId;
        boolean unique = false;

        if (studentExists(student)) {
            System.out.println("Student " + student.getName() + " with score " + student.getScore() + " already exists.");
            return;
        } else {
            System.out.println(capitalizeName(student.getName()) + " was added successfully.");
        }

        // Generate a unique ID
        do {
            randomId = random.nextInt(1000);
            if (getStudentById(randomId) == null) {
                unique = true;
            }
        } while (!unique);

        String sql = "INSERT INTO students (id, name, score, grade) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, randomId);
            pstmt.setString(2, student.getName());
            pstmt.setInt(3, student.getScore());
            pstmt.setInt(4, student.getGrade());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding student: " + e.getMessage());
        }
    }

    public static boolean studentExists(Student student) throws SQLException {
        if (student == null) {
            System.out.println("Student cannot be null");
            return false;
        }

        String sql = "SELECT COUNT(*) FROM students WHERE name = ? AND score = ?";
        try (Connection conn = DatabaseConnection.connect();
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

    public static ArrayList<Student> getAllStudents() {
        ArrayList<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY name";

        try (Connection conn = DatabaseConnection.connect();
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

    public static void updateStudent(Student student) {
        String sql = "UPDATE students SET name = ?, score = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

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
        try (Connection conn = DatabaseConnection.connect();
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

    public static ArrayList<Student> searchStudentByName(String name) {
        ArrayList<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE LOWER(name) LIKE LOWER(?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

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
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("score"),
                        rs.getInt("grade"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving student: " + e.getMessage());
        }
        return null;
    }
}
