package org.example;

// Student class inherits from AbstractStudent
public class Student extends AbstractStudent {

    // Constructor with all fields
    public Student(int id, String name, int score, int grade) {
        super(id, name, score, grade);
    }

    // Constructor without id, for new students before DB assigns id
    public Student(String name, int score) {
        super(name, score);
    }

    // Return type of student as "Student"
    @Override
    public String getStudentType() {
        return "Student";
    }

    // Display student info with label
    @Override
    public void displayStudentInfo() {
        System.out.println("Student " + getStudentType() + ":");
        super.displayStudentInfo();
    }
}
