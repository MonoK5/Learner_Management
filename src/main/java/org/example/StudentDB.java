package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

import static org.example.StudService.capitalizeName;

public class StudentDB {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Letsdoit!";

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


    public static void addStudent(Student student) throws SQLException {
        Random random = new Random();
        int randomId;
        boolean unique = false;

        if (studentExists(student)) {
            System.out.println("Student " + student.getName() + " with score " +
                    student.getScore() + " already exists.");
            return;
        }else{
            System.out.println(capitalizeName(student.getName())+ " was added successfully.");
        }

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

    public static boolean studentExists(Student student) throws SQLException {
        if (student == null) {
            System.out.println("Student cannot be null");
        }

        String sql = "SELECT COUNT(*) FROM student WHERE name = ? AND score = ?";

        try (Connection connection = connect();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, student != null ? student.getName() : null);
            pstmt.setInt(2, student != null ? student.getScore() : 0);

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
        String sql = "SELECT * FROM students";

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


    public static void deleteStudent(int id)  throws  SQLException{
        String sql = "DELETE FROM student WHERE id = ?";

        try (Connection connection = connect();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Student deleted successfully with ID: " + id);
            } else {
                System.out.println("No student found with ID: " + id);
            }

        } catch (SQLException e) {
            System.out.println("Error deleting student: " + e.getMessage());
            throw e;
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
