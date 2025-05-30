package org.example;

import java.sql.SQLException;
import java.util.List;

abstract  class AbstractStudent {
    protected abstract void addNewStudent(Student newStudents) throws SQLException;
    protected abstract List<Student> displayAllStudent() throws  SQLException;
    protected abstract void updateStudentDetails(Student updateDetails) throws  SQLException;
    abstract double calculateAllMarksAvg (List<Student> calculateAvg) throws  SQLException;
    abstract List<Student> searchStudent(Student search) throws  SQLException;
    abstract boolean deleteStudent(int studentId, String studentName) throws SQLException;
    abstract boolean studentExists(Student exists) throws  SQLException;
    abstract List<Student> getAllStudentMarks() throws SQLException;
    abstract List<Student> highestStudents() throws SQLException;
}