package org.example;

import java.sql.SQLException;
import java.util.List;

public abstract  class StudentMain{
    abstract  void  addNewStudent(Student newStudents) throws SQLException;
    abstract List<Student> displayAllStudent() throws  SQLException;
    abstract  void updateStudentDetails(Student updateDetails) throws  SQLException;
    abstract  double  calculateAllMarksAvg () throws  SQLException;
    abstract List<Student> searchStudent(String search) throws  SQLException;
    abstract boolean deleteStudent(int studentId) throws SQLException;
    abstract boolean studentExists(Student exists) throws SQLException;
    abstract List<Student> getAllStudentMarks() throws SQLException;
    abstract List<Student> highestStudents() throws SQLException;
    abstract boolean isIdUnique(int id) throws SQLException;
}