package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StudentMainExtend extends StudentMain {





    @Override
    public void addNewStudent(Student newStudents) throws SQLException {
        String insertQuery = "INSERT INTO Students (name, grade, score) VALUES (?,?,?)";
        try (PreparedStatement preparedStatement = Database.getConnection().prepareStatement(insertQuery) ) {
            preparedStatement.setString(1, newStudents.getName() );
            preparedStatement.setInt(2, newStudents.getGrade());
            preparedStatement.setInt(3, newStudents.getScore());

            preparedStatement.executeUpdate();

        }
    }
    @Override
    public List<Student> displayAllStudent() throws SQLException {


        var students = new ArrayList<Student>();
        String sql  = "SELECT * FROM Students ORDER BY id";

        try (Connection conn =  Database.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("score")

                 ));
            }
        } catch (SQLException e){
            System.out.println("Student details not available " + e.getMessage());
        }
        return students;
    }
    @Override
    public void updateStudentDetails(Student updateDetails) throws SQLException{}
    @Override
    public double calculateAllMarksAvg(List<Student> calculateAvg) throws  SQLException{
        return  0;
    }
    @Override
//    public List<Student> searchStudent(Student search) throws  SQLException{
//        return  new ArrayList<>();
//    }
    public List<Student> searchStudent(Student search) throws SQLException {
        List<Student> matchedStudents = new ArrayList<>();
        String searchQuery = "SELECT * FROM Students WHERE LOWER(name) = ?";

        try (PreparedStatement preparedStatement = Database.getConnection().prepareStatement(searchQuery)) {
            preparedStatement.setString(1, search.getName().toLowerCase());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                matchedStudents.add(new Student(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("score")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error while searching student: " + e.getMessage());
        }

        return matchedStudents;
    }
    @Override
    public void deleteStudent(int studentId) throws SQLException{}
    @Override
    public boolean studentExists(Student student)throws SQLException{
        return  false;
    }
}