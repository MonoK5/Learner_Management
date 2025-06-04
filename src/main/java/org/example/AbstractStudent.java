package org.example;

public abstract class AbstractStudent {
    private int id;
    private String name;
    private int score;
    private int grade;

    public AbstractStudent(int id, String name, int score, int grade) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.grade = grade;
    }

    public AbstractStudent(String name, int score) {
        this.name = name;
        this.score = score;
        this.grade = 10;  // default grade
    }

    // Getters and setters
    public int getId() { return id; }
    public String getName() { return name; }
    public int getScore() { return score; }
    public int getGrade() { return grade; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setScore(int score) { this.score = score; }
    public void setGrade(int grade) { this.grade = grade; }

    // Abstract method that must be implemented by subclasses
    public abstract String getStudentType();

    // Optional concrete method (can be overridden)
    public void displayStudentInfo() {
        System.out.println("ID: " + id + ", Name: " + name + ", Score: " + score + ", Grade: " + grade);
    }
}
