package org.example;

import org.example.Student;

import java.sql.SQLException;
import java.util.List;

abstract  class StudentMain{
    protected abstract void addNewStudent(Student newStudents) throws SQLException;
    protected abstract List<Student> displayAllStudent() throws  SQLException;
    protected abstract void updateStudentDetails(Student updateDetails) throws  SQLException;
    abstract double calculateAllMarksAvg (List<Student> calculateAvg) throws  SQLException;
    abstract List<Student> searchStudent(Student search) throws  SQLException;
    abstract void deleteStudent(int studentId) throws SQLException;
    abstract boolean studentExists(Student exists) throws  SQLException;
}