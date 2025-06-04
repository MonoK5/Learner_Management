package org.example;

public class Student extends AbstractStudent {

    public Student(int id, String name, int score, int grade) {
        super(id, name, score, grade);
    }

    public Student(String name, int score) {
        super(name, score);
    }

    @Override
    public String getStudentType() {
        return "Student";
    }

    // Optionally override displayStudentInfo()
    @Override
    public void displayStudentInfo() {
        System.out.println("Student " + getStudentType() + ":");
        super.displayStudentInfo();
    }
}
