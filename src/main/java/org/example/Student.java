package org.example;

public class Student {
    private int id;
    private String name;
    private int score;
    private final int grade;

    public Student(int id, String name, int score, int grade) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.grade = grade;
    }

    public Student(String name, int score) {
        this.name = name;
        this.score = score;
        this.grade = 10;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public int getScore() { return score; }
    public int getGrade() { return grade; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setScore(int score) { this.score = score; }
}
