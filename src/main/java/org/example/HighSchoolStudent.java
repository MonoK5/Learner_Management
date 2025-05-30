package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.example.StudentData.formatter;

public class HighSchoolStudent extends AbstractStudent {

    @Override
    public  void  addNewStudent(Student newStudents) throws SQLException {


    }
    @Override
    public List<Student> displayAllStudent() throws SQLException {

       return  null;
    }
    @Override
    public void updateStudentDetails(Student updateDetails) throws SQLException{}
    @Override
    public double calculateAllMarksAvg(List<Student> calculateAvg) throws  SQLException{
        return  0;
    }
    @Override
    public List<Student> searchStudent(Student search) throws SQLException {
      return  null;
    }
    @Override
    public boolean deleteStudent(int studentId, String studentName) throws SQLException {
        String deleteQuery = "DELETE FROM Students WHERE id = ? AND name = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(deleteQuery)) {

            pstmt.setInt(1, studentId);
            pstmt.setString(2, studentName);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Student deleted successfully with name: " +  formatter(studentName));
                return true;
            } else {
                System.out.println("No student found with ID: " + studentId + " and Name: " + formatter(studentName));
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Error deleting student: " + e.getMessage());
            throw e;
        }
    }
    @Override
    public boolean studentExists(Student student) throws SQLException {
        if (student == null) {
            System.out.println("Student cannot be null");
        }

        String checkExistsQuery = "SELECT COUNT(*) FROM Students WHERE name = ? AND score = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(checkExistsQuery)) {

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
    @Override
    public List<Student> getAllStudentMarks() throws SQLException {
        String allStudentQuery = "SELECT id, name, score FROM Students ORDER BY name";
        List<Student> students = new ArrayList<>();

        try (Connection conn =Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(allStudentQuery);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("score")));
            }
        }catch (SQLException e) {
            System.err.println("Error fetching all student: " + e.getMessage());
            throw e;
        }
        return students;
    }
    @Override
    public List<Student> highestStudents() throws SQLException{

        String highestScoreQuery = "SELECT id, name, score FROM Students WHERE score = (SELECT MAX(score) FROM student) ORDER BY name";

        List<Student> students = new ArrayList<>();

        try(Connection connection = Database.getConnection();
            Statement stmt = connection.createStatement()){
            ResultSet rs = stmt.executeQuery(highestScoreQuery);

            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("score")
                ));
            }

        }catch (SQLException e){
            System.out.println("Error to display highest score/marks to all student: "+ e.getMessage());
        }

        return  students;

    }

}